package io.github.openguava.guavatool.shiro.handler;

import io.github.openguava.guavatool.spring.constant.AuthConstants;

/**
 * 默认 shiro配置处理
 * @author openguava
 *
 */
public class SimpleShiroConfigHandler implements ShiroConfigHandler {
	
	/** 默认token生成密钥 */
	private static final String DEFAULT_TOKEN_SECRET = "tokenSecret";
	
	public static final String CACHE_AUTHENTICATIONCACHE = "authenticationCache";
	
	public static final String CACHE_AUTHORIZATIONCACHE = "authorizationCache";
	
	/** 会话id cookie名称 */
	protected String sessionIdCookieName = AuthConstants.DEFAULT_SESSION_IDCOOKIE;
	
	@Override
	public String getSessionIdCookieName() {
		return this.sessionIdCookieName;
	}
	
	public void setSessionIdCookieName(String sessionIdCookieName) {
		this.sessionIdCookieName = sessionIdCookieName;
	}
	
	/** 会话id超时时间(秒)  */
	protected int sessionIdTimeout = AuthConstants.DEFAULT_SESSION_TIMEOUT;
	
	public int getSessionIdTimeout() {
		return this.sessionIdTimeout;
	}
	
	public void setSessionIdTimeout(int sessionIdTimeout) {
		this.sessionIdTimeout = sessionIdTimeout;
	}
	
	/** 全局会话超时时间(秒) */
	protected int globalSessionTimeout = AuthConstants.DEFAULT_SESSION_TIMEOUT;
	
	public int getGlobalSessionTimeout() {
		return this.globalSessionTimeout;
	}
	
	public void setGlobalSessionTimeout(int globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}
	
	/** 会话验证定时周期(秒) */
	protected int sessionValidationInterval = AuthConstants.DEFAULT_SESSION_TIMEOUT / 2;
	
	public int getSessionValidationInterval() {
		return this.sessionValidationInterval;
	}
	
	public void setSessionValidationInterval(int sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}
	
	/** redis会话超时时间(秒)  */
	protected int redisSessionTimeout = AuthConstants.DEFAULT_SESSION_TIMEOUT + 60;
	
	public int getRedisSessionTimeout() {
		return this.redisSessionTimeout;
	}
	
	public void setRedisSessionTimeout(int redisSessionTimeout) {
		this.redisSessionTimeout = redisSessionTimeout;
	}
	
	/** redis 缓存超时时间(秒) */
	protected int redisCacheTimeout = AuthConstants.DEFAULT_SESSION_TIMEOUT;
	
	public int getRedisCacheTimeout() {
		return this.redisCacheTimeout;
	}
	
	public void setRedisCacheTimeout(int redisCacheTimeout) {
		this.redisCacheTimeout = redisCacheTimeout;
	}
	
	protected String principalIdFieldName = "id";
	
	@Override
	public String getPrincipalIdFieldName() {
		return this.principalIdFieldName;
	}
	
	protected String authenticationCacheName = CACHE_AUTHENTICATIONCACHE;
	
	@Override
	public String getAuthenticationCacheName() {
		return this.authenticationCacheName;
	}
	
	protected String authorizationCacheName = CACHE_AUTHORIZATIONCACHE;
	
	@Override
	public String getAuthorizationCacheName() {
		return this.authorizationCacheName;
	}
	
	/**
	 * token 应用
	 */
	protected String tokenApp = AuthConstants.DEFAULT_TOKEN_APP;
	
	public String getTokenApp() {
		return this.tokenApp;
	}
	
	public void setTokenApp(String tokenApp) {
		this.tokenApp = tokenApp;
	}
	
	/**
	 * token 名称
	 */
	protected String tokenName = AuthConstants.DEFAULT_TOKEN_NAME;
	
	public String getTokenName() {
		return this.tokenName;
	}
	
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	/**
	 * token 有效时间(秒)
	 */
	protected int tokenTimeout = AuthConstants.DEFAULT_TOKEN_TIMEOUT;
	
	public int getTokenTimeout() {
		return this.tokenTimeout;
	}
	
	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}
	
	/**
	 * token 生成密钥
	 */
	protected String tokenSecret = DEFAULT_TOKEN_SECRET;
	
	public String getTokenSecret() {
		return this.tokenSecret;
	}
	
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
}
