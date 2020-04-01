package com.zy.security.oauth2.token;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.TokenGranter;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 上午12:05:29;
 * @Description:
 */
public abstract class AbstractTokenGranter implements TokenGranter {
	protected AuthorizationServerTokenServices tokenServices;
	
	public AbstractTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super();
		this.tokenServices = tokenServices;
	}

	public AuthorizationServerTokenServices getTokenServices() {
		return tokenServices;
	}
	
	@Override
	public OAuth2AccessToken grant(String grantType, RequestDetails requestDetails,ClientDetails client) throws AuthenticationException {
		return tokenServices.createAccessToken(getOauth2Authentication(requestDetails,client),client);
	}

	protected abstract OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client) throws AuthenticationException;

}
