package com.zy.security.oauth2.interfaces;

import com.zy.security.oauth2.exception.ClientRegistrationException;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午6:07:09;
 * @Description: 客户端详细信息服务
 */
public interface ClientDetailsService {
	
	 /**
	  * 根据clientId获得对应的客户端详细信息
	 * @param clientId
	 * @return
	 * @throws ClientRegistrationException
	 */
	ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException;
}
