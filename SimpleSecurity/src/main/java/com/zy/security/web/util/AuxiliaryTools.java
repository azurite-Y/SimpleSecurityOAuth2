
package com.zy.security.web.util;

/**
 * @author zy
 * @Date 2019-12-01 周日 下午 14:28:09
 * @Description
 * @version
 */
public final class AuxiliaryTools {
	public static boolean debug;
	/**
	 *  使用什么实现类RolePermission封装权限字符串
	 *  ，false-SimpleRolePermission，ture-PluralisticRolePermission(默认)
	 */
	public static boolean isPluralistic = true;
	/**
	 * csrf凭证表单提交的参数名
	 */
	public static String csrfParameName;
}
