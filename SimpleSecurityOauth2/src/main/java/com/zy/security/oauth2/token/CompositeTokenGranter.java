package com.zy.security.oauth2.token;

import java.util.List;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.TokenGranter;
import com.zy.utils.Assert;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午11:19:30;
 * @Description: token生成器调用策略实现
 */
public class CompositeTokenGranter implements TokenGranter {
	private List<TokenGranter> tokenGranters;
	
	public CompositeTokenGranter() {
		super();
	}
	public CompositeTokenGranter(List<TokenGranter> tokenGranters) {
		super();
		Assert.notNull(tokenGranters, "空的TokenGranter.");
		this.tokenGranters = tokenGranters;
	}

	@Override
	public OAuth2AccessToken grant(String grantType, RequestDetails requestDetails,ClientDetails client) 
			throws AuthenticationException{
		for (TokenGranter tokenGranter : tokenGranters) {
			// 根据grantType使用不同的TokenGranter
			boolean supports = tokenGranter.supports(grantType);
			if(supports) {
				return tokenGranter.grant(grantType, requestDetails,client);
			}
		}
		return null;
	}
	
	@Override
	public boolean supports(String granterType) {
		return granterType.equals(granterType);
	}
	
	
	public List<TokenGranter> getTokenGranters() {
		return tokenGranters;
	}
	public void setTokenGranters(List<TokenGranter> tokenGranters) {
		this.tokenGranters = tokenGranters;
	}
}
