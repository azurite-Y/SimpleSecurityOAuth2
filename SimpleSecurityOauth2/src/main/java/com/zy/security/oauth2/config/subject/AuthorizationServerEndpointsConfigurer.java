package com.zy.security.oauth2.config.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.oauth2.approval.ApprovalStoreUserApprovalHandler;
import com.zy.security.oauth2.endpoint.DefaultRedirectResolver;
import com.zy.security.oauth2.interfaces.AccessTokenConverter;
import com.zy.security.oauth2.interfaces.ApprovalStore;
import com.zy.security.oauth2.interfaces.AuthorizationCodeServices;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.RedirectResolver;
import com.zy.security.oauth2.interfaces.TokenGranter;
import com.zy.security.oauth2.interfaces.TokenStore;
import com.zy.security.oauth2.interfaces.UserApprovalHandler;
import com.zy.security.oauth2.store.InMemoryApprovalStore;
import com.zy.security.oauth2.store.InMemoryAuthorizationCodeServices;
import com.zy.security.oauth2.store.InMemoryTokenStore;
import com.zy.security.oauth2.token.DefaultAccessTokenConverter;
import com.zy.security.oauth2.token.DefaultAuthorizationServerTokenServices;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:58:06;
 * @Description:
 */
public final class AuthorizationServerEndpointsConfigurer {
	private AccessTokenConverter accessTokenConverter;
	private ApprovalStore approvalStore;
	private AuthorizationCodeServices authorizationCodeServices;
	private Map<String, String> patternMap = new HashMap<>();
	private ClientDetailsResolver clientDetailsResolver;
	private RedirectResolver redirectResolver;
//	private TokenEnhancer tokenEnhancer;
	private boolean reuseRefreshToken;
	private AuthorizationServerTokenServices tokenServices;
	private TokenGranter tokenGranter;
	private TokenStore tokenStore;
	private UserApprovalHandler userApprovalHandler;
	private UserDetailsService userDetailsService;
	private int authorizationCodeSize = 6;
	// sprng security oauth2默认30天 - 60 * 60 * 24 * 30
	private int refreshTokenValiditySeconds =  60 * 60 * 24 * 30;
	// sprng security oauth2默认12天 - 60 * 60 * 24 * 12
	private int accessTokenValiditySeconds =  60 * 60 * 24 * 12;
	private List<String> defaultUris = new ArrayList<>();
	private List<String> mappings = new ArrayList<>();
	
	/**
	 * 配置刷新令牌有效时间
	 * @param authorizationCodeSize
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer refreshTokenValiditySeconds (int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		return this;
	}
	
	/**
	 * 配置访问令牌有效时间
	 * @param authorizationCodeSize
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer accessTokenValiditySeconds (int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		return this;
	}
	
	/**
	 * 配置授权码字符位数
	 * @param authorizationCodeSize
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer authorizationCodeSize (int authorizationCodeSize) {
		
		this.authorizationCodeSize = authorizationCodeSize;
		return this;
	}
	
	/**
	 * 访问令牌转换器，默认使用 DefaultAccessTokenConverter 对象，
	 * 用于在令牌内存储身份验证数据的转换器接口 
	 * @param accessTokenConverter
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer accessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
		return this;
	}
	
	/**
	 * 批准存储库，默认使用 InMemoryApprovalStore 对象
	 * @param approvalStore
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer approvalStore(ApprovalStore approvalStore) {
		this.approvalStore = approvalStore;
		return this;
	}
	
	/**
	 * 授权代码服务，默认使用 InMemoryAuthorizationCodeServices 对象进行授权码的签发和储存服务
	 * 内部使用 RandomValueStringGenerator.generate()方法生成6位随机字符作为授权码
	 */
	public AuthorizationServerEndpointsConfigurer authorizationCodeServices(
			AuthorizationCodeServices authorizationCodeServices) {
		this.authorizationCodeServices = authorizationCodeServices;
		return this;
	}
	
	/**
	 * 端节点的路径映射，映射之后可使用映射路径自定义端节点回复内容，如自定义授权界面
	 * @param defaultPath - 原路径
	 * @param customPath - 映射的路径
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer pathMapping(String defaultPath, String customPath) {
		boolean contains = defaultUris.contains(defaultPath);
		if (!contains) {
			throw new IllegalArgumentException("映射的端节点原路径与预设不符，by："+defaultPath);
		}
		this.patternMap.put(defaultPath, customPath);
		mappings.add(customPath);
		return this;
	}
	
	/**
	 * 重定向解析程序，默认使用 DefaultRedirectResolver
	 * @param redirectResolver
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer redirectResolver(RedirectResolver redirectResolver) {
		this.redirectResolver = redirectResolver;
		return this;
	}
	
	/**
	 * 客户端信息验证程序，验证client_id、client_secret、scope、GrantType
	 * @param clientDetailsResolver
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer clientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
		return this;
	}
	
	/**
	 * 是否重用刷新令牌，默认false，即刷新accessToken时不更换refreshTokens
	 * @param reuseRefreshToken
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer reuseRefreshTokens(boolean reuseRefreshToken) {
		if(reuseRefreshToken) {
			this.reuseRefreshToken = reuseRefreshToken;
		}
		return this;
	}
	
	/**
	 * 令牌增强器,用于自定义的创建令牌
	 * @param tokenEnhancer
	 * @return
	 */
//	public AuthorizationServerEndpointsConfigurer tokenEnhancer(TokenEnhancer tokenEnhancer) {
//		this.tokenEnhancer = tokenEnhancer;
//		return this;
//	}
	
	/**
	 * 令牌管家，默认使用 ”CompositeTokenGranter“ 根据grantType调用对应的TokenGranter
	 * @param tokenGranter
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer tokenGranter(TokenGranter tokenGranter) {
		this.tokenGranter = tokenGranter;
		return this;
	}
	
	/**
	 * 令牌服务，默认值DefaultTokenServices。负责AccessToken的签发、刷新、获得
	 * @param tokenServices
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer tokenServices(AuthorizationServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
		return this;
	}
	
	/**
	 * 令牌存储库，默认使用 “InMemoryTokenStore”
	 * @param tokenStore
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer tokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
		return this;
	}
	
	/**
	 *  用户批准处理程序，默认使用 ApprovalStoreUserApprovalHandler 实现
	 */
	public AuthorizationServerEndpointsConfigurer userApprovalHandler(UserApprovalHandler approvalHandler) {
		this.userApprovalHandler = approvalHandler;
		return this;
	}
	
	/**
	 * 在密码模式下，为请求提供用户身份信息的效验服务
	 * @param userDetailsService
	 * @return
	 */
	public AuthorizationServerEndpointsConfigurer userDetailsService(UserDetailsService userDetailsService) {
		if (userDetailsService != null) {
			this.userDetailsService = userDetailsService;
		}
		return this;
	}
	
	public AccessTokenConverter getAccessTokenConverterOrDefault() {
		if(accessTokenConverter == null) {
			this.accessTokenConverter = new DefaultAccessTokenConverter(this.reuseRefreshToken);
		}
		return this.accessTokenConverter;
	}
	
	public ApprovalStore getApprovalStoreOrDefault() {
		if(this.approvalStore == null) {
			this.approvalStore = new InMemoryApprovalStore();
		}
		return approvalStore;
	}
	
	// List<TokenGranter> tokenGranters; AuthenticationManager
	public TokenGranter getTokenGranter() {
		return tokenGranter;
	}
	
	public TokenStore getTokenStoreOrDefault() {
		if(tokenStore == null) {
			tokenStore = new InMemoryTokenStore();
		}
		return tokenStore;
	}
	
	public UserApprovalHandler getUserApprovalHandlerOrDefault() {
		if(this.userApprovalHandler == null) {
			this.userApprovalHandler = new ApprovalStoreUserApprovalHandler(this.getApprovalStoreOrDefault());
		}
		return userApprovalHandler;
	}
	
	// authBuilder
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
//	public TokenEnhancer getTokenEnhancer() {
//		return null;
//	}
	
	public RedirectResolver getRedirectResolverOrDefault() {
		if(redirectResolver == null) {
			redirectResolver = new DefaultRedirectResolver();
		}
		return redirectResolver;
	}
	
	public AuthorizationCodeServices getAuthorizationCodeServicesOrDefault() {
		if(authorizationCodeServices == null) {
			authorizationCodeServices = new InMemoryAuthorizationCodeServices(this.authorizationCodeSize);
		}
		return authorizationCodeServices;
	}
	
	public Map<String, String> getPatternMap() {
		return patternMap;
	}
	
	// PasswordEncoder passwordEncoder; 从WebSecurityConfigurerAdapter中获得
	public ClientDetailsResolver getClientDetailsResolver() {
		return clientDetailsResolver;
	}
	
	public int getRefreshTokenValiditySecondsOrDefault() {
		return refreshTokenValiditySeconds;
	}
	public int getAccessTokenValiditySecondsOrDefault() {
		return accessTokenValiditySeconds;
	}
	
	public AuthorizationServerTokenServices getTokenServicesOrDefault() {
		if (this.tokenServices == null) {
			this.tokenServices = new DefaultAuthorizationServerTokenServices(
					this.refreshTokenValiditySeconds,this.accessTokenValiditySeconds
					,getTokenStoreOrDefault(),this.reuseRefreshToken);
		}
		return tokenServices;
	}
	
	//-------------构造器------------------
	public AuthorizationServerEndpointsConfigurer() {
		super();
		this.defaultUris.add(Oauth2Utils.oauth_authorize);
		this.defaultUris.add(Oauth2Utils.oauth_check_token);
		this.defaultUris.add(Oauth2Utils.oauth_confirm_access);
		this.defaultUris.add(Oauth2Utils.oauth_token);
	}
	//-------------get、set------------------
	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
	}
	/**
	 * 存储映射的端节点uri
	 * @return
	 */
	public List<String> getMappings() {
		return mappings;
	}
	public void setMappings(List<String> mappings) {
		this.mappings = mappings;
	}
	public void setApprovalStore(ApprovalStore approvalStore) {
		this.approvalStore = approvalStore;
	}
	
	public void setAuthorizationCodeServices(AuthorizationCodeServices authorizationCodeServices) {
		this.authorizationCodeServices = authorizationCodeServices;
	}
	public void setPatternMap(Map<String, String> patternMap) {
		this.patternMap = patternMap;
	}
	public void setClientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
	}
	public void setRedirectResolver(RedirectResolver redirectResolver) {
		this.redirectResolver = redirectResolver;
	}
//	public void setTokenEnhancer(TokenEnhancer tokenEnhancer) {
//		this.tokenEnhancer = tokenEnhancer;
//	}
	public boolean isReuseRefreshToken() {
		return reuseRefreshToken;
	}
	public void setReuseRefreshToken(boolean reuseRefreshToken) {
		this.reuseRefreshToken = reuseRefreshToken;
	}
	public void setTokenServices(AuthorizationServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}
	public void setTokenGranter(TokenGranter tokenGranter) {
		this.tokenGranter = tokenGranter;
	}
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
	public void setUserApprovalHandler(UserApprovalHandler userApprovalHandler) {
		this.userApprovalHandler = userApprovalHandler;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	public void setAuthorizationCodeSize(int authorizationCodeSize) {
		this.authorizationCodeSize = authorizationCodeSize;
	}
	public int getAuthorizationCodeSize() {
		return authorizationCodeSize;
	}
	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
}
