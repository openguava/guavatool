package io.github.openguava.guavatool.shiro;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationToken;

import io.github.openguava.guavatool.core.exception.auth.NotLoginException;
import io.github.openguava.guavatool.shiro.util.ShiroUtils;
import io.github.openguava.guavatool.spring.auth.AuthToken;
import io.github.openguava.guavatool.spring.auth.AuthUser;
import io.github.openguava.guavatool.spring.auth.logic.AbstractAuthLogic;

/**
 * shiro 认证逻辑
 * @author openguava
 *
 */
public class ShiroAuthLogic extends AbstractAuthLogic {

	@Override
	public void login(AuthToken authToken) {
		ShiroUtils.login(ShiroUtils.getSubject(), (AuthenticationToken)authToken);
	}

	@Override
	public void logout() {
		ShiroUtils.logout(ShiroUtils.getSubject());
	}

	@Override
	public void logout(AuthToken authToken) {
		ShiroRealm shiroRealm = ShiroUtils.getShiroRealm();
		if(shiroRealm != null) {
			shiroRealm.clearAuthenticationCacheBy((AuthenticationToken)authToken);
		}
	}

	@Override
	public boolean isLogin() {
		return ShiroUtils.isLogin();
	}

	@Override
	public void checkLogin() {
		if(!ShiroUtils.isLogin()) {
			throw new NotLoginException("用户未登录！");
		}
	}

	@Override
	public AuthToken getLoginToken() {
		return ShiroUtils.getShiroToken(ShiroUtils.getSubject());
	}

	@Override
	public AuthUser getLoginUser() {
		return ShiroUtils.getShiroPrincipal();
	}

	@Override
	public boolean hasPermission(String permission) {
		return ShiroUtils.isPermitted(ShiroUtils.getSubject(), permission);
	}

	@Override
	public Set<String> getPermissions() {
		return ShiroUtils.getShiroPrincipal().getPermissions();
	}

	@Override
	public boolean hasRole(String role) {
		return ShiroUtils.hasRole(ShiroUtils.getSubject(), role);
	}

	@Override
	public Set<String> getRoles() {
		return ShiroUtils.getShiroPrincipal().getRoles();
	}
}
