package com.zy.security.oauth2.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.exception.InvalidRequestException;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.exception.RedirectException;
import com.zy.security.oauth2.exception.UnsupportedResponseTypeException;
import com.zy.security.oauth2.interfaces.AuthorizationCodeServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.UserApprovalHandler;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.oauth2.token.ReplyOauth2AccessToken;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:32:16;
 * @Description: /oauth/authorize - 签发授权码或访问令牌
 */
public class AuthorizationEndpoint extends AbstractEndpoint {
	// 验证用户是否自动授权
	private UserApprovalHandler userApprovalHandler;
	private AuthorizationCodeServices authorizationCodeServices;
	// 简单模式下创建accesstoken的对象锁
	private Object implicitLock = new Object();

	
	public AuthorizationEndpoint(String postUri, String getUri) {
		super(new AntRequestMapping(postUri, HttpMethod.POST), new AntRequestMapping(getUri, HttpMethod.GET));
	}
	public AuthorizationEndpoint(RequestMatcher postRequest, RequestMatcher getRequest) {
		super(postRequest, getRequest);
	}
	public AuthorizationEndpoint(String uri) {
		super(new AnyRequestMapping(uri, HttpMethod.POST), new AnyRequestMapping(uri, HttpMethod.GET));
	}

	@Override
	public boolean endpoint(HttpServletRequest request, HttpServletResponse response,FilterChain chain)
			throws AuthenticationException, OAuth2Exception {
		if (super.getRequest != null && super.getRequest.match(request)) {
			doGet(request, response,chain);
			return true;
		}
		if (super.postRequest != null && super.postRequest.match(request)) {
			doPost(request, response);
			return true;
		}
		return false;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws AuthenticationException, OAuth2Exception {
		Authentication authentication = verifiedAuthentication();

		RequestDetails requestDetails = createRequestDetails(request);

		ClientDetails clientDetails = super.clientDetailsService.loadClientByClientId(requestDetails.getClientId());
		
		String responseType = requestDetails.getResponseType();
		if (!responseType.contains(Oauth2Utils.token) && !responseType.contains(Oauth2Utils.code)) {
			throw new UnsupportedResponseTypeException("不支持的response type: " + responseType);
		}
		
		// 授权码模式的GET请求提供的secret、scope和grantType是非必须的 
		super.clientDetailsResolver.resolveClientId(clientDetails, requestDetails);
		
		String resolveRedirect = super.redirectResolver.resolveRedirect(requestDetails.getRedirectUri(), clientDetails);
		if (resolveRedirect == null || resolveRedirect.isEmpty()) {
			throw new RedirectException("必须为此客户端提供有个重定向URI");
		}
		requestDetails.setRedirectUri(resolveRedirect);
		
		// 判斷用戶是否已同意自動授權
		boolean approved = userApprovalHandler.isApproved(requestDetails, authentication);
		requestDetails.setApproved(approved);
		
		if (requestDetails.getScope() == null) {
			requestDetails.setScope(clientDetails.getScope());
		}
		
		if (requestDetails.getResourceIds() == null) {
			requestDetails.setResourceIds(clientDetails.getResourceIds());
		}
		requestDetails.setHttpServletRequest(request);
		requestDetails.setHttpServletResponse(response);
		
		if (responseType.equals(Oauth2Utils.token)) {
				// 简单模式必须提供clientId和clientSecret
				super.clientDetailsResolver.resolveSecret(requestDetails.getSecret() , clientDetails);
				List<RolePermission> scope = requestDetails.getScope();
				if (scope != null && !scope.isEmpty()) {
					super.clientDetailsResolver.resolveScopes(clientDetails , scope);
				}
				
				implicitGrantResponse(requestDetails, clientDetails);
				return ;
		}
		if (responseType.equals(Oauth2Utils.code)) {
			if (approved) {

				authorizationCodeResponse(requestDetails, authentication);
				return;
			} else {

				HttpSession session = request.getSession(false);
				// 将RequestDetails保存到session中，以在doPost()方法中获得
				session.setAttribute(super.INDEX, requestDetails);

				// 将请求重定向到用户授权页面
				try {
					response.sendRedirect(Oauth2Utils.oauth_confirm_access);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 在授权模式下获得授权码
	 * @param request
	 * @param response
	 * @throws AuthenticationException
	 * @throws OAuth2Exception
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException , OAuth2Exception{
		Authentication authentication = verifiedAuthentication();
		// 已进行过身份验证则代表已创建其对应的session
		HttpSession session = request.getSession(false);
		
		RequestDetails requestDetails = (RequestDetails) session.getAttribute(INDEX);
		if(requestDetails == null) {
			throw new InvalidRequestException("无法批准未初始化的授权请求.");
		}
		
		requestDetails.setHttpServletRequest(request);
		requestDetails.setHttpServletResponse(response);
		
		RequestDetails updateRequestDetails = userApprovalHandler.updateApproval(requestDetails, authentication);
		
		boolean approved = userApprovalHandler.isApproved(updateRequestDetails, authentication);
		// 避免在UserApprovalHandler自定义实现类中未设置RequestDetails的approved属性
		updateRequestDetails.setApproved(approved);
		
		if(updateRequestDetails.getRedirectUri() == null) {
			throw new RedirectException("必须至少向客户端注册一个重定向uri");
		}
		if (requestDetails.getResponseType().contains(Oauth2Utils.token)) {
			throw new InvalidRequestException("不支持的ResponseType，请尝试GET请求.");
		}
		
		this.authorizationCodeResponse(updateRequestDetails, authentication);
	}

	/**
	 * 设置authorizationCode模式下的Response回复
	 * 
	 * @param requestDetails
	 * @param authentication
	 */
	private void authorizationCodeResponse(RequestDetails requestDetails, Authentication authentication)
			throws AuthenticationException {
		String code = authorizationCodeServices
				.createAuthorizationCode(new OAuth2AuthenticationToken(requestDetails, authentication));
	
		HttpServletResponse response = requestDetails.getHttpServletResponse();
		
		StringBuilder builder = new StringBuilder();
		// 拼接重定向uri
		builder.append(requestDetails.getRedirectUri()).append("?code=").append(code).append("&state=")
				.append(requestDetails.getState());
		if (!response.isCommitted()) {
			try {
				response.sendRedirect(builder.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置implicit模式下的Response回复
	 * @param requestDetails
	 */
	private void implicitGrantResponse(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		OAuth2AccessToken accessToken = null;
		synchronized (this.implicitLock) {
			accessToken = tokenGranter.grant("implicit", requestDetails, client);
		}
		super.replyJson(requestDetails.getHttpServletResponse(), new ReplyOauth2AccessToken(accessToken));
	}
	public UserApprovalHandler getUserApprovalHandler() {
		return userApprovalHandler;
	}
	public void setUserApprovalHandler(UserApprovalHandler userApprovalHandler) {
		this.userApprovalHandler = userApprovalHandler;
	}
	public AuthorizationCodeServices getAuthorizationCodeServices() {
		return authorizationCodeServices;
	}
	public void setAuthorizationCodeServices(AuthorizationCodeServices authorizationCodeServices) {
		this.authorizationCodeServices = authorizationCodeServices;
	}
}