package com.zy.security.oauth2.token;

import java.util.ArrayList;
import java.util.List;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;

/**
 * @author: zy;
 * @DateTime: 2020年3月20日 下午10:46:47;
 * @Description: 将作为response的body返回的AccessToken
 */
public class ReplyOauth2AccessToken {
	private String access_token;
	private String token_type;
	private String refresh_token;
	private String expires_in;
	private List<String> scope = new ArrayList<>();

	public ReplyOauth2AccessToken(OAuth2AccessToken token) {
		super();
		this.access_token = token.getValue();
		this.token_type = token.getTokenType();
		this.refresh_token = token.getRefreshToken().getValue();
		long exp = ( token.getExpiration().getTime() - System.currentTimeMillis() )/1000;
		this.expires_in = Long.toString(exp);
		List<RolePermission> scopes = token.getScope();
		for (RolePermission rolePermission : scopes) {
			if(rolePermission != null) {
				this.scope.add(rolePermission.getValue());
			}
		}
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}
}
