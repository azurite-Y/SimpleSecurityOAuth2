package com.zy.security.oauth2.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.userdetails.RolePermission;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午4:56:34;
 * @Description: 
 */
public interface RequestDetails {
	public HttpServletRequest getHttpServletRequest();
	public HttpServletResponse getHttpServletResponse();
	public void setHttpServletRequest(HttpServletRequest req);
	public void setHttpServletResponse(HttpServletResponse resp);
	public String getClientId();
	public void setClientId(String clientId);
	public String getSecret();
	public void setSecret(String secret);
	public List<RolePermission> getScope();
	public void setScope(List<RolePermission> scope);
	public String getState();
	public void setState(String state);
	public String getResponseType();
	public void setResponseType(String responseTypes);
	public String getGrantType();
	public void setGrantType(String list);
	public List<RolePermission> getAuthorities();
	public void setAuthorities(List<RolePermission> authorities);
	public List<String> getResourceIds();
	public void setResourceIds(List<String> resourceIds);
	public String getRedirectUri();
	public void setRedirectUri(String redirectUri);
	public boolean isApproved();
	public void setApproved(boolean approved);
}
