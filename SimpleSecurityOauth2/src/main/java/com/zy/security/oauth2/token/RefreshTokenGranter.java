package com.zy.security.oauth2.token;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午1:02:53;
 * @Description:
 */
public class RefreshTokenGranter extends AbstractTokenGranter {

	public RefreshTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	public boolean supports(String granterType) {
		return granterType.equals(Oauth2Utils.refresh_token);
	}

	/**
	 * 获得刷新令牌
	 */
	@Override
	public OAuth2AccessToken grant(String grantType, RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		String refreshToken = requestDetails.getHttpServletRequest().getParameter(Oauth2Utils.refresh_token_parame);
		return super.tokenServices.refreshAccessToken(refreshToken, requestDetails, client);
	}

	/**
	 * 空方法，刷新访问令牌时不会被调用到
	 */
	@Override
	protected OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		return null;
	}

}
