package io.github.openguava.guavatool.spring.auth;

import java.util.Collection;

/**
 * 认证处理器
 * @author openguava
 *
 */
public interface AuthHandler {

	/**
	 * 获取用户信息
	 * @param authToken
	 * @return
	 */
	AuthUser getUser(AuthToken authToken);
	
	/**
	 * 获取角色集合
	 * @param authUser
	 * @return
	 */
	Collection<String> getRoles(AuthUser authUser); 
	
	/**
	 * 获取权限集合
	 * @param authUser
	 * @return
	 */
	Collection<String> getPermissions(AuthUser authUser);
}
