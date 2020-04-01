package com.zy.security.oauth2.approval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.approval.Approval.ApprovalStatus;
import com.zy.security.oauth2.exception.OAuth2Exception;
import com.zy.security.oauth2.exception.UserDeniedAuthorizationException;
import com.zy.security.oauth2.interfaces.ApprovalStore;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.interfaces.UserApprovalHandler;
import com.zy.security.oauth2.utils.Oauth2Utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午7:50:02;
 * @Description: 将用户的授权请求存储到ApprovalStore中
 */
public class ApprovalStoreUserApprovalHandler implements UserApprovalHandler {

	private ApprovalStore approvalStore;
	private int approvalExpirySeconds = -1;
	
	public ApprovalStoreUserApprovalHandler(ApprovalStore approvalStore) {
		super();
		this.approvalStore = approvalStore;
	}

	@Override
	public boolean isApproved(RequestDetails requestDetails, Authentication userAuthentication) {
		return requestDetails.isApproved();
	}

	@Override
	public RequestDetails updateApproval(RequestDetails requestDetails, Authentication userAuthentication) throws OAuth2Exception {
		List<RolePermission> requestedScopes = requestDetails.getScope();
		// 已批准的权限
		List<RolePermission> approvedScopes = new ArrayList<>();
		// 将要保存的Approval对象集合
		List<Approval> approvals = new ArrayList<>();
		
		Date expiry = computeExpiry();

		HttpServletRequest req = requestDetails.getHttpServletRequest();
		Map<String, String[]> parameterMap = req.getParameterMap();
		
		for (RolePermission rolePermission : requestedScopes) {
			String value = parameterMap.get(rolePermission.getValue())[0];
			
			value = value == null ? "" : value.toLowerCase();
			if ("true".equals(value) || value.startsWith("approve")) {
				approvedScopes.add(rolePermission);
				approvals.add(new Approval(userAuthentication.getPrincipal().toString(),
							requestDetails.getClientId(),value, expiry, ApprovalStatus.APPROVED));
			} else {
				approvals.add(new Approval(userAuthentication.getPrincipal().toString(),
						requestDetails.getClientId(),value, expiry, ApprovalStatus.DENIED));
			}
		}
		// 客户端所要的权限用户全部拒绝
		if (approvedScopes.isEmpty()) {
			throw new UserDeniedAuthorizationException("用户拒绝授权.");
		} 
		
		approvalStore.addApprovals(approvals);
		
		// 将用户批准的权限保存起来
		requestDetails.setScope(approvedScopes);
		
		// 默认为true
		String val = req.getParameter(Oauth2Utils.user_oauth_approval);
		
		
		requestDetails.setApproved(Boolean.parseBoolean(val));
		return requestDetails;
	}

	private Date computeExpiry() {
		Calendar expiresAt = Calendar.getInstance();
		if (approvalExpirySeconds == -1) { // spring security oauth2: 默认一个月过期
			expiresAt.add(Calendar.MONTH, 1);
		}
		else {
			expiresAt.add(Calendar.SECOND, approvalExpirySeconds);
		}
		return expiresAt.getTime();
	}
	
}
