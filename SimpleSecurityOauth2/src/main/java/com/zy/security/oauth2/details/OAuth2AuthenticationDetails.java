package com.zy.security.oauth2.details;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午4:02:24;
 * @Description:
 */
@SuppressWarnings("serial")
public class OAuth2AuthenticationDetails implements Serializable {
	private String remoteAddress;
	private String sessionId;
	private String tokenValue;
	private String tokenType;
	//-----------------------------Construction ------------------------------------------
	public OAuth2AuthenticationDetails(HttpServletRequest request) {
		this.remoteAddress = request.getRemoteAddr();
		HttpSession session = request.getSession(false);
		this.sessionId = (session != null) ? session.getId() : null;
		this.tokenValue = request.getParameter(Oauth2Utils.token);
		this.tokenType = Oauth2Utils.grant_type;
	}
	//---------------------------------get、set-------------------------------------------------
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public String getSessionId() {
		return sessionId;
	}
	public String getTokenValue() {
		return tokenValue;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	@Override
	public String toString() {
		return "OAuth2AuthenticationDetails [remoteAddress=" + remoteAddress + ", sessionId=" + sessionId
				+ ", tokenValue=" + tokenValue + ", tokenType=" + tokenType + "]";
	}
}
