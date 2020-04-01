package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午1:34:51;
 * @Description:
 */
@SuppressWarnings("serial")
public class InvalidRequestException extends ClientAuthenticationException {
	public InvalidRequestException(String msg) {
		super(msg);
	}
	public InvalidRequestException(String msg, Throwable t) {
		super(msg, t);
	}
}
