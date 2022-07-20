package io.github.openguava.guavatool.spring.constant;

/**
 * 用户认证常量
 * @author openguava
 *
 */
public class AuthConstants {

	/** 默认token应用 */
	public static final String DEFAULT_TOKEN_APP = "app";
	
	/** 默认token类型 */
	public static final String DEFAULT_TOKEN_TYPE = "login";
	
	/** 默认token设备 */
	public static final String DEFAULT_TOKEN_DEVICE = "pc";
	
	/** 默认token名称 */
	public static final String DEFAULT_TOKEN_NAME = "accessToken";
	
	/** 默认token超时时间(秒) */
	public static final int DEFAULT_TOKEN_TIMEOUT = 7 * 24 * 60 * 60;
	
	/** 默认会话ID名称 */
	public static final String DEFAULT_SESSION_IDCOOKIE = "SESSIONID";
	
	/** 默认会话超时时间(秒)  */
	public static final int DEFAULT_SESSION_TIMEOUT = 30 * 60;
}
