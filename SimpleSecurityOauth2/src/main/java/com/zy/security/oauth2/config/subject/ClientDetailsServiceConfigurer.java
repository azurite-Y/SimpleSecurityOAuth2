package com.zy.security.oauth2.config.subject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.oauth2.interfaces.ClientDetailsService;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:59:07;
 * @Description:
 */
public final class ClientDetailsServiceConfigurer {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String inMemory = "inMemory";
	private final String jdbc = "jdbc";
	private ClientDetailsService clientDetailsService;
	
	private Map<String, ClientDetailsServiceBuilder> map = new HashMap<>();
	
	public ClientDetailsServiceConfigurer() {
		super();
	}
	
	/**
	 * 配置内存中的客户端信息
	 * @return
	 * @throws Exception
	 */
	public ClientDetailsServiceBuilder inMemory() throws Exception {
		return getOrApply(this.inMemory);
	}
	
	private ClientDetailsServiceBuilder getOrApply(String name) {
		ClientDetailsServiceBuilder builder = map.get(name);
		if(builder == null) {
			switch (name) {
				case inMemory : 
					builder = new InMemoryClientDetailsServiceBuilder();
					map.put(inMemory, builder);
					break;
				case jdbc :  
					map.put(jdbc, builder);
					logger.error("JDBC式的ClientDetails信息获取尚未实现！");
					break;
			}
		}
		return builder;
	}
	
	/**
	 * 当前只能使用一种数据源
	 * @param refreshTokenValiditySeconds 
	 * @param accessTokenValiditySeconds 
	 * @return
	 */
	public ClientDetailsService builder(int accessTokenValiditySeconds, int refreshTokenValiditySeconds){
		ClientDetailsServiceBuilder clientDetailsServiceBuilder = map.get(this.inMemory);
		if(clientDetailsServiceBuilder != null) {
			clientDetailsService = clientDetailsServiceBuilder.builder(accessTokenValiditySeconds,refreshTokenValiditySeconds);
		}
		return clientDetailsService;
	}

	// 意在获得授权服务器配置类中的客户端信息数据源，需在builder()方法之后调用
	public ClientDetailsService getClientDetailsService() {
		return clientDetailsService;
	}
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
}