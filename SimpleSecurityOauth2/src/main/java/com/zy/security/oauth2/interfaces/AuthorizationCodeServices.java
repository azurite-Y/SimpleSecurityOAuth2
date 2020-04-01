package com.zy.security.oauth2.interfaces;

import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午1:28:39;
 * @Description: 签发和储存授权码的服务
 */
public interface AuthorizationCodeServices {
	
	/**
	 * 为指定的已认证用户或客户端创建授权代码
	 * @param authentication
	 * @return
	 */
	String createAuthorizationCode(OAuth2AuthenticationToken authentication);

	/**
	 * 使用授权代码
	 * @param code
	 * @return
	 * @throws InvalidGrantException
	 */
	OAuth2AuthenticationToken consumeAuthorizationCode(String code) throws InvalidGrantException;
}
