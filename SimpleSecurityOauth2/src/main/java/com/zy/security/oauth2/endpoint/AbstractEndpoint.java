package com.zy.security.oauth2.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.AnonymousAuthenticationToken;
import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.details.Oauth2RequestDetails;
import com.zy.security.oauth2.exception.InvalidClientException;
import com.zy.security.oauth2.exception.UnsupportedResponseTypeException;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.interfaces.Endpoint;
import com.zy.security.oauth2.interfaces.RedirectResolver;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.TokenGranter;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.oauth2.utils.ResponseUtils;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AuthorityUtils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:49:18;
 * @Description:
 */
public abstract class AbstractEndpoint extends ResponseUtils implements Endpoint {
	// session存储RequstDetails对象的索引
	protected final String  INDEX = "requestDetails_index";
	
	protected RequestMatcher postRequest;
	protected RequestMatcher getRequest;
	
	protected ClientDetailsService clientDetailsService;
	protected RedirectResolver redirectResolver;
	protected ClientDetailsResolver clientDetailsResolver;
	protected TokenGranter tokenGranter;
	
	public AbstractEndpoint(RequestMatcher postRequest, RequestMatcher getRequest) {
		super();
		this.postRequest = postRequest;
		this.getRequest = getRequest;
	}
	
	/**
	 * 根据请求参数创建RequestDetails，保证返回的RequestDetails的ClientId不为null
	 * @param req
	 * @return
	 */
	protected RequestDetails createRequestDetails(HttpServletRequest req) {
		String clientId = req.getParameter(Oauth2Utils.client_id);
		if (clientId == null) {
			throw new InvalidClientException("必须提供客户端id");
		}

		String secret = req.getParameter(Oauth2Utils.client_secret);
//		if (secret == null) {
//			throw new InvalidClientException("必须提供客户端secret");
//		}

		String redirectUri = req.getParameter(Oauth2Utils.redirect_uri);
		String scope = req.getParameter(Oauth2Utils.scope);
		String state = req.getParameter(Oauth2Utils.state);
		String grantType = req.getParameter(Oauth2Utils.grant_type);
		String resourceId = req.getParameter(Oauth2Utils.resources_id);
		
		List<String> resourceIds = null;
		if (resourceId != null) {
			if (resourceId.indexOf(",")  != -1) {
				String[] split = resourceId.split(",");
				resourceIds = Arrays.asList(split);
			} else {
				resourceIds = new ArrayList<>();
				resourceIds.add(resourceId);
			}
		}
		
		String responseType = req.getParameter(Oauth2Utils.response_type);
		if(responseType != null) {
			if (!responseType.contains("token") && !responseType.contains("code")) {
				throw new UnsupportedResponseTypeException("不支持的 response type: " + responseType);
			}
		}
		
		Oauth2RequestDetails oauth2RequestDetails = null;
		if (scope == null ) {
			oauth2RequestDetails = new Oauth2RequestDetails(clientId, secret, null, state, responseType, redirectUri, grantType);
			oauth2RequestDetails.setResourceIds(resourceIds);
			return oauth2RequestDetails;
		}
		oauth2RequestDetails = new Oauth2RequestDetails(clientId, secret, AuthorityUtils.createAuthorityList(scope)
				, state, responseType, redirectUri, grantType);
		oauth2RequestDetails.setResourceIds(resourceIds);
		return oauth2RequestDetails;
				
	}

	/**
	 * 判断用户是否已进行身份验证
	 * @return
	 * @throws AuthenticationException
	 */
	protected Authentication verifiedAuthentication() throws AuthenticationException {
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			throw new AuthenticationException("用户尚未进行身份认证，不能进行授权或获得访问令牌!");
		}
		return authentication;
	}
	
	public RequestMatcher getPostRequest() {
		return postRequest;
	}
	public void setPostRequest(RequestMatcher postRequest) {
		this.postRequest = postRequest;
	}
	public RequestMatcher getGetRequest() {
		return getRequest;
	}
	public void setGetRequest(RequestMatcher getRequest) {
		this.getRequest = getRequest;
	}
	public ClientDetailsService getClientDetailsService() {
		return clientDetailsService;
	}
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
	public RedirectResolver getRedirectResolver() {
		return redirectResolver;
	}
	public void setRedirectResolver(RedirectResolver redirectResolver) {
		this.redirectResolver = redirectResolver;
	}
	public ClientDetailsResolver getClientDetailsResolver() {
		return clientDetailsResolver;
	}
	public void setClientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
	}
	public TokenGranter getTokenGranter() {
		return tokenGranter;
	}
	public void setTokenGranter(TokenGranter tokenGranter) {
		this.tokenGranter = tokenGranter;
	}
}
