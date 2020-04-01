package com.zy.security.oauth2.interfaces;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.OAuth2Exception;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:32:51;
 * @Description:
 */
public interface Endpoint {
	
	/**
	 * 端节点处理逻辑
	 * @param request
	 * @return true为已处理，false为未处理或无法处理
	 */
	boolean endpoint(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
			throws OAuth2Exception, AuthenticationException ;
}
