package com.zy.security.oauth2.token;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ExpiringOAuth2RefreshToken;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.OAuth2RefreshToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午3:53:23;
 * @Description:
 */
public class DefaultAuthorizationServerTokenServices implements AuthorizationServerTokenServices {
	// sprng security oauth2默认30天 - 60 * 60 * 24 * 30
	protected int refreshTokenValiditySeconds;
	// sprng security oauth2默认12天 - 60 * 60 * 24 * 12
	protected int accessTokenValiditySeconds;
	protected TokenStore tokenStore;
	// 更新访问令牌时是否需要更换刷新令牌
	protected boolean changeRefreshToken;
	
	
	public DefaultAuthorizationServerTokenServices() {
		super();
	}
	/**
	 * 默认构造器
	 * @param refreshTokenValiditySeconds
	 * @param accessTokenValiditySeconds
	 * @param tokenStore
	 * @param changeRefreshToken - 更新访问令牌时是否需要重新创建刷新令牌,true则更换
	 */
	public DefaultAuthorizationServerTokenServices(int refreshTokenValiditySeconds, int accessTokenValiditySeconds, TokenStore tokenStore,
			boolean changeRefreshToken) {
		super();
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		this.tokenStore = tokenStore;
		this.changeRefreshToken = changeRefreshToken;
	}

	
	@Override
	public OAuth2AccessToken createAccessToken(OAuth2AuthenticationToken authentication,ClientDetails client) throws AuthenticationException {
		OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
		OAuth2RefreshToken refreshToken = null;
		if (accessToken != null) {
			if (accessToken.isExpired()) {
				if (accessToken.getRefreshToken() != null) {
					refreshToken = accessToken.getRefreshToken();
					tokenStore.removeRefreshToken(refreshToken);
				}
				// 删除过期的访问令牌
				tokenStore.removeAccessToken(accessToken);
			}
			else {
				// 更新访问令牌的身份验证信息
				tokenStore.storeAccessToken(accessToken, authentication);
				return accessToken;
			}
		}

		if (refreshToken == null) {
			refreshToken = createRefreshToken(authentication,client);
		}
		else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
			ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
			// 若存在的刷新令牌已过期则重新创建刷新令牌
			if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
				refreshToken = createRefreshToken(authentication,client);
			}
		}
		
		// 到此则存储中无访问令牌或已过期
		accessToken = createAccessToken(authentication, refreshToken,client);
		
		tokenStore.storeAccessToken(accessToken, authentication);
		refreshToken = accessToken.getRefreshToken();
		if (refreshToken != null) {
			tokenStore.storeRefreshToken(refreshToken, authentication);
		}
		return accessToken;
	}

	/**
	 * 根据oauth与刷新令牌创建访问令牌
	 * @param authentication
	 * @param refreshToken
	 * @return
	 */
	private OAuth2AccessToken createAccessToken(OAuth2AuthenticationToken authentication, OAuth2RefreshToken refreshToken,ClientDetails client) {
		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
		int validitySeconds = client.getAccessTokenValiditySeconds();
		if (validitySeconds > 0) {
			token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
		}
		token.setRefreshToken(refreshToken);
		List<RolePermission> scope = authentication.getRequestDetails().getScope();
		if (scope == null) {
			scope = client.getScope();
		}
		token.setScope(scope);
		token.setTokenType(Oauth2Utils.bearer_type);
		return token;
	}

	/**
	 * 根据已认证的身份验证信息创建刷新令牌
	 * @param authentication
	 * @param client 
	 * @return
	 */
	private OAuth2RefreshToken createRefreshToken(OAuth2AuthenticationToken authentication, ClientDetails client) {
		int validitySeconds = client.getRefreshTokenValiditySeconds();
		String value = UUID.randomUUID().toString();
		if (validitySeconds > 0) {
			return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
					+ (validitySeconds * 1000L)));
		}
		return new DefaultOAuth2RefreshToken(value);
	}

	@Override
	public OAuth2AccessToken refreshAccessToken(String refreshToken, RequestDetails requestDetails,ClientDetails client)
			throws AuthenticationException {
		OAuth2RefreshToken oauth2RefreshToken = tokenStore.readRefreshToken(refreshToken);
		if (oauth2RefreshToken == null) {
			throw new InvalidGrantException("失效的令牌: " + refreshToken);
		}
		
		// 在存储访问令牌和刷新令牌的时候，要存储的OAuth2AuthenticationToken已保证是认证过的
		OAuth2AuthenticationToken authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
		
		// 重新更换刷新令牌
		if(this.changeRefreshToken) {
			oauth2RefreshToken = createRefreshToken(authentication, client);
		}
		
		OAuth2AccessToken createAccessToken = createAccessToken(authentication, oauth2RefreshToken, client);
		return createAccessToken;
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2AuthenticationToken authentication) {
		return tokenStore.getAccessToken(authentication);
	}
	
	//------------------get、set----------------------
	public int getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}
	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
	public int getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}
	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
	public TokenStore getTokenStore() {
		return tokenStore;
	}
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
	public boolean isChangeRefreshToken() {
		return changeRefreshToken;
	}
	public void setChangeRefreshToken(boolean changeRefreshToken) {
		this.changeRefreshToken = changeRefreshToken;
	}
}
