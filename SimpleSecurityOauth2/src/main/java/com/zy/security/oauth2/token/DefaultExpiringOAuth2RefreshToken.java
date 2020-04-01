package com.zy.security.oauth2.token;

import java.util.Date;

import com.zy.security.oauth2.interfaces.ExpiringOAuth2RefreshToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:56:39;
 * @Description:
 */
@SuppressWarnings("serial")
public class DefaultExpiringOAuth2RefreshToken extends DefaultOAuth2RefreshToken implements  ExpiringOAuth2RefreshToken {
	private Date expiration;
	
	public DefaultExpiringOAuth2RefreshToken(String value, Date expiration) {
		super(value);
		this.expiration = expiration;
	}

	@Override
	public String getValue() {
		return super.value;
	}
	@Override
	public Date getExpiration() {
		return this.expiration;
	}

	@Override
	public String toString() {
		return "DefaultExpiringOAuth2RefreshToken [expiration=" + expiration + "]";
	}
}
