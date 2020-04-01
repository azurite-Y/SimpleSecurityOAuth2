package com.zy.security.oauth2.interfaces;

import java.util.Map;

import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月21日 下午3:59:57;
 * @Description: 转换令牌的显示格式
 */
public interface AccessTokenConverter {
	/**
	 * 转换访问令牌
	 * @param token
	 * @param authentication
	 * @return
	 */
	Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2AuthenticationToken authentication);

	/**
	 * 提取访问令牌
	 * @param value
	 * @param map
	 * @return
	 */
	OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map);

	/**
	 * 提取Authentication
	 * @param map
	 * @return
	 */
	OAuth2AuthenticationToken extractAuthentication(Map<String, ?> map);
}
