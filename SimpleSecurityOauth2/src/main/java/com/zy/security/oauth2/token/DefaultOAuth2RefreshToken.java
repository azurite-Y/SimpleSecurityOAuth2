package com.zy.security.oauth2.token;

import com.zy.security.oauth2.interfaces.OAuth2RefreshToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午2:21:07;
 * @Description:
 */
@SuppressWarnings("serial")
public class DefaultOAuth2RefreshToken implements OAuth2RefreshToken {
	
	protected  String value;
	
	public DefaultOAuth2RefreshToken(String value) {
		super();
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "DefaultOAuth2RefreshToken [value=" + value + "]";
	}
}
