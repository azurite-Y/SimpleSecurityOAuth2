package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:25:02;
 * @Description: 客户端认证异常
 */
@SuppressWarnings("serial")
public class ClientAuthenticationException extends InvalidClientException {

	public ClientAuthenticationException(String msg) {
		super(msg);
	}
	public ClientAuthenticationException(String msg, Throwable t) {
		super(msg, t);
	}

}
