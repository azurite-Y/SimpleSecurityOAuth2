package com.zy.security.oauth2.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.OAuth2RefreshToken;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午4:06:30;
 * @Description: 存储和读取访问令牌与身份验证凭据的内存式实现
 */
public class InMemoryTokenStore implements TokenStore {
	// tokenVal:AccessToken
	private ConcurrentHashMap<String, OAuth2AccessToken> accessTokenStore = new ConcurrentHashMap<String, OAuth2AccessToken>();
	// refreshTokenVal：refreshToken
	private ConcurrentHashMap<String, OAuth2RefreshToken> refreshTokenStore = new ConcurrentHashMap<String, OAuth2RefreshToken>();

	// username：<AccessToken>
	private ConcurrentHashMap<String, Collection<OAuth2AccessToken>> userNameToAccessTokenStore = new ConcurrentHashMap<String, Collection<OAuth2AccessToken>>();
	// clientId：<AccessToken>
	private ConcurrentHashMap<String, Collection<OAuth2AccessToken>> clientIdToAccessTokenStore = new ConcurrentHashMap<String, Collection<OAuth2AccessToken>>();

	// AccessTokenVal：Oauth
	private ConcurrentHashMap<String, OAuth2AuthenticationToken> authenticationStore = new ConcurrentHashMap<String, OAuth2AuthenticationToken>();
	// refreshTokenVal：Oauth
	private ConcurrentHashMap<String, OAuth2AuthenticationToken> refreshTokenAuthenticationStore = new ConcurrentHashMap<String, OAuth2AuthenticationToken>();

	// refreshToken : accessToken
	private ConcurrentHashMap<String, String> refreshTokenToAccessTokenStore = new ConcurrentHashMap<String, String>();
	// AccessTokenVal：refreshTokenVal
	private ConcurrentHashMap<String, String> accessTokenToRefreshTokenStore = new ConcurrentHashMap<String, String>();

	// --------------------------
	@Override
	public OAuth2AuthenticationToken readAuthentication(OAuth2AccessToken token) {
		if (token == null) {
			return null;
		}
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2AuthenticationToken readAuthentication(String token) {
		return this.authenticationStore.get(token);
	}

	@Override
	public OAuth2AuthenticationToken readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		if (token == null) {
			return null;
		}
		return readAuthenticationForRefreshToken(token.getValue());
	}

	@Override
	public OAuth2AuthenticationToken readAuthenticationForRefreshToken(String token) {
		return this.refreshTokenAuthenticationStore.get(token);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		return this.accessTokenStore.get(tokenValue);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		return this.refreshTokenStore.get(tokenValue);
	}

	public void removeAccessToken(String value) {
		this.accessTokenStore.remove(value);
		this.accessTokenToRefreshTokenStore.remove(value);
		this.authenticationStore.remove(value);

		this.refreshTokenStore.remove(value);
		this.refreshTokenToAccessTokenStore.remove(value);
		this.refreshTokenAuthenticationStore.remove(value);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		if (token == null) {
			return;
		}
		removeAccessToken(token.getValue());
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		String tokenValue = token.getValue();
		this.refreshTokenStore.remove(tokenValue);
		this.refreshTokenAuthenticationStore.remove(tokenValue);
		this.refreshTokenToAccessTokenStore.remove(tokenValue);
	}

	// --------------------------
	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2AuthenticationToken authentication) {
		if (token == null || authentication == null) {
			return;
		}
		String accessTokenValue = token.getValue();
		this.accessTokenStore.put(accessTokenValue, token);
		this.authenticationStore.put(accessTokenValue, authentication);

		OAuth2RefreshToken refreshToken = token.getRefreshToken();
		String refreshTokenValue = refreshToken.getValue();

		this.accessTokenToRefreshTokenStore.put(accessTokenValue, refreshTokenValue);
		this.refreshTokenToAccessTokenStore.put(refreshTokenValue, accessTokenValue);

		String clientId = authentication.getRequestDetails().getClientId();
		Collection<OAuth2AccessToken> clientCollection = this.clientIdToAccessTokenStore.get(clientId);
		if (clientCollection == null) {
			clientCollection = new ArrayList<>();
			clientCollection.add(token);
			this.clientIdToAccessTokenStore.put(clientId, clientCollection);
		} else {
			clientCollection.add(token);
		}
		
		String principal = authentication.getPrincipal().toString();
		Collection<OAuth2AccessToken> collection = this.userNameToAccessTokenStore.get(principal);
		if (collection == null) {
			collection = new ArrayList<>();
			collection.add(token);
			this.userNameToAccessTokenStore.put(principal, collection);
		} else {
			collection.add(token);
		}
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2AuthenticationToken authentication) {
		String refreshTokenValue = refreshToken.getValue();
		this.refreshTokenAuthenticationStore.put(refreshTokenValue, authentication);
		this.refreshTokenStore.put(refreshTokenValue, refreshToken);
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		if (refreshToken == null) {
			return;
		}
		String accessToken = this.refreshTokenToAccessTokenStore.remove(refreshToken.getValue());
		removeAccessToken(accessToken);
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2AuthenticationToken authentication) {
		Object principal = authentication.getPrincipal();
		Collection<OAuth2AccessToken> collection = this.userNameToAccessTokenStore.get(principal.toString());
		if (collection == null) {
			return null;
		}
		
		return collection.iterator().next();
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		Collection<OAuth2AccessToken> result = this.userNameToAccessTokenStore.get(userName);
		return result;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		return this.clientIdToAccessTokenStore.get(clientId);
	}
}
