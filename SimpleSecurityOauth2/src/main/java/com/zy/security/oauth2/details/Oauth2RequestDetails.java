package com.zy.security.oauth2.details;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.RequestDetails;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午4:59:32;
 * @Description:
 */
public class Oauth2RequestDetails implements RequestDetails {
	private String clientId;
	private String secret;
	private List<RolePermission> scope;
	private String state;
	private String responseType;
	private String grantType;
	private List<RolePermission> authorities;
	private List<String> resourceIds = new ArrayList<String>();
	private String redirectUri;
	private boolean approved;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	
	public Oauth2RequestDetails(String clientId, String secret, List<RolePermission> scope, String state, String responseTypes,
			String redirectUri,String grantType) {
		super();
		this.clientId = clientId;
		this.secret = secret;
		this.scope = scope;
		this.state = state;
		this.responseType = responseTypes;
		this.redirectUri = redirectUri;
		this.grantType = grantType;
	}
	
	//-----------------------Get、Set------------------------
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public List<RolePermission> getScope() {
		return scope;
	}
	public void setScope(List<RolePermission> scope) {
		this.scope = scope;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public List<RolePermission> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<RolePermission> authorities) {
		this.authorities = authorities;
	}
	public List<String> getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public HttpServletRequest getReq() {
		return req;
	}
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}
	public HttpServletResponse getResp() {
		return resp;
	}
	public void setResp(HttpServletResponse resp) {
		this.resp = resp;
	}
	@Override
	public HttpServletRequest getHttpServletRequest() {
		return this.req;
	}
	@Override
	public HttpServletResponse getHttpServletResponse() {
		return resp;
	}
	@Override
	public void setHttpServletRequest(HttpServletRequest req) {
		this.req = req;
	}
	@Override
	public void setHttpServletResponse(HttpServletResponse resp) {
		this.resp = resp;
	}
}
