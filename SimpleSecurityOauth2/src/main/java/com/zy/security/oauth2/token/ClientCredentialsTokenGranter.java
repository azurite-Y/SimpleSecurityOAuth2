package com.zy.security.oauth2.token;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午12:59:58;
 * @Description: 针对客户端模式下的AccessToken创建
 */
public class ClientCredentialsTokenGranter extends AbstractTokenGranter {
	// 是否启用客户端模式的刷新令牌
	private boolean RefreshTokenEnable;

	
	/**
	 * 创建根据配置对RefreshToken进行取舍对象的构造器
	 * @param tokenServices
	 * @param refreshTokenEnable - true为保留
	 */
	public ClientCredentialsTokenGranter(AuthorizationServerTokenServices tokenServices, boolean refreshTokenEnable) {
		super(tokenServices);
		RefreshTokenEnable = refreshTokenEnable;
	}

	public ClientCredentialsTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	public boolean supports(String granterType) {
		return granterType.equals(Oauth2Utils.client_credentials);
	}

	/**
	 * 獲得AccessToken之後，根据配置对RefreshToken进行取舍
	 */
	@Override
	public OAuth2AccessToken grant(String grantType, RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		OAuth2AccessToken oAuth2AccessToken = super.grant(grantType, requestDetails, client);
		if (oAuth2AccessToken != null) {
			// 规范中默認不应允许客户端凭据获取刷新令牌
			if (!RefreshTokenEnable) {
				oAuth2AccessToken.setRefreshToken("默认不允许客户端凭据获取刷新令牌");
			}
		}
		return oAuth2AccessToken;
	}

	@Override
	protected OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		return new OAuth2AuthenticationToken(requestDetails, authentication);
	}

	public boolean isRefreshTokenEnable() {
		return RefreshTokenEnable;
	}
	public void setRefreshTokenEnable(boolean refreshTokenEnable) {
		RefreshTokenEnable = refreshTokenEnable;
	}
}
