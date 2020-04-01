package com.zy.security.oauth2.interfaces;

import com.zy.security.oauth2.exception.OAuth2Exception;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:17:59;
 * @Description: 用于确定用户代理的重定向URI的基本接口
 */
public interface RedirectResolver {
	/**
	 * 比对请求参数和客户端详细信息之中的重定向uri，返回二者交集中第一个uri
	 * @param requestedRedirect
	 * @param client
	 * @return
	 * @throws OAuth2Exception
	 */
	String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception;
}
