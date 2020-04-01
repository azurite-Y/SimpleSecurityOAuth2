package com.zy.security.oauth2.approval;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.token.Authentication;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.UserApprovalHandler;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:11:13;
 * @Description:
 */
public class DefaultUserApprovalHandler implements UserApprovalHandler {
	
	@Override
	public boolean isApproved(RequestDetails requestDetails, Authentication userAuthentication) {
		return requestDetails.isApproved();
	}

//	@Override
//	public RequestDetails AddApproval(RequestDetails requestDetails, Authentication userAuthentication) {
//		return null;
//	}

	@Override
	public RequestDetails updateApproval(RequestDetails requestDetails, Authentication userAuthentication) {
		HttpServletRequest req = requestDetails.getHttpServletRequest();
		
		String flag = req.getParameter(Oauth2Utils.user_oauth_approval);
		boolean approved = flag != null && flag.toLowerCase().equals("true");
		
		requestDetails.setApproved(approved);
		
		return requestDetails;
	}

//	@Override
//	public Map<String, Object> getUserApprovalRequest(RequestDetails requestDetails,
//			Authentication userAuthentication) {
//		// TODO 自动生成的方法存根
//		return null;
//	}
	
}
