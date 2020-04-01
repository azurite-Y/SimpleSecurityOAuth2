package com.zy.security.oauth2.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午11:06:52;
 * @Description: oauth2异常处理
 */
public interface SecurityExceptionHandler {
	void handler (HttpServletRequest req , HttpServletResponse resp , Exception e);
}
