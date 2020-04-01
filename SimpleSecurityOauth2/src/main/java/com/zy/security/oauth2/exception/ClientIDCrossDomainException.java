package com.zy.security.oauth2.exception;
/**
 * @author: zy;
 * @DateTime: 2020年3月31日 下午6:58:37;
 * @Description: 客户端id跨域使用所引发的异常。
 * 比如简单模式下请求提供的客户端id获取到的ClientDetails的grantType是authorization_code
 */
@SuppressWarnings("serial")
public class ClientIDCrossDomainException extends InvalidClientException {
	public ClientIDCrossDomainException(String msg) {
		super(msg);
	}
	public ClientIDCrossDomainException(String msg, Throwable t) {
		super(msg, t);
	}
}
