package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午5:54:07;
 * @Description: 无效的客户端异常，包括clientId为null
 */
@SuppressWarnings("serial")
public class InvalidClientException extends OAuth2Exception {

	public InvalidClientException(String msg) {
		super(msg);
	}
	public InvalidClientException(String msg, Throwable t) {
		super(msg, t);
	}

}
