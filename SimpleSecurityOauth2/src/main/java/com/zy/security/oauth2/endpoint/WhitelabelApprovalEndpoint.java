package com.zy.security.oauth2.endpoint;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.oauth2.exception.InvalidRequestException;
import com.zy.security.oauth2.interfaces.RequestDetails;
import com.zy.security.oauth2.utils.Oauth2Utils;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.session.CsrfContextHolder;
import com.zy.security.web.util.AuxiliaryTools;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午2:42:31;
 * @Description: /oauth/confirm_access - 构建授权页面，拦截GET请求
 */
public class WhitelabelApprovalEndpoint extends AbstractEndpoint  {
	
	// null、GET
	public WhitelabelApprovalEndpoint(RequestMatcher postRequest, RequestMatcher getRequest) {
		super(postRequest, getRequest);
	}

	@Override
	public boolean endpoint(HttpServletRequest request,HttpServletResponse response, FilterChain chain) {
		if (super.getRequest != null && super.getRequest.match(request)) {
			String html = createTemplate(request,response);
			super.replyHtml(response, html);
			return true;
		}
		return false;
	}

	private String createTemplate(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			throw new InvalidRequestException("未进行身份验证无法授权.");
		}
		RequestDetails requestDetails = (RequestDetails) session.getAttribute(super.INDEX);
		
		StringBuilder builder = new StringBuilder();
		builder.append("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n");
		builder.append("<head>\n\t<meta charset=\"UTF-8\" />\n");
		builder.append("\t<title>授权确认</title>\n");
//		builder.append("<style>\n");
//		builder.append("</style>\n</style>");
		builder.append("</head>\n<body>\n");
		builder.append("<h3>");
		
		// 请求转发来到的授权页面
		builder.append(requestDetails.getClientId());
		builder.append(" 将获得以下权限，是否同意授权？</h3>\n"); 
		builder.append("<form name='confirmationForm' action='");
		builder.append(Oauth2Utils.oauth_authorize);
		builder.append("' method='post'>\n");
		
		builder.append("<div>\n<ul>\n");
		
		List<RolePermission> scopes = requestDetails.getScope();
		String scope = "";
		for (RolePermission rolePermission : scopes) {
			scope = rolePermission.getValue();
			
			builder.append("<li>\n");
			builder.append(scope);
			builder.append("\n\t<input type='radio' name='");
			builder.append(scope);
			builder.append("' value='true'>同意</input>\n");
			builder.append("\t<input type='radio' name='");
			builder.append(scope);
			builder.append("' value='false'>取消</input>\n");
			builder.append("</li>\n");
		}
		builder.append("</ul>\n");
		
		String csrfBySessionId = CsrfContextHolder.getCsrfBySessionId(request);
		if(null != csrfBySessionId && !csrfBySessionId.isEmpty()) {
			builder.append("\t<input type='hidden' name='");
			builder.append(AuxiliaryTools.csrfParameName);
			builder.append("' value='");
			builder.append(csrfBySessionId);
			builder.append("'/>\n");
		}
		
		// 默认同意授权
		builder.append("\t<input name='");
		builder.append(Oauth2Utils.user_oauth_approval);
		builder.append(" value='true' type='hidden'/>\n");
		
		builder.append("</div>\n");
		builder.append("<label style='margin-top:10px;'>\n\t<input  value='提交' type='submit'/>\n</label>\n");
		
		builder.append("</form>\n");
		builder.append("</body>\n</html>");
		return builder.toString();
	}

}
