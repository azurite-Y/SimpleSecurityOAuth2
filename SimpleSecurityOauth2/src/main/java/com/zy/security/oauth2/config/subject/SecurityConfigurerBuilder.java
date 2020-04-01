
package com.zy.security.oauth2.config.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zy.reflect.SecurityRelectHandler;
import com.zy.reflect.analysis.repository.SecurityAnnotationRepositoryImpl;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.oauth2.config.AuthenticationServerFiltersConfig;
import com.zy.security.oauth2.config.AuthorizationServerConfigurerAdapter;
import com.zy.security.oauth2.config.ResourceServerConfigurerAdapter;
import com.zy.security.oauth2.config.ResourceServerFiltersConfig;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.config.AuthenticationManagerBuilder;
import com.zy.security.web.config.ForLoginConfiguration;
import com.zy.security.web.config.RememberMeConfiguration;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.config.subject.SecurityFilterChainProxy;
import com.zy.security.web.config.subject.WebSecurity;
import com.zy.security.web.config.subject.WebSecurityConfigurerAdapter;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;
import com.zy.utils.AnnotaionAttribute;

/**
 * @author zy
 * @Date 2019-12-03 周二 下午 14:07:27
 * @Description 负责创建{@link WebSecurityConfigurerAdapter}的子类，如果没有则直接创建WebSecurityConfigurerAdapter对象
 * @version
 */
public class SecurityConfigurerBuilder {
	private List<SecurityFilterChainProxy> filterChains = new ArrayList<>(3);
	
	private WebSecurityConfigurerAdapter webSecurityConfigurerAdapter;
	private ResourceServerConfigurerAdapter resourceServerConfigurerAdapter;
	private AuthorizationServerConfigurerAdapter authorizationServerConfigurerAdapter;
	// 传递clientDetailsService
	private ClientDetailsServiceConfigurer clientDetailsServiceConfigurer;
	// 传递用户名密码表单提交uri
	private HttpSecurity httpSecurity = new HttpSecurity();
	// 传递TokenStore
	private AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer;
	
	public void init() {
		SecurityRelectHandler handler = new SecurityRelectHandler();
		// 扫描根目录下的注解
		handler.invoke();
		SecurityAnnotationRepositoryImpl annotationRepository = handler.getAnnotationRepository();

		Map<String, List<Class<?>>> annoClz = annotationRepository.getAnnoClz();
		// 获得@EnableSecurityConfiguration标注的Class对象
		List<Class<?>> webList = annoClz.get(AnnotaionAttribute.enableSecurityConfiguration);
		// 获得@EnableAuthorizationServer标注的Class对象
		List<Class<?>> authList = annoClz.get(AnnotaionAttribute.EnableAuthorizationServer);
		// 获得@EnableResourceServer标注的Class对象
		List<Class<?>> resoList = annoClz.get(AnnotaionAttribute.EnableResourceServer);

		// -------------根据获得的相关注解Class集合创建对象----------------
		Class<?> configClz = null;
		if (webList == null || webList.isEmpty()) { // 默认配置环境
			this.webSecurityConfigurerAdapter = new WebSecurityConfigurerAdapter();
		} else {
			configClz = webList.get(0);
			try {
				this.webSecurityConfigurerAdapter = (WebSecurityConfigurerAdapter) configClz.getDeclaredConstructor()
						.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// 获得uri与role的映射关系
			Map<String, String[]> annoMethod = annotationRepository.getAnnoMethod();
			this.webSecurityConfigurerAdapter.setMap(annoMethod);
		}
		
		// 没有标注@EnableResourceServer、@EnableAuthorizationServer注解的配置类则不会启用Oauth2相关配置
		if(authList != null && !authList.isEmpty()) {
			configClz = authList.get(0);
			try {
				this.authorizationServerConfigurerAdapter =  (AuthorizationServerConfigurerAdapter) configClz.getDeclaredConstructor()
						.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		if(resoList != null && !resoList.isEmpty()) {
			configClz = resoList.get(0);
			try {
				this.resourceServerConfigurerAdapter = (ResourceServerConfigurerAdapter) configClz.getDeclaredConstructor()
						.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	public List<SecurityFilterChainProxy> config() {
		// anyRequest
		SecurityFilterChainProxy webSecurityConfigurerAdapterBuilder = webSecurityConfigurerAdapterBuilder();
		// 拦截oauth2请求
		SecurityFilterChainProxy authorizationServerConfigurerAdapterBuilder = AuthorizationServerConfigurerAdapterBuilder();
		// 拦截配置的请求
		SecurityFilterChainProxy resourceServerConfigurerAdapterBuilder = ResourceServerConfigurerAdapterBuilder();
		
		this.filterChains.add(authorizationServerConfigurerAdapterBuilder);
		this.filterChains.add(resourceServerConfigurerAdapterBuilder);
		this.filterChains.add(webSecurityConfigurerAdapterBuilder);
		
		return this.filterChains;
	}
	
	private SecurityFilterChainProxy webSecurityConfigurerAdapterBuilder() {
		WebSecurity webSecurity = new WebSecurity();
		AuthenticationManagerBuilder authBuilder = new AuthenticationManagerBuilder();
		httpSecurity.init();
		
		// 读取配置类配置
		webSecurityConfigurerAdapter.configureParent(authBuilder);
		webSecurityConfigurerAdapter.configureParent(httpSecurity);
		webSecurityConfigurerAdapter.configureParent(webSecurity);
		
		// 根据配置类配置创建AuthenticationManager对象
		AuthenticationManager authenticationManager = authBuilder.authenticationManagerBuilder(httpSecurity);
		httpSecurity.setAuthenticationManager(authenticationManager);
		
		SecurityFilterChainProxy config = httpSecurity.config(new AnyRequestMapping(RequestMatcher.anyRequest));
		return config;
	}

	private SecurityFilterChainProxy AuthorizationServerConfigurerAdapterBuilder() {
		SecurityFilterChainProxy builder = null;
		if(authorizationServerConfigurerAdapter != null) {
			AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer = new AuthorizationServerSecurityConfigurer();
			clientDetailsServiceConfigurer = new ClientDetailsServiceConfigurer();
			authorizationServerEndpointsConfigurer = new AuthorizationServerEndpointsConfigurer();
			
			try {
				authorizationServerConfigurerAdapter.configureParent(authorizationServerSecurityConfigurer);
				authorizationServerConfigurerAdapter.configureParent(clientDetailsServiceConfigurer);
				authorizationServerConfigurerAdapter.configureParent(authorizationServerEndpointsConfigurer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//创建基本的过滤器链
			HttpSecurity security = new HttpSecurity();
			security.init();
			security.anonymous().and().SessionManager();
			
			/*
			 * 避免SessionManagerConfiguration获取不到ForLoginConfiguration
			 * 授权服务器中的Authentication封装的是clientid和clientSecret，不需要记住我服务
			 */
			security.disableConfig(ForLoginConfiguration.class);
			
			SecurityFilterChainProxy securityFilterChainProxy = security.config(
					new AntRequestMapping(Oauth2Utils.oauth_authorize,null,true),
					new AntRequestMapping(Oauth2Utils.oauth_check_token,HttpMethod.GET),
					new AntRequestMapping(Oauth2Utils.oauth_confirm_access,HttpMethod.GET),
					new AntRequestMapping(Oauth2Utils.oauth_token,HttpMethod.POST) );
			
			// 将创建的授权服务器本身的过滤器添加到已有的过滤器链中
			AuthenticationServerFiltersConfig authenticationServerFiltersConfig = new AuthenticationServerFiltersConfig(authorizationServerEndpointsConfigurer
					, clientDetailsServiceConfigurer, authorizationServerSecurityConfigurer);
			builder = authenticationServerFiltersConfig.builder(securityFilterChainProxy,httpSecurity);
		}
		return builder;
	}

	private SecurityFilterChainProxy ResourceServerConfigurerAdapterBuilder() {
		SecurityFilterChainProxy builder = null;
		if(resourceServerConfigurerAdapter != null) {
			HttpSecurity security = new HttpSecurity();
			security.init();
			ResourceServerSecurityConfigurer resourceServerSecurityConfigurer = new ResourceServerSecurityConfigurer();
			
			try {
				resourceServerConfigurerAdapter.configureParent(security);
				resourceServerConfigurerAdapter.configureParent(resourceServerSecurityConfigurer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 避免SessionManagerConfiguration获取不到ForLoginConfiguration
			security.formLogin();
			
			/*
			 * 避免SessionManagerConfiguration获取不到ForLoginConfiguration
			 * 资源服务器中的Authentication封装的是clientid和clientSecret，不需要记住我服务
			 */
			security.disableConfig(ForLoginConfiguration.class,RememberMeConfiguration.class);
			
			
			// 若为配置匹配的uri则不匹配所有请求
			SecurityFilterChainProxy proxy = security.config(new AnyRequestMapping(RequestMatcher.notAnyRequest));
			
			// 将创建的资源服务器本身的过滤器添加到已有的过滤器链中
			ResourceServerFiltersConfig resourceServerFiltersConfig = new ResourceServerFiltersConfig(security,resourceServerSecurityConfigurer);
			builder = resourceServerFiltersConfig.config(proxy,clientDetailsServiceConfigurer,authorizationServerEndpointsConfigurer);
		}
		return builder;
	}
}
