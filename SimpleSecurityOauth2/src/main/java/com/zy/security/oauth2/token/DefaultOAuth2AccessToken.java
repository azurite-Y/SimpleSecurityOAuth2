package com.zy.security.oauth2.token;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.OAuth2RefreshToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:59:53;
 * @Description:
 */
public class DefaultOAuth2AccessToken implements OAuth2AccessToken {
	private String value;
	private Date expiration;
	private String tokenType;
	private OAuth2RefreshToken refreshToken;
	// 访问令牌的作用域
	private List<RolePermission> scope;
	// 附加信息
	private Map<String, Object> additionalInformation = Collections.emptyMap();
	
	public DefaultOAuth2AccessToken(String value) {
		super();
		this.value = value;
	}
	
	@Override
	public Map<String, Object> getAdditionalInformation() {
		return this.additionalInformation;
	}
	@Override
	public List<RolePermission> getScope() {
		return this.scope;
	}
	@Override
	public OAuth2RefreshToken getRefreshToken() {
		return this.refreshToken;
	}
	@Override
	public String getTokenType() {
		return this.tokenType;
	}
	@Override
	public boolean isExpired() {
		// 当前日期是否在参数日期之前，true：是在参数日期之前，即代表访问令牌已过期
		return expiration != null && expiration.before(new Date());
	}
	@Override
	public Date getExpiration() {
		return this.expiration;
	}
	@Override
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public void setRefreshToken(OAuth2RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}
	public void setScope(List<RolePermission> scope) {
		this.scope = scope;
	}
	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		this.additionalInformation.putAll(additionalInformation);
	}
	@Override
	public void setRefreshToken(String value) {
		this.refreshToken = new DefaultOAuth2RefreshToken(value);
	}
	@Override
	public String toString() {
		return "DefaultOAuth2AccessToken [value=" + value + ", expiration=" + expiration + ", tokenType=" + tokenType
				+ ", refreshToken=" + refreshToken + ", scope=" + scope + ", additionalInformation="
				+ additionalInformation + "]";
	}
}
