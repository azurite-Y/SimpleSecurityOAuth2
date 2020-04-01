package com.zy.security.oauth2.details;

import java.util.List;
import java.util.Map;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.ClientDetails;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午7:43:27;
 * @Description: 基础的客户端信息javabean
 */
@SuppressWarnings("serial")
public class BasicClientDetails implements ClientDetails {
	private String clientId;
	private String clientSecret;
	private List<String> resourceIds;
	private List<RolePermission> scopes;
	private List<String> authorizedGrantTypes;
	private List<String> registeredRedirectUris;
	private boolean autoApprove;
	private List<String> autoApproveScopes;
	private List<RolePermission> authorities;
	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;
	private Map<String, Object> additionalInformation;
	
	public BasicClientDetails(String clientId, String clientSecret, List<String> resourceIds,
			List<RolePermission> scopes, List<String> authorizedGrantTypes, List<String> registeredRedirectUris,
			List<String> autoApproveScopes, List<RolePermission> authorities, Integer accessTokenValiditySeconds,
			Integer refreshTokenValiditySeconds, Map<String, Object> additionalInformation) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.resourceIds = resourceIds;
		this.scopes = scopes;
		this.authorizedGrantTypes = authorizedGrantTypes;
		this.registeredRedirectUris = registeredRedirectUris;
		this.autoApproveScopes = autoApproveScopes;
		this.authorities = authorities;
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		this.additionalInformation = additionalInformation;
	}
	public BasicClientDetails() {
		super();
	}
	
	@Override
	public String getClientId() {
		return this.clientId;
	}
	
	@Override
	public List<String> getResourceIds() {
		return this.resourceIds;
	}
	
	@Override
	public String getClientSecret() {
		return this.clientSecret;
	}
	
	@Override
	public boolean isScoped() {
		return this.scopes != null && !this.scopes.isEmpty();
	}
	
	@Override
	public List<RolePermission> getScope() {
		return this.scopes;
	}
	
	@Override
	public List<String> getAuthorizedGrantTypes() {
		return this.authorizedGrantTypes;
	}
	
	@Override
	public List<String> getRegisteredRedirectUri() {
		return this.registeredRedirectUris;
	}
	
	@Override
	public List<RolePermission> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public Integer getAccessTokenValiditySeconds() {
		return this.accessTokenValiditySeconds;
	}
	
	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return this.refreshTokenValiditySeconds;
	}
	
	@Override
	public boolean isAutoApprove(String scope) {
		if (autoApproveScopes == null || !this.autoApprove) {
			return false;
		}
		for (String auto : autoApproveScopes) {
			if (this.autoApprove && scope.matches(auto)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Map<String, Object> getAdditionalInformation() {
		return this.additionalInformation;
	}
	
	public void addAdditionalInformation(String key, Object value) {
		this.additionalInformation.put(key, value);
	}
	public List<RolePermission> getScopes() {
		return scopes;
	}
	public void setScopes(List<RolePermission> scopes) {
		this.scopes = scopes;
	}
	public List<String> getRegisteredRedirectUris() {
		return registeredRedirectUris;
	}
	public void setRegisteredRedirectUris(List<String> registeredRedirectUris) {
		this.registeredRedirectUris = registeredRedirectUris;
	}
	public List<String> getAutoApproveScopes() {
		return autoApproveScopes;
	}
	public void setAutoApproveScopes(List<String> autoApproveScopes) {
		this.autoApproveScopes = autoApproveScopes;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
	public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}
	public void setAuthorities(List<RolePermission> authorities) {
		this.authorities = authorities;
	}
	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public boolean isAutoApprove() {
		return autoApprove;
	}
	public void setAutoApprove(boolean autoApprove) {
		this.autoApprove = autoApprove;
	}
}
