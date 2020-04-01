package com.zy.security.oauth2.config.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetailsService;
import com.zy.security.oauth2.store.InMemoryClientDetailsService;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午6:48:59;
 * @Description:
 */
public class InMemoryClientDetailsServiceBuilder implements ClientDetailsServiceBuilder {
	private List<ClientBuilder> clientBuilders = new ArrayList<>();

	public ClientBuilder withClient(String clientId) {
		for (ClientBuilder clientBuilder : clientBuilders) {
			if (clientBuilder.getClientId().equals(clientId)) {
				return clientBuilder;
			}
		}
		ClientBuilder builder = new ClientBuilder(this,clientId);
		clientBuilders.add(builder);
		return builder;
	}

	@Override
	public ClientDetailsServiceBuilder and() {
		return this;
	}

	@Override
	public ClientDetailsService builder(int accessTokenValiditySeconds, int refreshTokenValiditySeconds) {
		Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();
		
		for (ClientBuilder clientBuilder : clientBuilders) {
			ClientDetails builder = clientBuilder.builder();
			if (builder.getAccessTokenValiditySeconds() == null) {
				builder.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
			}
			if (builder.getRefreshTokenValiditySeconds() == null) {
				builder.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
			}
			clientDetailsStore.put(builder.getClientId(), builder);
		}
		InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
		clientDetailsService.setClientDetailsStore(clientDetailsStore);
		return clientDetailsService;
	}
}
