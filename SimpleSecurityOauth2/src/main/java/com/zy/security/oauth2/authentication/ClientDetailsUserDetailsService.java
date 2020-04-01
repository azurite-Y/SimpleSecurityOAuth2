package com.zy.security.oauth2.authentication;

import com.zy.security.core.authentication.User;
import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.oauth2.exception.ClientRegistrationException;
import com.zy.security.oauth2.exception.NoSuchClientException;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetailsService;

/**
 * @author: zy;
 * @DateTime: 2020年3月29日 下午10:41:57;
 * @Description: 为ClientCredentialsTokenEndpointFilter效验请求提供的clientId和clientSecret对照
 */
public class ClientDetailsUserDetailsService implements UserDetailsService {
	private ClientDetailsService clientDetailsService;
	
	public ClientDetailsUserDetailsService(ClientDetailsService clientDetailsService) {
		super();
		this.clientDetailsService = clientDetailsService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		ClientDetails clientDetails;
		try {
			clientDetails = clientDetailsService.loadClientByClientId(username);
		} catch (ClientRegistrationException e) {
			throw new NoSuchClientException("未找到相关的客户端信息");
		}
		String clientSecret = clientDetails.getClientSecret();
		if (clientSecret== null || clientSecret.trim().length()==0) {
			clientSecret = "";
		}
		return new User(clientDetails.getClientId(),clientSecret,clientDetails.getAuthorities());
	}

}
