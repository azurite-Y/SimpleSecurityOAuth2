package com.zy.security.oauth2.approval;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午9:53:38;
 * @Description: 
 */
public class Approval {
	public enum ApprovalStatus {
		APPROVED, DENIED;
	}
	
	private String userId;
	private String clientId;
	private String scope;
	private ApprovalStatus status;
	private Date expiresAt;
	private Date lastUpdatedAt;

	public Approval(String userId, String clientId, String scope, int expiresIn, ApprovalStatus status) {
		this(userId, clientId, scope, new Date(), status, new Date());
		Calendar expiresAt = Calendar.getInstance();
		expiresAt.add(Calendar.MILLISECOND, expiresIn);
		setExpiresAt(expiresAt.getTime());
	}

	public Approval(String userId, String clientId, String scope, Date expiresAt, ApprovalStatus status) {
		this(userId, clientId, scope, expiresAt, status, new Date());
	}

	public Approval(String userId, String clientId, String scope, Date expiresAt, ApprovalStatus status,
			Date lastUpdatedAt) {
		setUserId(userId);
		setClientId(clientId);
		setScope(scope);
		setExpiresAt(expiresAt);
		this.status = status;
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public Approval() {}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? "" : userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId == null ? "" : clientId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope == null ? "" : scope;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		if (expiresAt == null) {
			Calendar thirtyMinFromNow = Calendar.getInstance();
			thirtyMinFromNow.add(Calendar.MINUTE, 30);
			expiresAt = thirtyMinFromNow.getTime();
		}
		this.expiresAt = expiresAt;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public boolean isCurrentlyActive() {
		return expiresAt != null && expiresAt.after(new Date());
	}

	public boolean isApproved() {
		return isCurrentlyActive() && status == ApprovalStatus.APPROVED;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

}
