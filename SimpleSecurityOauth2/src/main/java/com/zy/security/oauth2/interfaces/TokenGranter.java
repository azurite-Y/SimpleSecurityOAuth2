package com.zy.security.oauth2.interfaces;

import com.zy.security.core.authentication.exception.AuthenticationException;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午11:02:57;
 * @Description: token生成器
 */
public interface TokenGranter {

	OAuth2AccessToken grant(String grantType, RequestDetails requestDetails,ClientDetails client) throws AuthenticationException;

	boolean supports(String granterType);
}
