package com.zy.security.oauth2.config;

import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;

import com.zy.security.oauth2.authentication.OAuth2AuthenticationManager;
import com.zy.security.oauth2.config.subject.AuthorizationServerEndpointsConfigurer;
import com.zy.security.oauth2.config.subject.ClientDetailsServiceConfigurer;
import com.zy.security.oauth2.config.subject.Oauth2FilterComparator;
import com.zy.security.oauth2.config.subject.ResourceServerSecurityConfigurer;
import com.zy.security.oauth2.endpoint.WhitelabelErrorSecurityExceptionHandler;
import com.zy.security.oauth2.filter.OAuth2AuthenticationProcessingFilter;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.interfaces.ResourceServerTokenServices;
import com.zy.security.oauth2.interfaces.TokenExtractor;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.config.subject.SecurityFilterChainProxy;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:43:27;
 * @Description: 资源服务器专有过滤器链创建
 */
public class ResourceServerFiltersConfig {
	private HttpSecurity http;
	private ResourceServerSecurityConfigurer resourceServerSecurityConfigurer;
	
	/**
	 * 将创建的过滤器添加到已有的过滤器链中</br>
	 * 1.OAuth2AuthenticationProcessingFilter
	 * @param proxy
	 * @param clientDetailsServiceConfigurer - 从中获得授权服务器配置类中的客户端信息数据源
	 * @return
	 */
	public SecurityFilterChainProxy config(SecurityFilterChainProxy proxy,ClientDetailsServiceConfigurer clientDetailsServiceConfigurer,AuthorizationServerEndpointsConfigurer authServer) {
		Oauth2FilterComparator comparator = new Oauth2FilterComparator();
		List<Filter> filters = proxy.getFilters();
		
		TokenExtractor tokenExtractor = resourceServerSecurityConfigurer.getTokenExtractorOrDefault();
		String resourceId = resourceServerSecurityConfigurer.getResourceIdOrDefault();
		ClientDetailsResolver clientDetailsResolver = resourceServerSecurityConfigurer.getClientDetailsResolverOrDefault();
		
		ResourceServerTokenServices tokenServices  = resourceServerSecurityConfigurer.getTokenServices();;
		TokenStore tokenStore = null;
		if (authServer != null) { // 授权服务器和资源服务器在一起
			tokenStore = authServer.getTokenStoreOrDefault();
			tokenServices.setTokenStore(tokenStore);
		} else {
			TokenStore tokenStoreOrDefault = resourceServerSecurityConfigurer.getTokenStoreOrDefault();
			tokenServices.setTokenStore(tokenStoreOrDefault);
		}
		
		ClientDetailsService clientDetailsService = null;
		if (clientDetailsServiceConfigurer != null) { // 授权服务器和资源服务器在一起
			clientDetailsService = clientDetailsServiceConfigurer.getClientDetailsService();
		} else {
			clientDetailsService = resourceServerSecurityConfigurer.getClientDetailsServiceOrDefault();
		}
		
		WhitelabelErrorSecurityExceptionHandler whitelabelErrorSecurityExceptionHandler = new WhitelabelErrorSecurityExceptionHandler();
		
		OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
		oAuth2AuthenticationManager.setClientDetailsResolver(clientDetailsResolver);
		oAuth2AuthenticationManager.setClientDetailsService(clientDetailsService);
		oAuth2AuthenticationManager.setResourceId(resourceId);
		oAuth2AuthenticationManager.setTokenServices(tokenServices);
		
		OAuth2AuthenticationProcessingFilter oauth2Filter = new OAuth2AuthenticationProcessingFilter();
		oauth2Filter.setTokenExtractor(tokenExtractor);
		oauth2Filter.setExceptionHandler(whitelabelErrorSecurityExceptionHandler);
		oauth2Filter.setAuthenticationManager(oAuth2AuthenticationManager);
		
		filters.add(oauth2Filter);
		
		// 过滤器排序
		Collections.sort(filters, comparator);
		
		return proxy;
	}
	
	
	//-----------------get、set、构造器-----------------------
	public ResourceServerFiltersConfig(HttpSecurity http,
			ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) {
		super();
		this.http = http;
		this.resourceServerSecurityConfigurer = resourceServerSecurityConfigurer;
	}
	public ResourceServerFiltersConfig() {
		super();
	}
	public HttpSecurity getHttp() {
		return http;
	}
	public void setHttp(HttpSecurity http) {
		this.http = http;
	}
	public ResourceServerSecurityConfigurer getResourceServerSecurityConfigurer() {
		return resourceServerSecurityConfigurer;
	}
	public void setResourceServerSecurityConfigurer(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) {
		this.resourceServerSecurityConfigurer = resourceServerSecurityConfigurer;
	}
}
