package com.zy.security.oauth2.config;

import com.zy.security.oauth2.config.subject.AuthorizationServerEndpointsConfigurer;
import com.zy.security.oauth2.config.subject.AuthorizationServerSecurityConfigurer;
import com.zy.security.oauth2.config.subject.ClientDetailsServiceConfigurer;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:51:36;
 * @Description: 授权服务器配置类，覆写此类方法实现用户的自定义配置
 */
public class AuthorizationServerConfigurerAdapter {
	
	public final void configureParent(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security
		this.configure(security);
	}

	public final void configureParent(ClientDetailsServiceConfigurer clients) throws Exception {
		this.configure(clients);
	}

	public final void configureParent(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		this.configure(endpoints);
	}
	
	// ------
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	}

	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	}

	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	}
}
