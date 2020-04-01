package com.zy.security.oauth2.exception;

import com.zy.security.core.authentication.exception.AuthenticationException;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午3:18:36;
 * @Description:
 */
@SuppressWarnings("serial")
public class OAuth2AccessDeniedException extends AuthenticationException {
	public OAuth2AccessDeniedException() {}

	public OAuth2AccessDeniedException(String msg) {
		super(msg);
	}

	public OAuth2AccessDeniedException(String msg,Throwable t) {
		super(msg,t);
	}
}
