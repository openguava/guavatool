package io.github.openguava.guavatool.spring.auth;

/**
 * 认证用户信息
 * @author openguava
 *
 */
public interface AuthUser {

	String getUserId();
	
	String getUsername();
	
	String getUserType();
	
	String getOrgId();
	
	String getOrgName();
	
	String getDeptId();
	
	String getDeptName();
}
