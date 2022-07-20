package io.github.openguava.guavatool.shiro.handler;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * shiro 认证处理器
 * @author openguava
 *
 */
public interface ShiroAuthHandler {

	/**
	 * shiro认证
	 * @param realm
	 * @param token
	 * @return
	 */
	AuthenticationInfo doAuthenticate(AuthorizingRealm realm, AuthenticationToken token);
	
	/**
	 * shiro 授权
	 * @param principal shiro认证主体
	 */
	AuthorizationInfo doAuthorize(Object principal);
	
	/**
	 * 获取认证缓存token key
	 * @param token
	 * @return
	 */
	Object getAuthenticationTokenCacheKey(AuthenticationToken token);
	
	/**
	 * 获取认证缓存principal key
	 * @param token
	 * @return
	 */
	Object getAuthenticationPrincipalCacheKey(Object principal);
	
	/**
	 * 获取授权缓存key
	 * @param principal
	 * @return
	 */
	Object getAuthorizationCacheKey(Object principal);
}
