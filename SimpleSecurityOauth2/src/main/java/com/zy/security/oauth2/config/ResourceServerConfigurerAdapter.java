package com.zy.security.oauth2.config;

import com.zy.security.oauth2.config.subject.ResourceServerSecurityConfigurer;
import com.zy.security.web.config.subject.HttpSecurity;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:49:46;
 * @Description: 资源服务器配置类，覆写此类方法实现用户的自定义配置
 */
public class ResourceServerConfigurerAdapter {
	public final void configureParent(ResourceServerSecurityConfigurer resources) throws Exception {
		configure(resources);
	}

	public final void configureParent(HttpSecurity http) throws Exception {
		http.anonymous().and().SessionManager();
		configure(http);
	}
	
	// ------
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception  {
	}

	public void configure(HttpSecurity http) throws Exception {
	}
}
