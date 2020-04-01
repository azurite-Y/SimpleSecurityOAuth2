package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午5:39:32;
 * @Description: oauth2异常顶级父类
 */
@SuppressWarnings("serial")
public class OAuth2Exception extends RuntimeException {
	public OAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public OAuth2Exception(String msg) {
		super(msg);
	}
}
