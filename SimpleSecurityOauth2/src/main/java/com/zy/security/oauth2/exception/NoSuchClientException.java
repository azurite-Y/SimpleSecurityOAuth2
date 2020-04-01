package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月29日 下午10:45:56;
 * @Description: 未找到相关的客户端信息引发的异常
 */
@SuppressWarnings("serial")
public class NoSuchClientException extends ClientRegistrationException {
	public NoSuchClientException(String msg) {
		super(msg);
	}
	public NoSuchClientException(String msg, Throwable t) {
		super(msg, t);
	}
}
