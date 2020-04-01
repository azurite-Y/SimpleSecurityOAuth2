package com.zy.security.oauth2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: zy;
 * @DateTime: 2020年3月15日 上午12:22:11;
 * @Description: basic认证实现（未使用）
 */
public class BasicAuthenticationFilter implements Filter {
	
	private String authenticationHeader;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String header = req.getHeader(this.authenticationHeader);
		
		if (header != null && header.toLowerCase().startsWith("basic ")) {
			
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
