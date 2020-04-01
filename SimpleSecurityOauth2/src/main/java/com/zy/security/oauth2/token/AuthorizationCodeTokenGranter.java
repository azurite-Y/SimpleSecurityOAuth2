package com.zy.security.oauth2.token;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.exception.InvalidRequestException;
import com.zy.security.oauth2.interfaces.AuthorizationCodeServices;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午12:57:21;
 * @Description: 针对授权码模式下的AccessToken创建
 */
public class AuthorizationCodeTokenGranter extends AbstractTokenGranter {
	
	private AuthorizationCodeServices authorizationCodeServices;
	
	public AuthorizationCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			AuthorizationCodeServices authorizationCodeServices) {
		super(tokenServices);
		this.authorizationCodeServices = authorizationCodeServices;
	}
	public AuthorizationCodeTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	public boolean supports(String granterType) {
		return granterType.equals(Oauth2Utils.authorization_code);
	}

	@Override
	protected OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		HttpServletRequest request = requestDetails.getHttpServletRequest();
		
		String code = request.getParameter(Oauth2Utils.code);
		if(code == null) {
			throw new InvalidRequestException("必须提供一个授权码");
		}
		
		OAuth2AuthenticationToken storeAuth = authorizationCodeServices.consumeAuthorizationCode(code);
		if (storeAuth == null) {
			throw new InvalidGrantException("失效的授权码: " + code);
		}
		
		return new OAuth2AuthenticationToken(requestDetails, storeAuth);
	}
	
	public AuthorizationCodeServices getAuthorizationCodeServices() {
		return authorizationCodeServices;
	}
	public void setAuthorizationCodeServices(AuthorizationCodeServices authorizationCodeServices) {
		this.authorizationCodeServices = authorizationCodeServices;
	}
}
