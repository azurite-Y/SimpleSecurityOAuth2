package com.zy.security.oauth2.interfaces;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.InvalidTokenException;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月21日 下午3:56:15;
 * @Description: 资源服务器的令牌服务
 */
public interface ResourceServerTokenServices {
	/**
	 * 仅从值检索完整的访问令牌详细信息
	 */
	OAuth2AccessToken readAccessToken(String accessToken);
	
	/**
	 * 加载指定访问令牌的已认证凭据，未抛出异常则保证OAuth2Authentication不为null
	 */
	OAuth2AuthenticationToken loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException;
	/**
	 * 加载指定访问令牌的凭据，为抛出异常则保证OAuth2Authentication不为null
	 */
	OAuth2AuthenticationToken loadAuthentication(OAuth2AccessToken accessToken)
			throws AuthenticationException, InvalidTokenException;
	
	/**
	 * 使用此方法对自定义配置的本接口实现类中的TokenStore属性进行赋值
	 * @param tokenStore
	 */
	void setTokenStore(TokenStore tokenStore);
}
