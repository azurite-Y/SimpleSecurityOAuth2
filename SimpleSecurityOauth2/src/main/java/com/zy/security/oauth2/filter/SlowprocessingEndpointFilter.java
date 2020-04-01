package com.zy.security.oauth2.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.interfaces.Endpoint;
import com.zy.security.oauth2.interfaces.SecurityExceptionHandler;
import com.zy.security.oauth2.utils.SecurityHttpServletResponse;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:54:24;
 * @Description: 对端节点的慢处理过滤器
 */
public class SlowprocessingEndpointFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 标识
	private static final String FILTER_APPLIED = "slow_processing_endpoint_filter";
	private List<Endpoint> endpoints;
	private SecurityExceptionHandler exceptionHandler;
	// 存储自定义映射的RequestMatcher
	private List<RequestMatcher> matchers;
	private AuthenticationFailureHandler authenticationFailureHandler;
	public SlowprocessingEndpointFilter(List<Endpoint> endpoints) {
		super();
		this.endpoints = endpoints;
	}

	public SlowprocessingEndpointFilter(List<Endpoint> endpoints, SecurityExceptionHandler exceptionHandler) {
		super();
		this.endpoints = endpoints;
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * 若配置了端节点映射则放行请求到控制器中。
	 * 若未配置则交由本身的端节点处理类处理，有处理结果则直接返回。
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if (matchers != null && !matchers.isEmpty()) {
			for (RequestMatcher matcher : matchers) {
				if (matcher.match(req)) {
					chain.doFilter(request, response);
					return ;
				}
				break ;
			}
		}
		
		for (Endpoint endpoint : endpoints) {
			try {
				// 在有处理结果后中断循环
				if( endpoint.endpoint(req,resp,chain) ) {
					if (AuxiliaryTools.debug) {
						logger.info("{} 处理成功.",endpoint.getClass().getSimpleName());
					}
					return ;
				}
			} catch (OAuth2Exception e) {
				exceptionHandler.handler(req, resp, e);
				
				if (AuxiliaryTools.debug) {
					logger.info("{} 处理引发异常.",endpoint.getClass().getSimpleName());
				}
				return ;
				
			} catch (AuthenticationException e) {
				authenticationFailureHandler.onAuthenticationFailure(req, resp, e);
			
				if (AuxiliaryTools.debug) {
					logger.info("{} 处理引发异常.",endpoint.getClass().getSimpleName());
				}
				return ;
			}
			
		}
	}
	
	/**
	 * 釜底抽薪式过滤，若请求中有结果则放行，反之则尝试进行处理
	 * 此方案暂且搁置
	 */
	public void doFilterTest(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		//转换成代理类
		SecurityHttpServletResponse securityHttpServletResponse = new SecurityHttpServletResponse(resp);
		if(req.getAttribute(FILTER_APPLIED) == null) {
			req.setAttribute(FILTER_APPLIED, FILTER_APPLIED);
			// 此请求未经过此过滤器
			chain.doFilter(request, securityHttpServletResponse);
		}
		
		// 获取缓存的响应数据
		byte[] contentByte = securityHttpServletResponse.getBytes(); 
		// 无返回内容
		if( contentByte.length == 0 && req.getAttribute(FILTER_APPLIED) != null ) {
			for (Endpoint endpoint : endpoints) {
				try {
					// 在有处理结果后中断循环
					if( endpoint.endpoint(req,securityHttpServletResponse,chain) ) {
						return ;
					}
				} catch (OAuth2Exception e) {
					exceptionHandler.handler(req, securityHttpServletResponse, e);
					return ;
				} catch (AuthenticationException e) {
					exceptionHandler.handler(req, securityHttpServletResponse, e);
					return ;
				}
			}
		} else {
//			securityHttpServletResponse.setStatus(200);
			//把返回值输出到客户端
			ServletOutputStream out = resp.getOutputStream();
			out.write(contentByte);
			out.flush();
		}
	}

	
	public List<Endpoint> getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}
	public SecurityExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	public void setExceptionHandler(SecurityExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	public List<RequestMatcher> getMatchers() {
		return matchers;
	}
	public void setMatchers(List<RequestMatcher> matchers) {
		this.matchers = matchers;
	}
	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}
	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	
	@Override
	public void destroy() {
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
