package com.zy.security.oauth2.interfaces;

import java.util.List;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.exception.OAuth2Exception;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午7:29:20;
 * @Description: client_id、client_secret、scope、GrantType验证
 */
public interface ClientDetailsResolver {
	/**
	 * 验证请求参数中的client_secret信息与预设的client_secret是否匹配
	 * @param clientSecret
	 * @param client
	 * @return
	 * @throws OAuth2Exception
	 */
	void resolveSecret(String clientSecret, ClientDetails client) throws OAuth2Exception;
	/**
	 * 验证请求参数中的scope信息与预设的scope是否匹配
	 * @param client
	 * @param scope
	 * @return
	 * @throws OAuth2Exception
	 */
	void resolveScopes(ClientDetails client,RolePermission... scope) throws OAuth2Exception;
	/**
	 * 验证请求参数中的scope信息与预设的scope是否匹配
	 * @param client
	 * @param scope
	 * @return
	 * @throws OAuth2Exception
	 */
	void resolveScopes(ClientDetails client,List<RolePermission> scope) throws OAuth2Exception;
	
	/**
	 * 验证请求参数中的grantType信息与预设的grantType是否匹配
	 * @param clientDetails
	 * @param grantType
	 */
	void resolveGrantTypes(ClientDetails clientDetails, String grantType);
	/**
	 * 验证请求参数中的grantType信息与预设的grantType是否匹配
	 * @param clientDetails
	 * @param grantType
	 * @param noGrantType - 不允许的grantType
	 */
	void resolveGrantTypes(ClientDetails clientDetails, String grantType, String noGrantType);
	
	/**
	 * 验证请求参数中的clientId信息与预设的clientId是否匹配，且不允许跨模式使用clientId
	 * @param clientDetails
	 * @param client
	 */
	void resolveClientId(ClientDetails clientDetails, RequestDetails requestDetails);
}
