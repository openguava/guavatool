package io.github.openguava.guavatool.spring.auth;

/**
 * 认证 token信息
 * @author openguava
 *
 */
public interface AuthToken {

	String getTokenApp();
	
	String getTokenType();
	
	String getTokenDevice();
	
	String getTokenName();
	
	Object getTokenValue();
	
	int getTokenTimeout();
	
	String getTokenSubject();
}
