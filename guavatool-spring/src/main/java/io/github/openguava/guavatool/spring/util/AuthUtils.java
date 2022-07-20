package io.github.openguava.guavatool.spring.util;

import java.util.Set;

import io.github.openguava.guavatool.spring.auth.AuthLogic;
import io.github.openguava.guavatool.spring.auth.AuthToken;
import io.github.openguava.guavatool.spring.auth.AuthUser;

/**
 * 用户认证工具类
 * @author openguava
 *
 */
public class AuthUtils {

	/** 用户认证逻辑 */
	private static AuthLogic authLogic;
	
	public static AuthLogic getAuthLogic() {
		if(AuthUtils.authLogic == null) {
			AuthUtils.authLogic = SpringUtils.getBean(AuthLogic.class);
		}
		return AuthUtils.authLogic;
	}
	
	/**
	 * 登录
	 */
	public static void login(AuthToken authToken) {
		getAuthLogic().login(authToken);
	}
	
	/**
	 * 注销
	 */
	public static void logout() {
		getAuthLogic().logout();
	}
	
	/**
	 * 注销
	 * @param authToken
	 */
	public static void logout(AuthToken authToken) {
		getAuthLogic().logout(authToken);
	}
	
	/**
	 * 当前会话是否已经登录
	 * @return
	 */
	public static boolean isLogin() {
		return getAuthLogic().isLogin();
	}
	
	/**
	 * 校验用户登录
	 */
	public static void checkLogin() {
		getAuthLogic().checkLogin();
	}
	
	/**
	 * 获取当前登录token
	 * @return
	 */
	public static AuthToken getLoginToken() {
		return getAuthLogic().getLoginToken();
	}
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static AuthUser getLoginUser() {
		return getAuthLogic().getLoginUser();
	}
	
	/** 
 	 * 判断：当前账号是否含有指定权限, 返回true或false 
 	 * @param permission 权限码
 	 * @return 是否含有指定权限
 	 */
 	public static boolean hasPermission(String permission) {
 		return getAuthLogic().hasPermission(permission);
 	}
 	
 	/**
	 * 获取：当前账号的权限码集合 
	 * @return / 
	 */
	public static Set<String> getPermissions() {
		return getAuthLogic().getPermissions();
	}
	
 	/** 
 	 * 判断：当前账号是否拥有指定角色, 返回true或false 
 	 * @param role 角色
 	 * @return / 
 	 */
 	public static boolean hasRole(String role) {
 		return getAuthLogic().hasRole(role);
 	}
 	
 	/**
	 * 获取：当前账号的角色集合 
	 * @return /
	 */
	public static Set<String> getRoles() {
		return getAuthLogic().getRoles();
	}
}
