package io.github.openguava.guavatool.shiro.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;

import io.github.openguava.guavatool.shiro.ShiroPrincipal;
import io.github.openguava.guavatool.shiro.ShiroToken;
import io.github.openguava.guavatool.shiro.util.ShiroUtils;

public class SimpleShiroAuthHandler implements ShiroAuthHandler {
	
	@Override
	public AuthenticationInfo doAuthenticate(AuthorizingRealm realm, AuthenticationToken token) throws AuthenticationException {
		Object principal = this.getPrincipal(token);
		Object credentials = token.getCredentials();
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, realm.getName());
		return authenticationInfo;
	}
	
	@Override
	public AuthorizationInfo doAuthorize(Object principal) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(this.getRoles(principal));
		authorizationInfo.addStringPermissions(this.getPermissions(principal));
		return authorizationInfo;
	}
	
	@Override
	public Object getAuthenticationTokenCacheKey(AuthenticationToken token) {
		ShiroToken shiroToken = ShiroUtils.getShiroToken(token);
		if(shiroToken != null) {
			return shiroToken.getCacheKey();
		}
		return null;
	}
	
	@Override
	public Object getAuthenticationPrincipalCacheKey(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			ShiroPrincipal shiroPrincipal = (ShiroPrincipal)principal;
			ShiroToken shiroToken = ShiroUtils.getShiroToken(shiroPrincipal);
			if(shiroToken != null) {
				return shiroToken.getCacheKey();
			}
		}
		return null;
	}
	
	@Override
	public Object getAuthorizationCacheKey(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getCacheKey();
		}
		return null;
	}
	
	/**
	 * 获取认证主体
	 * @param token
	 * @return
	 */
	protected Object getPrincipal(AuthenticationToken token) throws AuthenticationException {
		return token.getPrincipal();
	}
	
	/**
	 * 获取角色
	 * @param principal
	 * @return
	 */
	protected Collection<String> getRoles(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getRoles();
		}
		return new ArrayList<>();
	}
	
	/**
	 * 获取角色
	 * @param principal
	 * @return
	 */
	protected Collection<String> getPermissions(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getPermissions();
		}
		return new ArrayList<>();
	}
}
