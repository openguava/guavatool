package io.github.openguava.guavatool.spring.auth;

import java.util.Set;

/**
 * 用户认证逻辑
 * @author openguava
 *
 */
public interface AuthLogic {

	/**
	 * 登录
	 */
	void login(AuthToken authToken);
	
	/**
	 * 注销
	 */
	void logout();
	
	/**
	 * 注销
	 * @param authToken
	 */
	void logout(AuthToken authToken);
	
	/**
	 * 当前会话是否已经登录
	 * @return
	 */
	boolean isLogin();
	
	/**
	 * 检验当前会话是否已经登录，如未登录，则抛出异常
	 */
	void checkLogin();
	
	/**
	 * 获取当前登录token
	 * @return
	 */
	AuthToken getLoginToken();
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	AuthUser getLoginUser();
	
	/** 
 	 * 判断：当前账号是否含有指定权限, 返回true或false 
 	 * @param permission 权限码
 	 * @return 是否含有指定权限
 	 */
 	boolean hasPermission(String permission);
 	
 	/**
	 * 获取：当前账号的权限码集合 
	 * @return / 
	 */
	Set<String> getPermissions();
 	
 	/** 
 	 * 判断：当前账号是否拥有指定角色, 返回true或false 
 	 * @param role 角色
 	 * @return / 
 	 */
 	boolean hasRole(String role);
 	
 	/**
	 * 获取：当前账号的角色集合 
	 * @return /
	 */
	Set<String> getRoles();
}
