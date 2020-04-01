package com.zy.security.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;

/**
 * @author: zy;
 * @DateTime: 2020年3月27日 下午6:12:35;
 * @Description: 认证成功或不做处理
 */
public class NullAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {

	}

}
