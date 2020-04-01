package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:35:47;
 * @Description: 
 */
@SuppressWarnings("serial")
public class InvalidScopeException extends OAuth2Exception {
	public InvalidScopeException(String msg) {
		super(msg);
	}
	public InvalidScopeException(String msg, Throwable t) {
		super(msg, t);
	}
}
