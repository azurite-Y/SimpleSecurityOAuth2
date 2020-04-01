package com.zy.security.oauth2.interfaces;

import java.util.Collection;

import com.zy.security.oauth2.approval.Approval;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午7:51:12;
 * @Description: 存储用户授权结果的存储库方法接口
 */
public interface ApprovalStore {
	/**
	 * 存储Approved
	 * @param approvals
	 * @return
	 */
	public boolean addApprovals(Collection<Approval> approvals);

	/**
	 * 撤销所传集合中的批准信息，返回true则代表撤销完成
	 * @param approvals
	 * @return
	 */
	public boolean revokeApprovals(Collection<Approval> approvals);

	/**
	 * 提供userid和clientid查找批准信息
	 * @param userId
	 * @param clientId
	 * @return
	 */
	public Collection<Approval> getApprovals(String userId, String clientId);
}
