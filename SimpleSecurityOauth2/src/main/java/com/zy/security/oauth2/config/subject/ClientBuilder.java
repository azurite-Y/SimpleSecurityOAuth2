package com.zy.security.oauth2.config.subject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zy.security.oauth2.details.BasicClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.web.util.AuthorityUtils;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午7:34:26;
 * @Description:
 */
public class ClientBuilder {
	private BasicClientDetails basicClientDetails = new BasicClientDetails();
	private ClientDetailsServiceBuilder builder;
	//----------------------------------
	/**
	 * 访问令牌有效时间（秒）
	 * @param accessTokenValiditySeconds
	 * @return
	 */
	public ClientBuilder accessTokenValiditySeconds(int accessTokenValiditySeconds) {
		basicClientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
		return this;
	}
	
	/**
	 * 授权的代理类型
	 * @param authorizedGrantTypes
	 * @return
	 */
	public ClientBuilder authorizedGrantTypes(String... authorizedGrantTypes) {
		List<String> asList = Arrays.asList(authorizedGrantTypes);
		basicClientDetails.setAuthorizedGrantTypes(asList);
		return this;
	}

	/**
	 * 刷新令牌有效时间
	 * @param refreshTokenValiditySeconds
	 * @return
	 */
	public ClientBuilder refreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		basicClientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
		return this;
	}

	/**
	 * 客户端密码
	 * @param secret
	 * @return
	 */
	public ClientBuilder secret(String secret) {
		basicClientDetails.setClientSecret(secret);
		return this;
	}

	/**
	 * 客户端作用域，底层使用PluralisticRolePermission或SimpleRolePermission封装，设置格式需与之相匹配
	 * @param scopes
	 * @return
	 */
	public ClientBuilder scopes(String... scopes) {
		basicClientDetails.setScopes( AuthorityUtils.createAuthorityList(scopes));
		return this;
	}

	/**
	 * 权限
	 * @param authorities
	 * @return
	 */
	public ClientBuilder authorities(String... authorities) {
		basicClientDetails.setAuthorities(AuthorityUtils.createAuthorityList(authorities));
		return this;
	}

	/**
	 * 是否自动授权，true：直接授权，false：需手动确认
	 * @param autoApprove
	 * @return
	 */
	public ClientBuilder autoApprove(boolean autoApprove) {
		basicClientDetails.setAutoApprove(autoApprove);
		return this;
	}

	/**
	 * 自动授权的作用域，在开启自动授权之后有效
	 * @param scopes
	 * @return
	 */
	public ClientBuilder autoApproveScopes(String... scopes) {
		if (basicClientDetails.isAutoApprove()) {
			basicClientDetails.setAutoApproveScopes(Arrays.asList(scopes));
		}
		return this;
	}
	
	/**
	 * 授权之后重定向的uri
	 * @param uris
	 * @return
	 */
	public ClientBuilder redirectUris(String... uris) {
		basicClientDetails.setRegisteredRedirectUris(Arrays.asList(uris));
		return this;
	}
	
	/**
	 * 客户端可访问的资源服务器的资源id
	 * @param resourceIds
	 * @return
	 */
	public ClientBuilder resourceIds(String... resourceIds) {
		basicClientDetails.setResourceIds(Arrays.asList(resourceIds));
		return this;
	}
	
	/**
	 * 添加附加信息
	 * @param map
	 * @return
	 */
	public ClientBuilder additionalInformation(Map<String, Object> map) {
		basicClientDetails.setAdditionalInformation(map);
		return this;
	}

	/**
	 * 添加附加信息，可通过 “:” 或 “=”分割参数名与参数值
	 * @param pairs
	 * @return
	 */
	public ClientBuilder additionalInformation(String... pairs) {
		for (String pair : pairs) {
			String separator = ":";
			if (!pair.contains(separator) && pair.contains("=")) {
				separator = "=";
			}
			int index = pair.indexOf(separator);
			String key = pair.substring(0, index > 0 ? index : pair.length());
			String value = index > 0 ? pair.substring(index+1) : null;
			basicClientDetails.addAdditionalInformation(key, (Object) value);
		}
		return this;
	}
	
	public ClientDetailsServiceBuilder and() {
		return this.builder;
	}
	
	public ClientDetails builder() {
		return basicClientDetails;
	}
	
	//----------------------------------
	public ClientBuilder() {
		super();
	}
	public ClientBuilder(ClientDetailsServiceBuilder clientDetailsServiceBuilder,String clientId) {
		this.builder = clientDetailsServiceBuilder;
		basicClientDetails.setClientId(clientId);
	}
	public String getClientId() {
		return basicClientDetails.getClientId();
	}
}
