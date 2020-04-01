package com.zy.security.oauth2.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.WebAuthenticationDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.filter.AbstractAuthenticationProcessingFilter;
import com.zy.security.web.util.AnyRequestMapping;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午3:46:49;
 * @Description:
 */
public class ClientCredentialsTokenEndpointFilter extends AbstractAuthenticationProcessingFilter {
	private boolean postOnly;
	
	// 默认 /oauth/token
	public ClientCredentialsTokenEndpointFilter(AnyRequestMapping loginUrl) {
		super(loginUrl);
	}
	public ClientCredentialsTokenEndpointFilter(String loginUrl, boolean postOnly) {
		super(loginUrl);
		this.postOnly = postOnly;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationException("不支持此类型Http认证请求: " + request.getMethod());
		}
		String clientId = request.getParameter(Oauth2Utils.client_id);
		String secret = request.getParameter(Oauth2Utils.client_secret);
		
		clientId = clientId==null?"":clientId;
		secret = secret==null?"":secret;
		
		if(clientId.isEmpty() || secret.isEmpty()) {
			throw new IllegalArgumentException("请求参数携带不充分.by: clientId="+clientId+" ,secret="+secret);
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientId, secret);
		
		setDetails(request, token);
		
		return super.authenticationManager.authenticate(token);
	}
	
	/**
	 * 置有关身份验证请求的其他详细信息，比如：IP、证书序列号
	 * @param request
	 * @param authRequest
	 */
	private void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(new WebAuthenticationDetails(request));
	}
	public boolean isPostOnly() {
		return postOnly;
	}
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
}
