package io.github.openguava.guavatool.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.openguava.guavatool.shiro.handler.ShiroAuthHandler;
import io.github.openguava.guavatool.shiro.util.ShiroUtils;

public class ShiroRealm extends AuthorizingRealm {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		ShiroAuthHandler shiroAuthHandler = ShiroUtils.getShiroAuthHandler();
		AuthenticationInfo authenticationInfo = shiroAuthHandler.doAuthenticate(this, token);
		return authenticationInfo;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroAuthHandler shiroAuthHandler = ShiroUtils.getShiroAuthHandler();
		Object principal = principals.getPrimaryPrincipal();
		AuthorizationInfo authorizationInfo = shiroAuthHandler.doAuthorize(principal);
		return authorizationInfo;
	}

	@Override
	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
		ShiroAuthHandler shiroAuthHandler = ShiroUtils.getShiroAuthHandler();
		Object cacheKey = shiroAuthHandler.getAuthenticationTokenCacheKey(token);
		if(cacheKey != null) {
			return cacheKey;
		}
		return super.getAuthenticationCacheKey(token);
	}
	
	@Override
	protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
		ShiroAuthHandler shiroAuthHandler = ShiroUtils.getShiroAuthHandler();
		Object principal = principals.getPrimaryPrincipal();
		Object cacheKey = shiroAuthHandler.getAuthenticationPrincipalCacheKey(principal);
		if(cacheKey != null) {
			return cacheKey;
		}
		return super.getAuthenticationCacheKey(principals);
	}
	
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		Object principal = principals.getPrimaryPrincipal();
		Object cacheKey = this.getAuthorizationCacheKey(principal);
		if(cacheKey != null) {
			return cacheKey;
		}
		return super.getAuthorizationCacheKey(principals);
	}
	
	/**
	 * 获取授权主体信息 缓存 key
	 * @param principal
	 * @return
	 */
	protected Object getAuthorizationCacheKey(Object principal) {
		ShiroAuthHandler shiroAuthHandler = ShiroUtils.getShiroAuthHandler();
		Object cacheKey = shiroAuthHandler.getAuthorizationCacheKey(principal);
		if(cacheKey != null) {
			return cacheKey;
		}
		return null;
	}
	
	@Override
	protected void doClearCache(PrincipalCollection principals) {
		super.doClearCache(principals);
	}
	
	@Override
	public boolean supports(AuthenticationToken token) {
		return super.supports(token) || AuthenticationToken.class.isInstance(token);
	}
	
	/**
	 * 清理认证缓存
	 * @param token
	 * @return
	 */
	public boolean clearAuthenticationCacheBy(AuthenticationToken token) {
		Object key = this.getAuthenticationCacheKey(token);
		return this.clearAuthenticationCacheByKey(key);
	}
	
	/**
	 * 清理认证 缓存
	 * @param token
	 * @return
	 */
	public boolean clearAuthenticationCacheByKey(Object key) {
		if(key == null) {
			return false;
		}
		try {
			Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
			if(cache != null) {
				cache.remove(key);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 清理授权缓存
	 * @param principal
	 * @return
	 */
	public boolean clearAuthorizationCache(Object principal) {
		Object key = this.getAuthorizationCacheKey(principal);
		return this.clearAuthorizationCacheByKey(key);
	}
	
	/**
	 * 清理授权缓存
	 * @param key
	 * @return
	 */
	public boolean clearAuthorizationCacheByKey(Object key) {
		if(key == null) {
			return false;
		}
		try {
			Cache<Object, AuthorizationInfo> cache = this.getAuthorizationCache();
			if(cache != null) {
				cache.remove(key);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
}
