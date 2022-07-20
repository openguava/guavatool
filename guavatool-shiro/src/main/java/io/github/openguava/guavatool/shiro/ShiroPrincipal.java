package io.github.openguava.guavatool.shiro;

import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.spring.auth.SimpleAuthUser;

/**
 * shiro 认证主体信息
 * @author openguava
 *
 */
public class ShiroPrincipal extends SimpleAuthUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6180589330638601935L;
	
	private String cacheKey;
	
	public String getCacheKey() {
		if(this.cacheKey == null && this.getToken() instanceof ShiroToken) {
			this.cacheKey = StringUtils.format("{}:{}:{}", this.getUserApp(), this.getUserId(), ((ShiroToken)this.getToken()).getCacheKey());
		}
		return this.cacheKey;
	}
	
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
