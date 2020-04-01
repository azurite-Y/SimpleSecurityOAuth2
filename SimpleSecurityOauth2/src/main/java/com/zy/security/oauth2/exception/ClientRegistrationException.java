package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:09:37;
 * @Description:
 */
@SuppressWarnings("serial")
public class ClientRegistrationException extends InvalidClientException {

	public ClientRegistrationException(String msg) {
		super(msg);
	}
	public ClientRegistrationException(String msg, Throwable t) {
		super(msg, t);
	}

}
