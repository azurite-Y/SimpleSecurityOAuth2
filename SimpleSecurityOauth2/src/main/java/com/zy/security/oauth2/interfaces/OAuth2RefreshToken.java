package com.zy.security.oauth2.interfaces;

import java.io.Serializable;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:50:14;
 * @Description:
 */
public interface OAuth2RefreshToken extends Serializable {
	String getValue();
}
