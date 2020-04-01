package com.zy.security.web.util;

import java.util.ArrayList;
import java.util.List;

import com.zy.security.core.userdetails.PluralisticRolePermission;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.core.userdetails.SimpleRolePermission;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午7:10:12;
 * @Description:
 */
public final class AuthorityUtils {
	/**
	 * 根据权限字符串创建权限集合
	 * @param roles 权限字符串，使用SimpleRolePermission类的权限字符串不可带“ROLE_”前缀
	 * @return
	 */
	public static List<RolePermission> createAuthorityList(String... roles) {
		if (roles == null ) {
			return null;
		}
		
		List<RolePermission> authorities = new ArrayList<>(roles.length);

		for (String role : roles) {
			if(AuxiliaryTools.isPluralistic) {
				authorities.add(new PluralisticRolePermission(role));
			}else {
				authorities.add(new SimpleRolePermission(role));
			}
		}

		return authorities;
	}
}
