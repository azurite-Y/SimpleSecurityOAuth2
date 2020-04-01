package com.zy.security.oauth2.interfaces;

import java.util.Collection;

import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午3:58:06;
 * @Description: 存储和读取访问令牌与身份验证凭据
 */
public interface TokenStore {
	/**
	 * 读取存储在指定令牌值下的OAuth2Authentication
	 */
	OAuth2AuthenticationToken readAuthentication(OAuth2AccessToken token);

	/**
	 * 读取存储在指定令牌值下的OAuth2Authentication.
	 */
	OAuth2AuthenticationToken readAuthentication(String token);

	/**
	 * 存储访问令牌.
	 */
	void storeAccessToken(OAuth2AccessToken token, OAuth2AuthenticationToken authentication);

	/**
	 * 从存储中读取访问令牌.
	 */
	OAuth2AccessToken readAccessToken(String tokenValue);

	/**
	 * 从存储中删除访问令牌
	 */
	void removeAccessToken(OAuth2AccessToken token);

	/**
	 * 将指定的刷新令牌存储在存储中.
	 */
	void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2AuthenticationToken authentication);

	/**
	 * 从存储读取刷新令牌.
	 */
	OAuth2RefreshToken readRefreshToken(String tokenValue);

	/**
	 * 读取刷新令牌的OAuth2Authentication
	 */
	OAuth2AuthenticationToken readAuthenticationForRefreshToken(OAuth2RefreshToken token);
	/**
	 * 读取刷新令牌的OAuth2Authentication
	 */
	OAuth2AuthenticationToken readAuthenticationForRefreshToken(String token);
	/**
	 * 从存储中删除刷新令牌.
	 */
	void removeRefreshToken(OAuth2RefreshToken token);

	/**
	 * 使用刷新令牌删除访问令牌，刷新令牌与访问令牌是一对一的关系
	 */
	void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken);

	/**
	 * 检索根据提供的身份验证密钥存储的访问令牌
	 */
	OAuth2AccessToken getAccessToken(OAuth2AuthenticationToken authentication);

	/**
	 * 根据clientId和username查找访问令牌
	 */
	Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName);

	/**
	 * 根据clientId查找访问令牌
	 */
	Collection<OAuth2AccessToken> findTokensByClientId(String clientId);
}
