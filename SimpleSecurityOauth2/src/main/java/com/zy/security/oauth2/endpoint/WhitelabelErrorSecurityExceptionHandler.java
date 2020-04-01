package com.zy.security.oauth2.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.oauth2.interfaces.SecurityExceptionHandler;
import com.zy.security.oauth2.utils.ResponseUtils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:42:44;
 * @Description: 根据异常构建错误页面
 */
public class WhitelabelErrorSecurityExceptionHandler extends ResponseUtils implements SecurityExceptionHandler  {
	
	private final String ERROR = "<html><body><h2>OAuth Error</h2><p>%errorSummary%</p></body></html>";
	
	@Override
	public void handler(HttpServletRequest req , HttpServletResponse resp , Exception e) {
		String errorContent = ERROR.replace("%errorSummary%", e.getMessage());
		super.replyHtml(resp, errorContent);
	}
}
