package com.zy.security.oauth2.interfaces;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 上午12:18:23;
 * @Description: 访问令牌的创建、刷新与检索服务
 */
public interface AuthorizationServerTokenServices {
	/**
	 * 创建与指定凭据关联的访问令牌
	 * @param authentication
	 * @param client 
	 * @return
	 * @throws AuthenticationException
	 */
	OAuth2AccessToken createAccessToken(OAuth2AuthenticationToken authentication, ClientDetails client) throws AuthenticationException;

	/**
	 * 刷新访问令牌
	 * @param refreshToken
	 * @param requestDetails
	 * @return
	 * @throws AuthenticationException
	 */
	OAuth2AccessToken refreshAccessToken(String refreshToken, RequestDetails requestDetails,ClientDetails client) throws AuthenticationException;

	/**
	 * 检索根据提供的身份验证密钥存储的访问令牌（如果存在）
	 * @param authentication
	 * @return
	 */
	OAuth2AccessToken getAccessToken(OAuth2AuthenticationToken authentication);
}
