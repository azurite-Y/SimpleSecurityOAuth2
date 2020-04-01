
package com.zy.security.web.config.subject;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AuxiliaryTools;

/**
* @author zy
* @Date 2019-11-27 周三 下午 15:18:39
* @Update 2020年3月13日23:12:37
* @Description 过滤器链对象
* @version 
*/
public class SecurityFilterChainProxy implements Filter {
	private Logger logger = LoggerFactory.getLogger(SecurityFilterChainProxy.class);
	
	// 过滤器容器
	private List<Filter> filters;
	// 过滤器链既定的可处理uri
	private List<RequestMatcher> matchers;
	private VirtualFilterChain vfc;
	
	public SecurityFilterChainProxy(List<Filter> filters, List<RequestMatcher> matchers) {
		super();
		this.filters = filters;
		this.matchers = matchers;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		if(AuxiliaryTools.debug) {
			logger.info("当前请求：'{}'，类型：'{}'",req.getRequestURI(),req.getMethod());
		}
		
		if(this.filters == null || filters.isEmpty()) {
			chain.doFilter(request, response);
		}else {
			if(vfc == null) {
				vfc = new VirtualFilterChain(chain, filters);
			}
			vfc.doFilter(request, response);
			// 在过滤器链执行完毕或返回时重置当前执行过滤器标记
			vfc.currentPosition = 0;
		}
	}
	
	/**
	 * 甄别当前请求是否适合进入过滤器链
	 * @param request
	 * @return
	 */
	public boolean matchers(HttpServletRequest request) {
		for (RequestMatcher matcher : matchers) {
			if (matcher.match(request)) {
				return true;
			}
		}
		return false;
	}
	

	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	public List<RequestMatcher> getMatchers() {
		return matchers;
	}
	public void setMatchers(List<RequestMatcher> matchers) {
		this.matchers = matchers;
	}
	
	// 实现过滤器链逻辑
	private static class VirtualFilterChain implements FilterChain {
		// 当前过滤器的索引标识
		private int currentPosition = 0;
		// 真正的FilterChain
		private final FilterChain chain;
		private final List<Filter> filters;
		private final int size;
		
		public VirtualFilterChain(FilterChain chain, List<Filter> filter) {
			super();
			this.chain = chain;
			this.filters = filter;
			this.size = filter.size();
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			if (currentPosition == size) { // 过滤器链已执行完毕，放行请求到下一个过滤器
				this.chain.doFilter(request, response);
				return;
			}
			Filter nextFilter = this.filters.get(currentPosition++);
			// 按梯度输出使用到的过滤器
			if(AuxiliaryTools.debug) {
				System.out.println(this.currentPosition+"--"+nextFilter.getClass().getSimpleName());
			}
			nextFilter.doFilter(request, response, this);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	@Override
	public void destroy() {}
}
