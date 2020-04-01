package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午10:54:21;
 * @Description: 用户拒绝授权异常
 */
@SuppressWarnings("serial")
public class UserDeniedAuthorizationException extends OAuth2Exception {
	
	public UserDeniedAuthorizationException(String msg) {
		super(msg);
	}
	public UserDeniedAuthorizationException(String msg, Throwable t) {
		super(msg, t);
	}

}
