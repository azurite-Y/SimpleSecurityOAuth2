package com.zy.security.oauth2.store;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.zy.security.oauth2.approval.Approval;
/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午22:10:02;
 * @Description: 
 */
import com.zy.security.oauth2.interfaces.ApprovalStore;

/**
 * @author PC
 * 存储于内存中的用户批准信息服务实现
 */
public class InMemoryApprovalStore implements ApprovalStore {
	// 提供userid和clientid作为索引
	private ConcurrentMap<Key, Collection<Approval>> map = new ConcurrentHashMap<Key, Collection<Approval>>();

	@Override
	public boolean addApprovals(Collection<Approval> approvals) {
		for (Approval approval : approvals) {
			Collection<Approval> collection = getApprovals(approval);
			collection.add(approval);
		}
		return true;
	}

	@Override
	public boolean revokeApprovals(Collection<Approval> approvals) {
		boolean success = false;
		for (Approval approval : approvals) {
			Collection<Approval> collection = getApprovals(approval);
			boolean removed = collection.remove(approval);
			if (!removed) {
				success = true;
			}
		}
		return success;
	}

	/**
	 * 提供Approval携带的userid和clientid获得其对应的批准信息集合
	 * @param approval
	 * @return
	 */
	private Collection<Approval> getApprovals(Approval approval) {
		Key key = new Key(approval.getUserId(), approval.getClientId());
		if (!map.containsKey(key)) {
			map.putIfAbsent(key, new HashSet<Approval>());
		}
		return map.get(key);
	}

	@Override
	public Collection<Approval> getApprovals(String userId, String clientId) {
		Approval approval = new Approval();
		approval.setUserId(userId);
		approval.setClientId(clientId);
		return Collections.unmodifiableCollection(getApprovals(approval));
	}
	
	public void clear() {
		map.clear();
	}

	private static class Key {
		String userId;
		String clientId;

		public Key(String userId, String clientId) {
			this.userId = userId;
			this.clientId = clientId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
			result = prime * result + ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (clientId == null) {
				if (other.clientId != null)
					return false;
			}
			else if (!clientId.equals(other.clientId))
				return false;
			if (userId == null) {
				if (other.userId != null)
					return false;
			}
			else if (!userId.equals(other.userId))
				return false;
			return true;
		}
	}
}
