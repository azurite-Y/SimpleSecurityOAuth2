package com.zy.security.oauth2.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zy.security.core.userdetails.RolePermission;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:48:13;
 * @Description:
 */
public interface OAuth2AccessToken {
	Map<String, Object> getAdditionalInformation();
	List<RolePermission> getScope();
	OAuth2RefreshToken getRefreshToken();
	void setRefreshToken(String value);
	String getTokenType();
	/**
	 * 返回true为已过期，false则未过期
	 */
	boolean isExpired();
	Date getExpiration();
	String getValue();
}
