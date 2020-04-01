package com.zy.security.oauth2.token;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.exception.BadCredentialsException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.WebAuthenticationDetails;
import com.zy.security.core.userdetails.exception.AccountStatusException;
import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.interfaces.AuthorizationServerTokenServices;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午11:37:00;
 * @Description:
 */
public class PasswordTokenGranter extends AbstractTokenGranter {
	// ProviderManager - 效验用户名与密码，需提供自定义的UserDetailsService实现
	private AuthenticationManager authenticationManager;
	
	public PasswordTokenGranter(AuthorizationServerTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	public boolean supports(String granterType) {
		return granterType.equals(Oauth2Utils.password);
	}

	@Override
	protected OAuth2AuthenticationToken getOauth2Authentication(RequestDetails requestDetails, ClientDetails client)
			throws AuthenticationException {
		/*
		 * 在password模式下，能执行到着没有发生异常，那么代表着由clientId和clientSecret创建的UsernamePasswordAuthenticationToken已经过身份认证。
		 * 此时验证提供的用户名密码
		 */
		HttpServletRequest request = requestDetails.getHttpServletRequest();
		String username = request.getParameter(Oauth2Utils.usernameParameter);
		String password = request.getParameter(Oauth2Utils.passwordParameter);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		// 在此设置Details属性避免认证之后的Token Details属性为null
		authenticationToken.setDetails(new WebAuthenticationDetails(request));
		
		Authentication authentication = null;
		try {
			authentication = this.authenticationManager.authenticate(authenticationToken);
		}
		catch (AccountStatusException e) {
			throw new InvalidGrantException(e.getMessage());
		}
		catch (BadCredentialsException e) {
			throw new InvalidGrantException(e.getMessage());
		}
		return new OAuth2AuthenticationToken(requestDetails, authentication);
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
