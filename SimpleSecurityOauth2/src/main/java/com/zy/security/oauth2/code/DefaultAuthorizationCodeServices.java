package com.zy.security.oauth2.code;

import com.zy.security.oauth2.exception.InvalidGrantException;
import com.zy.security.oauth2.interfaces.AuthorizationCodeServices;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.oauth2.utils.RandomValueStringGenerator;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午3:06:36;
 * @Description: 签发和储存授权码服务的基本实现
 */
public abstract class DefaultAuthorizationCodeServices implements AuthorizationCodeServices {
	private RandomValueStringGenerator generator;
	
	public DefaultAuthorizationCodeServices(int randomLength) {
		super();
		this.generator = new RandomValueStringGenerator(randomLength);
	}
	public DefaultAuthorizationCodeServices(RandomValueStringGenerator generator) {
		super();
		this.generator = generator;
	}

	/**
	 * 存储授权码
	 * @param code
	 * @param authentication
	 */
	protected abstract void store(String code, OAuth2AuthenticationToken authentication);
	/**
	 * 删除授权码
	 * @param code
	 * @return
	 */
	protected abstract OAuth2AuthenticationToken remove(String code);
	
	@Override
	public String createAuthorizationCode(OAuth2AuthenticationToken authentication) {
		String code = generator.generate();
		store(code, authentication);
		return code;
	}

	@Override
	public OAuth2AuthenticationToken consumeAuthorizationCode(String code) throws InvalidGrantException {
		OAuth2AuthenticationToken auth = this.remove(code);
		if (auth == null) {
			throw new InvalidGrantException("无效的授权码: " + code);
		}
		return auth;
	}

}
