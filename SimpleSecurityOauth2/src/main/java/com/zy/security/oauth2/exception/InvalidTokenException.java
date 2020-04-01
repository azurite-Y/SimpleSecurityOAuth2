package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月21日 下午3:57:24;
 * @Description: 令牌失效引发的异常
 */
@SuppressWarnings("serial")
public class InvalidTokenException extends OAuth2Exception {
	public InvalidTokenException(String msg) {
		super(msg);
	}
	public InvalidTokenException(String msg, Throwable t) {
		super(msg, t);
	}
}
