package com.zy.security.oauth2.authentication;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.oauth2.interfaces.TokenExtractor;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午2:30:23;
 * @Description: 默认的令牌提取器实现
 */
public class BearerTokenExtractor implements TokenExtractor {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Authentication extract(HttpServletRequest request) {
		String tokenValue = extractToken(request);
		if (tokenValue != null) {
			// 未认证的token，使用伪密码填充，且此密码不会被使用到（token不允许空值或空串）
			Authentication authentication = new UsernamePasswordAuthenticationToken
					(tokenValue,"pseudoPassword");
			return authentication;
		}
		return null;
	}

	private String extractToken(HttpServletRequest request) {
		// 首先从请求头中尝试获得
		String token = request.getHeader(Oauth2Utils.request_Header_token);

		if (token == null) {
			if(AuxiliaryTools.debug) {
				logger.info("请求头中没有找到令牌，查找的请求头参数名：{}",Oauth2Utils.request_Header_token);
			}
			token = request.getParameter(Oauth2Utils.access_token);
			if (token == null) {
				if(AuxiliaryTools.debug) {
					logger.info("请求参数中没有找到令牌，查找的请求参数名：{}",Oauth2Utils.access_token);
				}
			}else {
				if(AuxiliaryTools.debug) {
					logger.info("请求参数中提取的令牌：{}",token);
				}
			}
		}
		return token;
	}

}
