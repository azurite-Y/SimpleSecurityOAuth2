package com.zy.security.oauth2.store;

import java.util.Map;

import com.zy.security.oauth2.exception.ClientRegistrationException;
import com.zy.security.oauth2.interfaces.ClientDetails;
import com.zy.security.oauth2.interfaces.ClientDetailsService;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午3:12:38;
 * @Description: 获得客户端信息
 */
public class InMemoryClientDetailsService implements ClientDetailsService {
	// clientid：ClientDetails
	private Map<String, ClientDetails> clientDetailsStore;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientDetails details = clientDetailsStore.get(clientId);
	    if (details == null) {
	      throw new ClientRegistrationException("此客户端ID没有对应的客户端信息: " + clientId);
	    }
	    return details;
	}

	public Map<String, ClientDetails> getClientDetailsStore() {
		return clientDetailsStore;
	}
	public void setClientDetailsStore(Map<String, ClientDetails> clientDetailsStore) {
		this.clientDetailsStore = clientDetailsStore;
	}
}
