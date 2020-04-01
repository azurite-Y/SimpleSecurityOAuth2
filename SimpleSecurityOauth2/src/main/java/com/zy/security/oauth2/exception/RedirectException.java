package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:26:16;
 * @Description: 重定向异常，包括重定向uri匹配与否，重定向uri使用范围限制
 */
@SuppressWarnings("serial")
public class RedirectException extends ClientAuthenticationException {
	public RedirectException(String msg) {
		super(msg);
	}
	public RedirectException(String msg, Throwable t) {
		super(msg, t);
	}
}
