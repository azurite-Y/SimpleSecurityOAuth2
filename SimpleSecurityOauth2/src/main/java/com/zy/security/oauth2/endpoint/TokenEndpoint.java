package com.zy.security.oauth2.endpoint;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.oauth2.token.ReplyOauth2AccessToken;
import com.zy.security.web.interfaces.RequestMatcher;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:41:55;
 * @Description: oauth/token 签发AccessToken，限定post请求
 */
public class TokenEndpoint extends AbstractEndpoint  {
	
	public TokenEndpoint(RequestMatcher postRequest, RequestMatcher getRequest) {
		super(postRequest, getRequest);
	}

	@Override
	public boolean endpoint(HttpServletRequest request,HttpServletResponse response, FilterChain chain) throws AuthenticationException {
		if (super.postRequest != null && super.postRequest.match(request)) {
			doPost(request, response);
			return true;
		}
		return false;
	}

	private void doPost(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		Authentication verifiedAuthentication = super.verifiedAuthentication();
		
		String clientId = (String) verifiedAuthentication.getPrincipal();
		if (clientId == null || clientId.isEmpty() || verifiedAuthentication instanceof OAuth2AuthenticationToken) {
			clientId = (String) ((OAuth2AuthenticationToken)verifiedAuthentication).getAuthentication().getPrincipal();
		}
		
		ClientDetails clientDetails = super.clientDetailsService.loadClientByClientId(clientId);
		
		RequestDetails requestDetails = super.createRequestDetails(request);
		requestDetails.setHttpServletRequest(request);
		requestDetails.setHttpServletResponse(response);
		
		super.clientDetailsResolver.resolveClientId(clientDetails, requestDetails);
		// 验证secret
		super.clientDetailsResolver.resolveSecret(requestDetails.getSecret(), clientDetails);
		
		// 防止表单提交的scope参数与预设不符
		List<RolePermission> scope = requestDetails.getScope();
		if (scope != null) {
			super.clientDetailsResolver.resolveScopes(clientDetails,scope );
		}
		
		if (requestDetails.getResourceIds() == null) {
			requestDetails.setResourceIds(clientDetails.getResourceIds());
		}
		
		String grantType = requestDetails.getGrantType();
		super.clientDetailsResolver.resolveGrantTypes(clientDetails,grantType,"implicit" );
		
		OAuth2AccessToken token = super.tokenGranter.grant(grantType, requestDetails, clientDetails);
		
		super.replyJson(response, new ReplyOauth2AccessToken(token));
	}
}
