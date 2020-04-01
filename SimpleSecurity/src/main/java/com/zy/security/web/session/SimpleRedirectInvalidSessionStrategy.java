
package com.zy.security.web.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.web.interfaces.InvalidSessionStrategy;
import com.zy.security.web.interfaces.RequestCache;
import com.zy.security.web.util.DefaultRequestCache;

/**
* @author zy
* @Date 2019-11-26 周二 上午 00:08:37
* @Description 
* @version 
*/
public final class SimpleRedirectInvalidSessionStrategy implements InvalidSessionStrategy {
	private final String destinationUrl;
	private RequestCache requestCache = new DefaultRequestCache();
	
	public SimpleRedirectInvalidSessionStrategy(String destinationUrl) {
		super();
		this.destinationUrl = destinationUrl;
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest req, HttpServletResponse resp) {
		if(!resp.isCommitted()) {
			try {
				this.requestCache.saveRequest(req);
				resp.sendRedirect(destinationUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
