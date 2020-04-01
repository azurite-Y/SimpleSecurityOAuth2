package com.zy.security.oauth2.authentication;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.details.OAuth2AuthenticationDetails;
import com.zy.security.oauth2.exception.ClientRegistrationException;
import com.zy.security.oauth2.exception.InvalidTokenException;
import com.zy.security.oauth2.exception.OAuth2AccessDeniedException;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.ResourceServerTokenServices;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午3:01:11;
 * @Description: oauth2的认证管理器
 */
public class OAuth2AuthenticationManager implements AuthenticationManager {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ResourceServerTokenServices tokenServices;
	private ClientDetailsService clientDetailsService;
	private ClientDetailsResolver clientDetailsResolver;
	// 每个资源服务器拥有一个资源id，此值代表当前资源服务器的资源id
	private String resourceId;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw new InvalidTokenException("无效的token，token不能为null");
		}
		String token = (String) authentication.getPrincipal();
		
		// 在存储OAuth2AuthenticationToken的时候一定是已认证的
		OAuth2AuthenticationToken oauth2Authentication = tokenServices.loadAuthentication(token);
		
		if (oauth2Authentication == null) {
			throw new InvalidTokenException("无效的token: " + token);
		}

		Collection<String> resourceIds = oauth2Authentication.getRequestDetails().getResourceIds();
		if (resourceId != null && resourceIds != null && !resourceIds.contains(resourceId)) {
			throw new OAuth2AccessDeniedException("访问拒绝：无权访问此资源服务器，resourceId："+this.resourceId) ;
		}
		checkClientDetails(oauth2Authentication);

		// 填充details属性
		if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
			oauth2Authentication.setDetails(authentication.getDetails());
		}
		
		if (AuxiliaryTools.debug) {
			logger.info("认证通过：{}",oauth2Authentication);
		}
		return oauth2Authentication;
	}

	/*
	 * 在生成令牌或授权码的时候已验证ClientDetails与请求参数值相同，且保证请求提供的客户端id和secret有效，
	 * 除非人为修改过客户端信息存储库和令牌存储库的内容否则两存储库保存的内容一定相同，此处验证两存储保存的内容
	 */
	private void checkClientDetails(OAuth2AuthenticationToken auth) throws OAuth2AccessDeniedException {
		if (clientDetailsService != null) {
			RequestDetails requestDetails = auth.getRequestDetails();
			ClientDetails client;
			try {
				client = clientDetailsService.loadClientByClientId(requestDetails.getClientId());
			}
			catch (ClientRegistrationException e) {
				throw new OAuth2AccessDeniedException("无效的令牌包含无效的客户端id");
			}
			List<RolePermission> clientScope = client.getScope();
			clientDetailsResolver.resolveScopes(client, clientScope);
		}
	}

	//-------------get、set----------------
	public ResourceServerTokenServices getTokenServices() {
		return tokenServices;
	}
	public void setTokenServices(ResourceServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}
	public ClientDetailsService getClientDetailsService() {
		return clientDetailsService;
	}
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
	public ClientDetailsResolver getClientDetailsResolver() {
		return clientDetailsResolver;
	}
	public void setClientDetailsResolver(ClientDetailsResolver clientDetailsResolver) {
		this.clientDetailsResolver = clientDetailsResolver;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
