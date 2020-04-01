package com.zy.security.oauth2.interfaces;

import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.exception.OAuth2Exception;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午9:54:26;
 * @Description:  用于确定当前用户是否已批准给定客户端身份验证请求的基本接口
 */
public interface UserApprovalHandler {
	/**
	 * 验证是否启用自动批准
	 * @param requestDetails
	 * @param userAuthentication
	 * @return
	 */
	boolean isApproved(RequestDetails requestDetails,	Authentication userAuthentication);
//	RequestDetails AddApproval(RequestDetails requestDetails,Authentication userAuthentication);
	/**
	 * 存储或更新用户的批准信息
	 * @param requestDetails
	 * @param userAuthentication
	 * @return
	 */
	RequestDetails updateApproval(RequestDetails requestDetails,	Authentication userAuthentication) throws OAuth2Exception;
//	Map<String, Object> getUserApprovalRequest(RequestDetails requestDetails,Authentication userAuthentication);
}
