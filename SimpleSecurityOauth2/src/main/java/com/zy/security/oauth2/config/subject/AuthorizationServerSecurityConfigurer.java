package com.zy.security.oauth2.config.subject;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import com.zy.security.core.authentication.compare.DefaultPasswordEncoder;
import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.utils.Assert;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:58:36;
 * @Description: 端节点uri定制与映射，
 */
public final class AuthorizationServerSecurityConfigurer {
	
	private List<Filter> tokenEndpointAuthenticationFilters  = new ArrayList<>();
	private PasswordEncoder passwordEncoder;
	private boolean allowOnlyPost = true;
	
	
	/**
	 * 密码加密与密码匹配接口，默认使用 DefaultPasswordEncoder
	 * @param passwordEncoder
	 * @return
	 */
	public AuthorizationServerSecurityConfigurer passwordEncoder (PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "提供的PasswordEncoder实现不能为null");
		this.passwordEncoder = passwordEncoder;
		return this;
	}
	
	/**
	 * clientId在请求参数中的参数名，默认使用 DefaultPasswordEncoder 实现类
	 * @param clientId
	 * @return
	 */
	public AuthorizationServerSecurityConfigurer clientIdParame (String clientId) {
		Assert.notNull(clientId, "clientId不能为null");
		Oauth2Utils.client_id = clientId;
		return this;
	}
	
	/**
	 * 提交clientId与ClientSecret的请求(默认：“/oauth/token”)是否限定为post请求</br>
	 * 默认为true，即只允许表单提交
	 * @param allowOnlyPost
	 * @return
	 */
	public AuthorizationServerSecurityConfigurer postOnly(boolean allowOnlyPost) {
		if (!allowOnlyPost) {
			this.allowOnlyPost = allowOnlyPost;
		}
		return this;
		
	}
	
	/**
	 * 令牌端点身份验证过滤器，为令牌终结点设置自定义身份验证筛选器的新列表。
	 * 所有过滤器均设置在 BasicAuthenticationFilter 的上游。
	 */
	public void tokenEndpointAuthenticationFilters(List<Filter> filters) {
		Assert.notNull(filters, "自定义身份验证过滤器列表不能为空");
		this.tokenEndpointAuthenticationFilters = new ArrayList<>(filters);
	}

	public PasswordEncoder getPasswordEncoderOrDefault() {
		if (passwordEncoder == null) {
			passwordEncoder = new DefaultPasswordEncoder();
		}
		return passwordEncoder;
	}
	
	//----------get、set--------------
	public List<Filter> getTokenEndpointAuthenticationFilters() {
		return tokenEndpointAuthenticationFilters;
	}
	public void setTokenEndpointAuthenticationFilters(List<Filter> tokenEndpointAuthenticationFilters) {
		this.tokenEndpointAuthenticationFilters = tokenEndpointAuthenticationFilters;
	}
	public boolean isAllowOnlyPost() {
		return allowOnlyPost;
	}
	public void setAllowOnlyPost(boolean allowOnlyPost) {
		this.allowOnlyPost = allowOnlyPost;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
