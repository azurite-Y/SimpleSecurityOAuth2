package com.zy.security.oauth2.endpoint;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.interfaces.AccessTokenConverter;
import com.zy.security.oauth2.interfaces.OAuth2AccessToken;
import com.zy.security.oauth2.interfaces.ResourceServerTokenServices;
import com.zy.security.oauth2.token.OAuth2AuthenticationToken;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.interfaces.RequestMatcher;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:42:13;
 * @Description:  /oauth/check_token - 解析验证accesstoken有效性
 */
public class CheckTokenEndpoint extends AbstractEndpoint  {
	
	private ResourceServerTokenServices resourceServerTokenServices;
	private AccessTokenConverter accessTokenConverter;
	
	public CheckTokenEndpoint(RequestMatcher postRequest, RequestMatcher getRequest) {
		super(postRequest, getRequest);
	}

	@Override
	public boolean endpoint(HttpServletRequest request,HttpServletResponse response, FilterChain chain) throws OAuth2Exception, AuthenticationException {
		if (super.getRequest != null && super.getRequest.match(request)) {
			doGet(request, response);
			return true;
		}
		if (super.postRequest != null && super.postRequest.match(request)) {
			doGet(request, response);
			return true;
		}
		return false;
	}

	private void doGet(HttpServletRequest request, HttpServletResponse response) throws OAuth2Exception, AuthenticationException {
		String token = request.getParameter(Oauth2Utils.token);
		// 首先查看AccessToken存储库中是否有AccessToken
		OAuth2AccessToken accesstoken = resourceServerTokenServices.readAccessToken(token);

		// 然后根据获得AccessToken获得OAuth2Authentication
		OAuth2AuthenticationToken authentication = resourceServerTokenServices.loadAuthentication(accesstoken.getValue());

		@SuppressWarnings("unchecked")
		Map<String, Object> convertAccessToken = (Map<String, Object>) accessTokenConverter.convertAccessToken(accesstoken, authentication);
		
		// 到此还未抛出异常则代表AccessToken可用
		convertAccessToken.put(Oauth2Utils.active, true);	
		super.replyJson(response, convertAccessToken);
	}

	public ResourceServerTokenServices getResourceServerTokenServices() {
		return resourceServerTokenServices;
	}
	public void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
		this.resourceServerTokenServices = resourceServerTokenServices;
	}
	public AccessTokenConverter getAccessTokenConverter() {
		return accessTokenConverter;
	}
	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
	}
}
