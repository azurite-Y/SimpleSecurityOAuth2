
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;

/**
* @author zy
* @Date 2019-11-19 周二 下午 13:53:00
* @Description uri比较
* @version 
*/
public interface RequestMatcher {
	/**
	 * 此静态变量仅适用于 AnyRequestMapping 实现类
	 * ，在使用此变量创建AnyRequestMapping对象之后，
	 * match()方法恒返回true，代表匹配所有请求。
	 */
	public static String anyRequest = "permitAll";
	/**
	 * 此静态变量仅适用于 AnyRequestMapping 实现类
	 * ，在使用此变量创建AnyRequestMapping对象之后，
	 * match()方法恒返回false，代表不匹配所有请求。
	 */
	public static String notAnyRequest = "denyAll";
	/**
	 * uri比较，this的uri需囊括参数的uri <br/>
	 * 即：a：/a/** ,  b：/a/b/** <br/>
	 * a.match(b) = true --》  a的uri囊括b的uri
	 * @param mapping
	 * @return
	 */
	public boolean match(HttpServletRequest request);
}
