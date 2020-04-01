package com.zy.security.oauth2.store;

import java.util.concurrent.ConcurrentHashMap;

import com.zy.security.oauth2.code.DefaultAuthorizationCodeServices;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.oauth2.utils.RandomValueStringGenerator;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午3:33:46;
 * @Description: 存储于内存中的签发和储存授权码服务实现
 */
public class InMemoryAuthorizationCodeServices extends DefaultAuthorizationCodeServices {
	// code:oauth
	protected final ConcurrentHashMap<String, OAuth2AuthenticationToken> authorizationCodeStore = new ConcurrentHashMap<String, OAuth2AuthenticationToken>();
	
	/**
	 * @param randomLength - 生成的授权码字符数，默认为6
	 */
	public InMemoryAuthorizationCodeServices(int randomLength) {
		super(new RandomValueStringGenerator(randomLength));
	}
	public InMemoryAuthorizationCodeServices(RandomValueStringGenerator generator) {
		super(generator);
	}

	@Override
	protected void store(String code, OAuth2AuthenticationToken authentication) {
		this.authorizationCodeStore.put(code, authentication);
	}

	@Override
	protected OAuth2AuthenticationToken remove(String code) {
		OAuth2AuthenticationToken auth = this.authorizationCodeStore.remove(code);
		return auth;
	}

}
