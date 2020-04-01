
package com.zy.security.web.config;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.SecurityContextPersistenceFilter;

/**
* @author zy
* @Date 2019-11-29 周五 下午 14:37:41
* @Description SecurityContextPersistenceFilter配置类
* @version 
*/
public class SecurityContextPersistenceConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	public SecurityContextPersistenceConfiguration(HttpSecurity securityBuilder) {
		super(securityBuilder);
	}

	private SecurityContextPersistenceFilter createFilter() {
		return new SecurityContextPersistenceFilter();
	}
	
	@Override
	public void config() {
		http.getFilters().add(createFilter());
	}
}
