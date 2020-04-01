package com.zy.security.oauth2.endpoint;

import java.util.Arrays;
import java.util.List;

import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.exception.ClientIDCrossDomainException;
import com.zy.security.oauth2.exception.ClientSecretException;
import com.zy.security.oauth2.exception.InvalidClientException;
import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.exception.InvalidScopeException;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetailsResolver;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午7:30:45;
 * @Description: 默认的客户端详细信息验证器
 */
public class DefaultClientDetailsResolver implements ClientDetailsResolver {

	private PasswordEncoder passwordEncoder;

	public DefaultClientDetailsResolver() {
		super();
	}

	public DefaultClientDetailsResolver(PasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void resolveSecret(String clientSecret, ClientDetails client) throws OAuth2Exception {
		Boolean flag = passwordEncoder.matches(clientSecret, client.getClientSecret());
		if (!flag) {
			throw new ClientSecretException("client secret 错误.");
		}
	}

	@Override
	public void resolveScopes(ClientDetails client, RolePermission... scope) throws OAuth2Exception {
		List<RolePermission> asList = Arrays.asList(scope);
		resolveScopes(client, asList);
	}

	@Override
	public void resolveScopes(ClientDetails client, List<RolePermission> scopes) throws OAuth2Exception {
		List<RolePermission> clientScopes = client.getScope();

		boolean flag = false;
		if (clientScopes.size()>scopes.size()) {
			for (RolePermission parameScope : scopes) {
				for (RolePermission clientSecpe : clientScopes) {
					flag = parameScope.compare(clientSecpe);
					if (!flag) {
						throw new InvalidScopeException("无效的scope：" + parameScope);
					}
				}
			}
		} else {
			for (RolePermission clientSecpe : clientScopes) {
				for (RolePermission parameScope : scopes) {
					flag = parameScope.compare(clientSecpe);
					if (!flag) {
						throw new InvalidScopeException("无效的scope：" + parameScope);
					}
				}
			}
		}
	}
	
	@Override
	public void resolveGrantTypes(ClientDetails clientDetails, String grantType) throws OAuth2Exception {
		List<String> authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
		if (!authorizedGrantTypes.contains(grantType)) {
			throw new InvalidGrantException("无效的GrantType");
		}
	}

	@Override
	public void resolveGrantTypes(ClientDetails clientDetails, String grantType, String noGrantType)
			throws OAuth2Exception {
		List<String> authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
		for (String authorizedGrantType : authorizedGrantTypes) {
			if ( authorizedGrantType.equals(noGrantType) || grantType.equals(noGrantType) ) {
				throw new InvalidGrantException("不合时宜的GrantType，by：implicit");
			}
//			if (!authorizedGrantTypes.contains(grantType)) {
//				throw new ClientIDCrossDomainException("clientID不能跨域使用，by："+clientDetails.getClientId());
//			}
		}
	}

	@Override
	public void resolveClientId(ClientDetails clientDetails, RequestDetails requestDetails) {
		String clientId = requestDetails.getClientId();
		if (clientDetails == null) {
			throw new InvalidClientException("无效的客户端ID：" + clientId);
		}
		if (clientId != null && !clientId.equals("")) {
			if (!clientId.equals(clientDetails.getClientId())) {
				throw new InvalidClientException("无效的客户端ID：" + clientId);
			}
		}
		
		// client跨域验证
		String responseType = requestDetails.getResponseType();
		List<String> authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
		
		// 简单模式、授权码模式
		if (responseType != null) {
			boolean isAuthorizationCode = authorizedGrantTypes.contains(Oauth2Utils.authorization_code);
			if (isAuthorizationCode !=  responseType.equals(Oauth2Utils.code)) {
				throw new ClientIDCrossDomainException("客户端ID不允许跨域，by：" + clientId);
			}
		} else { // 密码模式、客户端模式
			String grantType = requestDetails.getGrantType();
			if (!authorizedGrantTypes.contains(grantType)) {
				throw new ClientIDCrossDomainException("客户端ID不允许跨域，by：" + clientId);
			}
		}
		
	}
}
