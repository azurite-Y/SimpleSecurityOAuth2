package com.zy.security.oauth2.endpoint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.exception.RedirectException;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.RedirectResolver;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:18:25;
 * @Description:  重定向解析器的默认实现
 */
public class DefaultRedirectResolver implements RedirectResolver {
	private Collection<String> redirectGrantTypes = Arrays.asList(Oauth2Utils.implicit, Oauth2Utils.authorization_code);
	
	@Override
	public String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {
		List<String> grantTypes = client.getAuthorizedGrantTypes();
		
		if(grantTypes.isEmpty()) {
			throw new InvalidGrantException("客户端必须至少拥有一个授予类型");
		}
		
		for (String type : grantTypes) {
			if (type.equals(Oauth2Utils.refresh_token)) {
				continue;
			}
			if (!redirectGrantTypes.contains(type)) {
				throw new RedirectException("重定向uri只能由简单或授权码模式使用");
			}
		}
		
		List<String> registeredRedirectUris = client.getRegisteredRedirectUri();
		if (registeredRedirectUris == null || registeredRedirectUris.isEmpty()) {
			throw new RedirectException("必须至少向客户端注册一个重定向uri");
		}
		
		if (registeredRedirectUris.size() == 1 && requestedRedirect == null) {
			return registeredRedirectUris.iterator().next();
		}
		for (String redirectUri : registeredRedirectUris) {
			if ( requestedRedirect != null && requestedRedirect.equals(redirectUri) ) {
				return requestedRedirect;
			}
		}
		return null;
	}

}
