package com.zy.security.oauth2.config.subject;

import com.zy.security.oauth2.interfaces.ClientDetailsService;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午6:48:28;
 * @Description: 
 */
public interface ClientDetailsServiceBuilder {
	/**
	 * 配置客户端id
	 * @param clientId
	 * @return
	 */
	ClientBuilder withClient(String clientId);
	ClientDetailsServiceBuilder and();
	ClientDetailsService builder(int accessTokenValiditySeconds, int refreshTokenValiditySeconds);
}
