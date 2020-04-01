package com.zy.security.oauth2.interfaces;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.token.Authentication;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午2:02:20;
 * @Description: 令牌提取器
 */
public interface TokenExtractor {
	/**
	 * 从未经身份验证的传入请求中提取令牌值
	 * @param request
	 * @return
	 */
	Authentication extract(HttpServletRequest request);
}
