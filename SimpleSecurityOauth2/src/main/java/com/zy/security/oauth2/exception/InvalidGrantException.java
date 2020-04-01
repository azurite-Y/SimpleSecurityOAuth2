package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午8:35:42;
 * @Description:
 */
@SuppressWarnings("serial")
public class InvalidGrantException extends ClientAuthenticationException {
	public InvalidGrantException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidGrantException(String msg) {
		super(msg);
	}
}
