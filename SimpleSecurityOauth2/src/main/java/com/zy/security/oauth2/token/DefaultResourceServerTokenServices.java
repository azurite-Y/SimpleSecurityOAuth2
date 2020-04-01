package com.zy.security.oauth2.token;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.InvalidRequestException;
import com.zy.security.oauth2.exception.InvalidTokenException;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.ResourceServerTokenServices;
import com.zy.security.oauth2.interfaces.TokenStore;

/**
 * @author: zy;
 * @DateTime: 2020年3月26日 下午7:38:45;
 * @Description:
 */
public class DefaultResourceServerTokenServices implements ResourceServerTokenServices {
	private TokenStore tokenStore;

	@Override
	public OAuth2AuthenticationToken loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
		return loadAuthentication(token);
	}
	
	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		return tokenStore.readAccessToken(accessToken);
	}

	@Override
	public OAuth2AuthenticationToken loadAuthentication(OAuth2AccessToken accessToken)
			throws AuthenticationException, InvalidTokenException {
		if(accessToken == null) {
			throw new InvalidRequestException("请求未携带AccessToken访问受Oauth2协议保护的URL");
		}
		if (accessToken.isExpired()) {
			tokenStore.removeAccessToken(accessToken);
			throw new InvalidTokenException("AccessToken已过期: " + accessToken);
		}

		OAuth2AuthenticationToken result = tokenStore.readAuthentication(accessToken);
		if (result == null) {
			throw new InvalidTokenException("失效的 Access token: " + accessToken);
		}
		return result;
	}

	@Override
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
	public TokenStore getTokenStore() {
		return tokenStore;
	}
	public DefaultResourceServerTokenServices(TokenStore tokenStore) {
		super();
		this.tokenStore = tokenStore;
	}
	public DefaultResourceServerTokenServices() {
		super();
	}
}
