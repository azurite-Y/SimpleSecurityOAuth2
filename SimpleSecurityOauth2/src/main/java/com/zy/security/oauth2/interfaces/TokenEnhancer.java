package com.zy.security.oauth2.interfaces;

import com.zy.security.oauth2.token.OAuth2AuthenticationToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午4:54:36;
 * @Description: 令牌增强器，用于自定义的创建令牌
 */
public interface TokenEnhancer {
	OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2AuthenticationToken authentication);
}
