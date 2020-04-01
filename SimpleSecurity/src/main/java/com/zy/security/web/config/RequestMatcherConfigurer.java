package com.zy.security.web.config;

import java.util.List;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;
import com.zy.utils.Assert;

/**
 * @author: zy;
 * @DateTime: 2020年3月25日 下午6:35:11;
 * @Description:
 */
public class RequestMatcherConfigurer extends AbstractHttpConfigurer<HttpSecurity> {
	// 存储当前过滤器链处理的请求
	private List<RequestMatcher> requestMatchers;
	
	public RequestMatcherConfigurer(HttpSecurity securityBuilder) {
		super(securityBuilder);
		this.requestMatchers = securityBuilder.getRequestMatchers();
	}

	/**
	 * 设置过滤器链所处理的URI，默认GET请求
	 * @param antPatterns
	 * @return
	 */
	public RequestMatcherConfigurer antMatchers(String... antPatterns) {
		for (String uri : antPatterns) {
			Assert.notNull(uri, "无效的URI，by："+uri);
			this.requestMatchers.add(
					new AntRequestMapping(uri, HttpMethod.GET) );
		}
		return this;
	}
	
	/**
	 * 设置过滤器链所处理的URI
	 * @param method
	 * @param antPatterns
	 * @return
	 */
	public RequestMatcherConfigurer antMatchers(HttpMethod method, String... antPatterns) {
		for (String uri : antPatterns) {
			Assert.notNull(uri, "无效的URI，by："+uri);
			this.requestMatchers.add(
					new AntRequestMapping(uri, method) );
		}
		return this;
	}
	
	/**
	 * 设置过滤器链处理所有请求
	 * @return
	 */
	public HttpSecurity anyRequest() {
			this.requestMatchers.add(new AnyRequestMapping(RequestMatcher.anyRequest) );
		return super.http;
	}
	
	@Override
	public void config() {
		super.http.setRequestMatchers(requestMatchers);
	}
}
