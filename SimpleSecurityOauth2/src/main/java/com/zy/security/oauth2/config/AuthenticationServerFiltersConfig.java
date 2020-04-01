package com.zy.security.oauth2.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;

import com.zy.security.core.authentication.DaoAuthenticationProvider;
import com.zy.security.core.authentication.DefaultUserDetailsChecker;
import com.zy.security.core.authentication.ProviderManager;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.core.userdetails.NotUserCache;
import com.zy.security.oauth2.authentication.ClientDetailsUserDetailsService;
import com.zy.security.oauth2.config.subject.AuthorizationServerEndpointsConfigurer;
import com.zy.security.oauth2.config.subject.AuthorizationServerSecurityConfigurer;
import com.zy.security.oauth2.config.subject.ClientDetailsServiceConfigurer;
import com.zy.security.oauth2.config.subject.Oauth2FilterComparator;
import com.zy.security.oauth2.endpoint.AuthorizationEndpoint;
import com.zy.security.oauth2.endpoint.CheckTokenEndpoint;
import com.zy.security.oauth2.endpoint.DefaultClientDetailsResolver;
import com.zy.security.oauth2.endpoint.TokenEndpoint;
import com.zy.security.oauth2.endpoint.WhitelabelApprovalEndpoint;
import com.zy.security.oauth2.endpoint.WhitelabelErrorSecurityExceptionHandler;
import com.zy.security.oauth2.filter.ClientCredentialsTokenEndpointFilter;
import com.zy.security.oauth2.filter.SlowprocessingEndpointFilter;
import com.zy.security.oauth2.interfaces.AccessTokenConverter;
import com.zy.security.oauth2.interfaces.AuthorizationCodeServices;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.interfaces.Endpoint;
import com.zy.security.oauth2.interfaces.RedirectResolver;
import com.zy.security.oauth2.interfaces.SecurityExceptionHandler;
import com.zy.security.oauth2.interfaces.TokenGranter;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.oauth2.interfaces.UserApprovalHandler;
import com.zy.security.oauth2.token.AuthorizationCodeTokenGranter;
import com.zy.security.oauth2.token.ClientCredentialsTokenGranter;
import com.zy.security.oauth2.token.CompositeTokenGranter;
import com.zy.security.oauth2.token.DefaultResourceServerTokenServices;
import com.zy.security.oauth2.token.ImplicitTokenGranter;
import com.zy.security.oauth2.token.PasswordTokenGranter;
import com.zy.security.oauth2.token.RefreshTokenGranter;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.config.ForLoginConfiguration;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.config.subject.SecurityFilterChainProxy;
import com.zy.security.web.filter.AbstractAuthenticationProcessingFilter;
import com.zy.security.web.filter.SecurityContextPersistenceFilter;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.HttpMethod;

/**
 * @author: zy;
 * @DateTime: 2020年3月25日 下午10:29:49;
 * @Description: 授权服务器专有过滤器链创建
 */
public class AuthenticationServerFiltersConfig {
	private AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer;
	private ClientDetailsServiceConfigurer clientDetailsServiceConfigurer;
	private AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer;
	private PasswordEncoder passwordEncoder;
	private ClientDetailsService clientDetailsService;
	/**
	 * 将创建的过滤器添加到已有的过滤器链中</br>
	 * 1.ClientCredentialsTokenEndpointFilter</br>
	 * 2.SlowprocessingEndpointFilter
	 * @param proxy
	 * @param httpSecurity 
	 * @return
	 */
	public SecurityFilterChainProxy builder(SecurityFilterChainProxy proxy, HttpSecurity httpSecurity) {
		Oauth2FilterComparator comparator = new Oauth2FilterComparator();
		List<Filter> filters = proxy.getFilters();
		List<RequestMatcher> matchers = proxy.getMatchers();
		
		Map<String, String> patternMap = authorizationServerEndpointsConfigurer.getPatternMap();
		List<RequestMatcher> mappings = null;
		if (patternMap != null && !patternMap.isEmpty()) {
			mappings = new ArrayList<>();
			Set<String> keySet = patternMap.keySet();
			for (String uri : keySet) {
				AntRequestMapping antRequestMapping = new AntRequestMapping(uri, null, true);
				mappings.add(antRequestMapping);
				matchers.add(antRequestMapping);
			}
		}
		
		List<Filter> tokenEndpointAuthenticationFilters = authorizationServerSecurityConfigurer.getTokenEndpointAuthenticationFilters();
		// 额外提供的过滤器注册到过滤器比较器中
		for (Filter filter : tokenEndpointAuthenticationFilters) {
			comparator.registerBefore(filter.getClass(), SecurityContextPersistenceFilter.class);
		}
		
		int accessTokenValiditySeconds = authorizationServerEndpointsConfigurer.getAccessTokenValiditySecondsOrDefault();
		int refreshTokenValiditySeconds = authorizationServerEndpointsConfigurer.getRefreshTokenValiditySecondsOrDefault();
		// ClientDetailsService初始化
		clientDetailsService = clientDetailsServiceConfigurer.builder(accessTokenValiditySeconds,refreshTokenValiditySeconds);
		
		ClientDetailsUserDetailsService clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService);
		boolean allowOnlyPost = authorizationServerSecurityConfigurer.isAllowOnlyPost();
		passwordEncoder = authorizationServerSecurityConfigurer.getPasswordEncoderOrDefault();
		
		AuthenticationManager createProviderManager = this.createProviderManager(clientDetailsUserDetailsService);
		AbstractAuthenticationProcessingFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter(Oauth2Utils.oauth_token, allowOnlyPost);
		clientCredentialsTokenEndpointFilter.setAuthenticationManager(createProviderManager);
//		clientCredentialsTokenEndpointFilter.setRememberMeServices(new NulltRememberMeService());
//		clientCredentialsTokenEndpointFilter.setSuccessHandler(new NullAuthenticationSuccessHandler());
		// 设置为true，则在认证通过后继续执行过滤器链
		clientCredentialsTokenEndpointFilter.setContinueChainBeforeSuccessfulAuthentication(true);
		
		SecurityExceptionHandler exceptionHandler = new WhitelabelErrorSecurityExceptionHandler();
		List<Endpoint> createEndpoint = createEndpoint();
		
		SlowprocessingEndpointFilter slowprocessingEndpointFilter = 
				new SlowprocessingEndpointFilter(createEndpoint, exceptionHandler);
		slowprocessingEndpointFilter.setMatchers(mappings);
		
		ForLoginConfiguration config = (ForLoginConfiguration) httpSecurity.getConfig(ForLoginConfiguration.class);
		AuthenticationFailureHandler authenticationFailureHandler = config.getAuthenticationFailureHandler();
		slowprocessingEndpointFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		
		// 读取Security配置中的username和password参数名
		Oauth2Utils.usernameParameter = config.getUsernameParameter();
		Oauth2Utils.passwordParameter = config.getPasswordParameter();
		
		filters.add(clientCredentialsTokenEndpointFilter);
		filters.add(slowprocessingEndpointFilter);
		filters.addAll(tokenEndpointAuthenticationFilters);
		
		// 过滤器排序
		Collections.sort(filters, comparator);
		
		return proxy;
	}

	private List<Endpoint> createEndpoint(){
		List<Endpoint> endpointList  = new ArrayList<>();
		
		UserApprovalHandler userApprovalHandler = authorizationServerEndpointsConfigurer.getUserApprovalHandlerOrDefault();
		AuthorizationCodeServices authorizationCodeServices = authorizationServerEndpointsConfigurer.getAuthorizationCodeServicesOrDefault();
		AccessTokenConverter accessTokenConverter = authorizationServerEndpointsConfigurer.getAccessTokenConverterOrDefault();
		RedirectResolver redirectResolver = authorizationServerEndpointsConfigurer.getRedirectResolverOrDefault();
		
		ClientDetailsResolver clientDetailsResolver = authorizationServerEndpointsConfigurer.getClientDetailsResolver();
		if (clientDetailsResolver == null) {
			clientDetailsResolver = new DefaultClientDetailsResolver(passwordEncoder);
		}
		
		TokenStore tokenStore = authorizationServerEndpointsConfigurer.getTokenStoreOrDefault();
		DefaultResourceServerTokenServices resourceServerTokenServices = new DefaultResourceServerTokenServices(tokenStore);
		
		TokenGranter tokenGranter = authorizationServerEndpointsConfigurer.getTokenGranter();
		if( tokenGranter == null ) {
			CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter();
			List<TokenGranter> createTokenGranter = createTokenGranter();
			compositeTokenGranter.setTokenGranters(createTokenGranter);
			tokenGranter = compositeTokenGranter;
		}
		
		AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint(Oauth2Utils.oauth_authorize);
		authorizationEndpoint.setUserApprovalHandler(userApprovalHandler);
		authorizationEndpoint.setAuthorizationCodeServices(authorizationCodeServices);
		authorizationEndpoint.setClientDetailsResolver(clientDetailsResolver);
		authorizationEndpoint.setClientDetailsService(clientDetailsService);
		authorizationEndpoint.setRedirectResolver(redirectResolver);
		authorizationEndpoint.setTokenGranter(tokenGranter);
		endpointList.add(authorizationEndpoint);
		
		CheckTokenEndpoint checkTokenEndpoint = new CheckTokenEndpoint(
				null, new AntRequestMapping(Oauth2Utils.oauth_check_token, HttpMethod.GET));
		checkTokenEndpoint.setAccessTokenConverter(accessTokenConverter);
		checkTokenEndpoint.setResourceServerTokenServices(resourceServerTokenServices);
		endpointList.add(checkTokenEndpoint);
		
		TokenEndpoint tokenEndpoint = new TokenEndpoint(
				new AntRequestMapping(Oauth2Utils.oauth_token, HttpMethod.POST), null);
		tokenEndpoint.setTokenGranter(tokenGranter);
		tokenEndpoint.setClientDetailsService(clientDetailsService);
		tokenEndpoint.setClientDetailsResolver(clientDetailsResolver);
		endpointList.add(tokenEndpoint);
		
		WhitelabelApprovalEndpoint whitelabelApprovalEndpoint = new WhitelabelApprovalEndpoint(
				null,new AntRequestMapping(Oauth2Utils.oauth_confirm_access, HttpMethod.GET));
		endpointList.add(whitelabelApprovalEndpoint);
		
		return endpointList;
	}
	
	private List<TokenGranter> createTokenGranter(){
		List<TokenGranter> tokenGranterList  = new ArrayList<>();
		
		AuthorizationCodeServices authorizationCodeServices = authorizationServerEndpointsConfigurer.getAuthorizationCodeServicesOrDefault();
		AuthorizationServerTokenServices tokenServices = authorizationServerEndpointsConfigurer.getTokenServicesOrDefault();
		boolean reuseRefreshToken = authorizationServerEndpointsConfigurer.isReuseRefreshToken();
		
		
		//---
		AuthorizationCodeTokenGranter authorizationCodeTokenGranter = new AuthorizationCodeTokenGranter(tokenServices);
		authorizationCodeTokenGranter.setAuthorizationCodeServices(authorizationCodeServices);
		tokenGranterList.add(authorizationCodeTokenGranter);
		
		//---
		ClientCredentialsTokenGranter clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(tokenServices);
		clientCredentialsTokenGranter.setRefreshTokenEnable(reuseRefreshToken);
		tokenGranterList.add(clientCredentialsTokenGranter);
		
		//---
		ImplicitTokenGranter implicitTokenGranter = new ImplicitTokenGranter(tokenServices);
		tokenGranterList.add(implicitTokenGranter);
		
		//---
		PasswordTokenGranter passwordTokenGranter = new PasswordTokenGranter(tokenServices);
		tokenGranterList.add(passwordTokenGranter);
		// 若未自定义配置则为null
		UserDetailsService customUserDetailsService = authorizationServerEndpointsConfigurer.getUserDetailsService();
		AuthenticationManager createProviderManager = createProviderManager(customUserDetailsService);
		passwordTokenGranter.setAuthenticationManager(createProviderManager);
		
		//---
		RefreshTokenGranter eefreshTokenGranter = new RefreshTokenGranter(tokenServices);
		tokenGranterList.add(eefreshTokenGranter);
		
		return tokenGranterList;
	}
	
	private AuthenticationManager createProviderManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setChecker(new DefaultUserDetailsChecker());
		daoAuthenticationProvider.setUserCache(new NotUserCache());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		
		List<AuthenticationProvider> authProviderList = new ArrayList<>();
		authProviderList.add(daoAuthenticationProvider);
		
		ProviderManager providerManager = new ProviderManager();
		providerManager.setAuthProviderLst(authProviderList);
		// 清除密码
		providerManager.setEraseCredentialsAfterAuthentication(true);
		return providerManager;
	}
	
	//-----------------get、set、构造器-----------------------
	public AuthenticationServerFiltersConfig(
			AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer,
			ClientDetailsServiceConfigurer clientDetailsServiceConfigurer,
			AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) {
		super();
		this.authorizationServerEndpointsConfigurer = authorizationServerEndpointsConfigurer;
		this.clientDetailsServiceConfigurer = clientDetailsServiceConfigurer;
		this.authorizationServerSecurityConfigurer = authorizationServerSecurityConfigurer;
	}
	public AuthorizationServerEndpointsConfigurer getAuthorizationServerEndpointsConfigurer() {
		return authorizationServerEndpointsConfigurer;
	}
	public void setAuthorizationServerEndpointsConfigurer(
			AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) {
		this.authorizationServerEndpointsConfigurer = authorizationServerEndpointsConfigurer;
	}
	public ClientDetailsServiceConfigurer getClientDetailsServiceConfigurer() {
		return clientDetailsServiceConfigurer;
	}
	public void setClientDetailsServiceConfigurer(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) {
		this.clientDetailsServiceConfigurer = clientDetailsServiceConfigurer;
	}
	public AuthorizationServerSecurityConfigurer getAuthorizationServerSecurityConfigurer() {
		return authorizationServerSecurityConfigurer;
	}
	public void setAuthorizationServerSecurityConfigurer(
			AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) {
		this.authorizationServerSecurityConfigurer = authorizationServerSecurityConfigurer;
	}
}
