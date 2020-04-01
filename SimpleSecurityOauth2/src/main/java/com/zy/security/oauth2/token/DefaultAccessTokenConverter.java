package com.zy.security.oauth2.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.exception.InvalidRequestException;
import com.zy.security.oauth2.interfaces.AccessTokenConverter;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月21日 下午5:42:11;
 * @Description:
 */
public class DefaultAccessTokenConverter implements AccessTokenConverter {
	// 是否显示grantType
	private boolean includeGrantType;
	
	/**
	 * 默认构造器
	 * @param includeGrantType - 是否显示GrantType信息，为true则显示
	 */
	public DefaultAccessTokenConverter(boolean includeGrantType) {
		super();
		this.includeGrantType = includeGrantType;
	}

	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2AuthenticationToken oauth2Authentication) {
		Map<String, Object> response = new HashMap<String, Object>();
		RequestDetails requestDetails = oauth2Authentication.getRequestDetails();

		Authentication authentication = oauth2Authentication.getAuthentication();
		if (authentication != null) {
			response.put(Oauth2Utils.user_name, authentication.getPrincipal());
			response.put(Oauth2Utils.authorities, authentication.getAuthorities());
		} else {
			// AccessToken必须有相关联的Authentication，没有则意味着此AccessToken可能是凭空捏造的
			throw new InvalidRequestException("无效的AccessToken："+token.getValue());
		}

		if (token.getScope()!=null) {
			response.put(Oauth2Utils.scope, token.getScope());
		}

		if (token.getExpiration() != null) {
			response.put(Oauth2Utils.expires, token.getExpiration().getTime() / 1000);
		}
		String grantType = requestDetails.getGrantType();
		if (this.includeGrantType && grantType != null) {
			response.put(Oauth2Utils.grant_type, grantType);
		}

		response.putAll(token.getAdditionalInformation());
		
		response.put(Oauth2Utils.client_id, requestDetails.getClientId());
		
		List<String> resourceIds = requestDetails.getResourceIds();
		if (resourceIds != null && !resourceIds.isEmpty()) {
			response.put(Oauth2Utils.resources_id, resourceIds);
		}
		return response;
	}

	@Override
	public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
		return null;
	}

	@Override
	public OAuth2AuthenticationToken extractAuthentication(Map<String, ?> map) {
		return null;
	}

}
