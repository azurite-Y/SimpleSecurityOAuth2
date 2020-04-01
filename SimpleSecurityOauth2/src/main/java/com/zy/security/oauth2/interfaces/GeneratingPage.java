package com.zy.security.oauth2.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午10:19:46;
 * @Description: 构造页面基本接口
 */
public interface GeneratingPage {
	public StringBuilder write(HttpServletRequest request,HttpServletResponse response);
}
