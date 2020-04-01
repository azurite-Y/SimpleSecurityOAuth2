package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午7:39:31;
 * @Description: client_secret错误异常
 */
@SuppressWarnings("serial")
public class ClientSecretException extends ClientAuthenticationException {
	public ClientSecretException(String msg) {
		super(msg);
	}
	public ClientSecretException(String msg, Throwable t) {
		super(msg, t);
	}
}
