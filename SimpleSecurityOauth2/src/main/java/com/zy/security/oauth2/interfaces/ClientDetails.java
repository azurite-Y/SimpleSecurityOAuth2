package com.zy.security.oauth2.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.zy.security.core.userdetails.RolePermission;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午4:43:43;
 * @Description: 客户端信息接口
 */
public interface ClientDetails extends Serializable {
	/**
	 *  client id.
	 */
	String getClientId();

	/**
	 * 此客户端可以访问的资源
	 */
	List<String> getResourceIds();

	/**
	 * client secret.
	 */
	String getClientSecret();

	/**
	 * 此客户端是否限于特定范围。如果为false，则身份验证请求的范围将被忽略。
	 */
	boolean isScoped();

	/**
	 * 客户端的作用域。如果客户端未限定作用域，则为空.
	 */
	List<RolePermission> getScope();

	/**
	 * 授权此客户端的授权类型
	 */
	List<String> getAuthorizedGrantTypes();

	/**
	 * 此客户端在“授权代码”访问授权期间使用的预定义重定向URI
	 * @return
	 */
	List<String> getRegisteredRedirectUri();

	/**
	 * 客户端所拥有的权限
	 * @return
	 */
	List<RolePermission> getAuthorities();

	/**
	 * AccessToken过期时间（单位为：秒）
	 * @return
	 */
	Integer getAccessTokenValiditySeconds();
	void setAccessTokenValiditySeconds(Integer seconds);
	
	/**
	 * RefreshToken过期时间（单位为：秒）
	 * @return
	 */
	Integer getRefreshTokenValiditySeconds();
	void setRefreshTokenValiditySeconds(Integer seconds);
	
	/**
	 * 判断此作用域下是否自动授权
	 * @param scope
	 * @return
	 */
	boolean isAutoApprove(String scope);

	/**
	 * 获得附加信息
	 * @return
	 */
	Map<String, Object> getAdditionalInformation();
	
	/**
	 * 添加描述信息
	 * @param key
	 * @param value
	 */
	public void addAdditionalInformation(String key, Object value);
}
