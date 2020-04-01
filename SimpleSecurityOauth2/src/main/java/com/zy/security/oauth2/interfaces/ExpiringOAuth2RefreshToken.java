package com.zy.security.oauth2.interfaces;

import java.util.Date;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:57:38;
 * @Description: 有失效时间的refreshToken
 */
public interface ExpiringOAuth2RefreshToken extends OAuth2RefreshToken {
	Date getExpiration();
}
