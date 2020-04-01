
package com.zy.security.web.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.zy.security.core.userdetails.PluralisticRolePermission;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.core.userdetails.SimpleRolePermission;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.AnonymousAuthenticationFilter;
import com.zy.security.web.util.AuxiliaryTools;

/**
* @author zy
* @Date 2019-11-24 周日 下午 16:29:43
* @Description
* @version 
*/
public class AnonymousConfiguration  extends AbstractHttpConfigurer<HttpSecurity>{
	// 匿名Token的key
	private String AnnoKey;
	private Object principal = "anonymousUser";
	private List<RolePermission> authorities = new ArrayList<>();
	private String[] authCache;
	
	// 是否区分大小写,设置为true则代表不区分，在解析时将会把权限字符串转为小写,默认为false
	public static boolean case_sensitiver;
	
	// 权限字符串前缀
	private static String prefix = "Role_";
	
	public AnonymousConfiguration(HttpSecurity securityBuilder) {
		super(securityBuilder);
	}
	
	
	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */
	
	/**
	 * 设置匿名用户的用户主体标识，默认为“anonymousUser”
	 * @param principal
	 * @return
	 */
	public AnonymousConfiguration principal(Object principal) {
		this.principal = principal;
		return this;
	}
	/**
	 * 设置匿名用户所拥有的权限
	 * @param authorities
	 * @return
	 */
	public AnonymousConfiguration authorities(List<RolePermission> authorities) {
		this.authorities = authorities;
		return this;
	}
	/**
	 * 设置匿名用户所拥有的权限
	 * @param authorities
	 * @return
	 */
	public AnonymousConfiguration authorities(String... authorities) {
		this.authCache = authorities;
		return this;
	}
	
	/**
	 * 使用什么实现类RolePermission封装权限字符串<br/>
	 * false-{@linkplain SimpleRolePermission}，true-{@linkplain PluralisticRolePermission}(默认)
	 * <p>改变此值将应用到框架中的所有 {@linkplain RolePermission} 实例上</p>
	 * @param isSimple true or false
	 * @return
	 */
	public AnonymousConfiguration RolePermissionType(boolean isSimple) {
		if(isSimple) {
			AuxiliaryTools.isPluralistic = isSimple;
		}
		return this;
	}

	/**
	 * 是否区分大小写,设置为true则代表不区分，在解析时将会把权限字符串转为小写,默认为false<br/>
	 * <p>改变此值将应用到框架中的所有 {@linkplain RolePermission} 实例上</p>
	 * @param  case_sensit - true或false
	 * @return
	 */
	public AnonymousConfiguration CaseSensit(boolean case_sensit) {
		if(case_sensit) {
			AnonymousConfiguration.case_sensitiver = case_sensit;
		}
		return this;
	}
	
	/**
	 * 设置权限字符串的前缀，默认为“ROLE_”.
	 * <p>改变此值将应用到框架中的所有 {@linkplain SimpleRolePermission} 实例上</p>
	 * @param prefix
	 * @return
	 */
	public AnonymousConfiguration CaseSensit(String prefix) {
		if(!AnonymousConfiguration.prefix.equals(prefix)) {
			AnonymousConfiguration.prefix = prefix;
		}
		return this;
	}
	
	
	/**
	 * 获得匿名用户的权限集合
	 * @return
	 */
	private List<RolePermission> getRolePermission(){
		if(this.authorities .isEmpty()) { // 用户未设置则执行默认的配置
			if(AuxiliaryTools.isPluralistic) {
				this.authorities.add(new PluralisticRolePermission("guest:anonymous",AnonymousConfiguration.case_sensitiver) );
			}else {
				this.authorities.add(new SimpleRolePermission("Role_anonymous",AnonymousConfiguration.case_sensitiver) );
			}
		}else { // 用户已配置则转化
			for (String role : authCache) {
				if(role.isEmpty()) {
					continue ;
				}
				
				if(AuxiliaryTools.isPluralistic) {
					this.authorities.add(new PluralisticRolePermission(role,AnonymousConfiguration.case_sensitiver) );
				}else {
					this.authorities.add(new SimpleRolePermission(role,AnonymousConfiguration.case_sensitiver) );
				}
				
			}
		}
		return authorities;
	}
	
	private AnonymousAuthenticationFilter createAnonymousAuthenticationFilter() {
		this.AnnoKey = super.http.getAnnoKey();
		// 防止未调用AuthenticationManagerBuilder的authenticationManagerBuilder方法而导致AnnoKey为null
		if (this.AnnoKey == null || this.AnnoKey.isEmpty()) {
			this.AnnoKey = UUID.randomUUID().toString();
		}
		return new AnonymousAuthenticationFilter(this.AnnoKey, principal, getRolePermission());
	}
	
	@Override
	public void config() {
		super.http.getFilters().add(createAnonymousAuthenticationFilter());
	}
}
