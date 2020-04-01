package com.zy.security.oauth2.config.subject;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.config.subject.IgnoringConfiguration;
import com.zy.security.web.config.subject.SecurityFilterChainProxy;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月13日 下午11:17:46;
 * @Description:  注册为Filter，对请求有所甄别的执行过滤器链(SecurityFilterChainProxy)
 */
@WebFilter(filterName="FilterChainEnhanceProxy",urlPatterns="/*")
public class FilterChainEnhanceProxy implements Filter {
	private Logger logger = LoggerFactory.getLogger(FilterChainEnhanceProxy.class);
	private List<SecurityFilterChainProxy> filterChains;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		SecurityConfigurerBuilder securityConfigurerBuilder = new SecurityConfigurerBuilder();
		securityConfigurerBuilder.init();
		this.filterChains = securityConfigurerBuilder.config();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		boolean isIngoring = isIgnoring(req);
		if(this.filterChains == null || filterChains.isEmpty() || isIngoring) {
			chain.doFilter(request, response);
			return ;
		}
		
		// 挑选合适的SecurityFilterChainProxy
		for (SecurityFilterChainProxy chainProxy : filterChains) {
			if (chainProxy.matchers(req)) {
				chainProxy.doFilter(request, response, chain);;
				return ;
			}
		}
	}

	/**
	 * 当前url是否是忽略的url
	 * @param requestURI
	 * @return
	 */
	private boolean isIgnoring(HttpServletRequest req) {
		for (RequestMatcher mapping : IgnoringConfiguration.ignoredRequests) {
			if(mapping.match(req)) {
				if(AuxiliaryTools.debug) {
					logger.info("忽略.请求：'{}'，类型：'{}'",req.getRequestURI(),req.getMethod());
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void destroy() {}
}
