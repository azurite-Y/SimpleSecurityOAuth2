
package com.zy.security.core.authentication.exception;
/**
* @author zy
* @Date 2019-11-13 周三 下午 10:12:08
* @Description 凭证错误异常
* @version 
*/
@SuppressWarnings("serial")
public class BadCredentialsException extends AuthenticationException {
	public BadCredentialsException() {}
	public BadCredentialsException(String msg) {
		super(msg);
	}
	public BadCredentialsException(String msg,Throwable t) {
		super(msg,t);
	}
}
