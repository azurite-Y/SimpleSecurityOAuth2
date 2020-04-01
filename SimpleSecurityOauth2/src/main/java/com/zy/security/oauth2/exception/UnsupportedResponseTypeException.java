package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午5:42:35;
 * @Description: 不支持的ResponseType类型
 */
@SuppressWarnings("serial")
public class UnsupportedResponseTypeException extends OAuth2Exception {
	
	public UnsupportedResponseTypeException(String msg) {
	    super(msg);
	  }
	public UnsupportedResponseTypeException(String msg, Throwable t) {
		super(msg, t);
	}

}
