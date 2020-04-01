package com.zy.security.oauth2.token;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午1:00:44;
 * @Description: 针对简单模式下的AccessToken创建
 */
public class ImplicitTokenGranter extends AbstractTokenGranter {

	public ImplicitTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	public boolean supports(String granterType) {
		return granterType.equals(Oauth2Utils.implicit);
	}

	@Override
	protected OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		return new OAuth2AuthenticationToken(requestDetails, authentication);
	}

}
