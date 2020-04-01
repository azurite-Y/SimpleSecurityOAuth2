package com.zy.security.oauth2.config.subject;

import com.zy.security.core.authentication.compare.DefaultPasswordEncoder;
import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.oauth2.authentication.BearerTokenExtractor;
import com.zy.security.oauth2.endpoint.DefaultClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.interfaces.ResourceServerTokenServices;
import com.zy.security.oauth2.interfaces.TokenExtractor;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.oauth2.store.InMemoryClientDetailsService;
import com.zy.security.oauth2.store.InMemoryTokenStore;
import com.zy.security.oauth2.token.DefaultResourceServerTokenServices;
import com.zy.utils.Assert;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午5:00:07;
 * @Description:
 */
public final class ResourceServerSecurityConfigurer {
	private String resourceId = "oauth2";
	private TokenStore tokenStore;
	private ResourceServerTokenServices tokenServices;
	private TokenExtractor tokenExtractor;
	private PasswordEncoder passwordEncoder;
	private ClientDetailsResolver clientDetailsResolver;
	private ClientDetailsService clientDetailsService;
	
	public ResourceServerSecurityConfigurer() {}
	
	/**
	 * 密码加密与密码匹配接口，默认使用 DefaultPasswordEncoder
	 * @param passwordEncoder
	 * @return
	 */
	public ResourceServerSecurityConfigurer passwordEncoder (PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "提供的PasswordEncoder实现不能为null");
		this.passwordEncoder = passwordEncoder;
		return this;
	}
	
	/**
	 * 客户端信息验证程序，验证client_id、client_secret、scope、GrantType
	 * @param clientDetailsResolver
	 * @return
	 */
	public ResourceServerSecurityConfigurer clientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
		return this;
	}
	
	/**
	 * 客户端详细信息服务，默认使用InMemoryClientDetailsService实现</br>
	 * 若在一个Web容器中同时启用授权服务器和资源服务器，那么此配置项会被忽略，转而授权服务器提供ClientDetailsService实现
	 * @param clientDetailsService
	 * @return
	 */
	public ResourceServerSecurityConfigurer clientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
		return this;
	}
	
	/**
	 * 配置此资源服务器的ResourcesId
	 * @param resourceId
	 * @return
	 */
	public ResourceServerSecurityConfigurer resourceId(String resourceId) {
		Assert.notNull(resourceId, "资源ID无效，by："+resourceId);
		this.resourceId = resourceId;
		return this;
	}
	
	/**
	 * 令牌提取器，默认使用 BearerTokenExtractor 对象，从请求头和请求参数中提取accessToken
	 * @param tokenExtractor
	 * @return
	 */
	public ResourceServerSecurityConfigurer tokenExtractor(TokenExtractor tokenExtractor) {
		Assert.notNull (tokenExtractor, "令牌提取器不能为null");
		this.tokenExtractor = tokenExtractor;
		return this;
	}
	
	/**
	 * 令牌服务，默认使用 DefaultTokenServices实现.</br>
	 * 若同时启用授权服务器和资源服务器则采用授权服务器的TokenStore配置创建ResourceServerTokenServices实现。</br>
	 * 而如果人为配置了此项实现，那么将使用setTokenStore(TokenStore tokenStore)传递授权服务器所提供的TokenStore实现
	 * @param tokenServices
	 * @return
	 */
	public ResourceServerSecurityConfigurer tokenServices(ResourceServerTokenServices tokenServices) {
		Assert.notNull(tokenServices, "ResourceServerTokenServices不能为null");
		this.tokenServices = tokenServices;
		return this;
	}
	
	/**
	 * 令牌存储库，默认使用 InMemoryTokenStore() 对象.</br>
	 * 若同时启用授权服务器和资源服务器则此项配置会被忽略，而采用授权服务器的TokenStore配置
	 * @param tokenStore
	 * @return
	 */
	public ResourceServerSecurityConfigurer tokenStore(TokenStore tokenStore) {
		Assert.notNull(tokenStore, "TokenStore不能为null");
		this.tokenStore = tokenStore;
		return this;
	}
	
	public TokenStore getTokenStoreOrDefault() {
		if(tokenStore == null) {
			tokenStore = new InMemoryTokenStore();
		}
		return tokenStore;
	}
	
	public TokenExtractor getTokenExtractorOrDefault() {
		if(tokenExtractor == null) {
			tokenExtractor = new BearerTokenExtractor();
		}
		return tokenExtractor;
	}
	
	public ResourceServerTokenServices getTokenServices() {
		if (tokenServices == null) {
			tokenServices = new DefaultResourceServerTokenServices();
		}
		return tokenServices;
	}
	
	public PasswordEncoder getPasswordEncoderOrDefault() {
		if (null == passwordEncoder) {
			passwordEncoder = new DefaultPasswordEncoder();
		}
		return passwordEncoder;
	}
	
	public ClientDetailsResolver getClientDetailsResolverOrDefault() {
		if (null == clientDetailsResolver) {
			clientDetailsResolver = new DefaultClientDetailsResolver(this.getPasswordEncoderOrDefault());
		}
		return clientDetailsResolver;
	}
	
	public String getResourceIdOrDefault() {
		return resourceId;
	}
	
	public ClientDetailsService getClientDetailsServiceOrDefault() {
		return clientDetailsService = new InMemoryClientDetailsService();
	}
	
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
	public void setTokenServices(ResourceServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}
	public void setTokenExtractor(TokenExtractor tokenExtractor) {
		this.tokenExtractor = tokenExtractor;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public void setClientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
	}
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
	public ClientDetailsService getClientDetailsService() {
		return clientDetailsService;
	}
}
