package com.zy.security.oauth2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.AbstractAuthentication;
import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.details.OAuth2AuthenticationDetails;
import com.zy.security.oauth2.interfaces.SecurityExceptionHandler;
import com.zy.security.oauth2.interfaces.TokenExtractor;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午11:36:26;
 * @Description: 处理所有经过此过滤器的请求，处理过后一律放行到下一个过滤器
 */
public class OAuth2AuthenticationProcessingFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private SecurityExceptionHandler exceptionHandler;
	// OAuth2AuthenticationManager
	private AuthenticationManager authenticationManager;
	private TokenExtractor tokenExtractor;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		try {
			Authentication authentication = tokenExtractor.extract(req);

			if (authentication == null) {
				if (AuxiliaryTools.debug) {
					logger.info("请求携带的token无效，token：{}", authentication);
				}
			} else {
				// 在此填充details属性，避免认证过的Authentication的details属性为null
				if (authentication instanceof AbstractAuthentication) {
					OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails(req);
					AbstractAuthentication needsDetails = (AbstractAuthentication) authentication;
					needsDetails.setDetails(oAuth2AuthenticationDetails);
				}
				Authentication authResult = authenticationManager.authenticate(authentication);

				if (AuxiliaryTools.debug) {
					logger.info("身份验证成功: " + authResult);
				}

				SecurityContextStrategy.getContext().setAuthentication(authResult);
			}
		} catch (AuthenticationException failed) {
			if (AuxiliaryTools.debug) {
				logger.info(failed.getMessage());
			}
			exceptionHandler.handler(req, resp, failed);
			return;
		}
		chain.doFilter(request, response);
	}

	
	public SecurityExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	public void setExceptionHandler(SecurityExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	public TokenExtractor getTokenExtractor() {
		return tokenExtractor;
	}
	public void setTokenExtractor(TokenExtractor tokenExtractor) {
		this.tokenExtractor = tokenExtractor;
	}
	@Override
	public void destroy() {
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
