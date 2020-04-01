package com.zy.security.oauth2.token;

import java.util.Collection;

import com.zy.security.core.token.AbstractAuthentication;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.RequestDetails;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午11:55:27;
 * @Description: oauth2认证token
 */
@SuppressWarnings("serial")
public class OAuth2AuthenticationToken extends AbstractAuthentication {
	
	private RequestDetails requestDetails;
	// 客户端认证信息的引用
	private Authentication authentication;
	
	
	public OAuth2AuthenticationToken(RequestDetails requestDetails,	Authentication authentication) {
		// 可使用scope代替authorities
		super(requestDetails.getAuthorities() == null ? requestDetails.getScope() : requestDetails.getAuthorities());
		this.requestDetails = requestDetails;
		this.authentication = authentication;
		super.authenticated = true;
	}

	public OAuth2AuthenticationToken(Collection<? extends RolePermission> authorities) {
		super(authorities);
	}

	@Override
	public Object getPrincipal() {
		// 此处获得clientId
		return authentication==null?null:authentication.getPrincipal();
	}

	public RequestDetails getRequestDetails() {
		return requestDetails;
	}
	public void setRequestDetails(RequestDetails requestDetails) {
		this.requestDetails = requestDetails;
	}
	public Authentication getAuthentication() {
		return authentication;
	}
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}
