package io.github.openguava.guavatool.core.exception.auth;

import io.github.openguava.guavatool.core.enums.HttpStatus;

/**
 * 认证异常
 * @author openguava
 *
 */
public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/** 成功状态码 */
	public static final int CODE_SUCCESS = HttpStatus.OK.getValue();
	
	/** 错误状态码 */
	public static final int CODE_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.getValue();
	
	/** 无效请求状态码 */
	public static final int CODE_BAD_REQUEST = HttpStatus.BAD_REQUEST.getValue();
	
	/** 无权限状态码 */
	public static final int CODE_FORBIDDEN = HttpStatus.FORBIDDEN.getValue();
	
	/** 未登录状态码 */
	public static final int CODE_UNAUTHORIZED = HttpStatus.UNAUTHORIZED.getValue();
	
	/**
	 * 底层code码
	 */
	private int code = HttpStatus.OK.getValue();
	
	public int getCode() {
		return this.code;
	}
	
	public AuthenticationException setCode(int code) {
		this.code = code;
		return this;
	}
	
	public AuthenticationException(int code, String message) {
        super(message);
		setCode(code);
    }
	
	public AuthenticationException(String message) {
        super(message);
    }
	
	public AuthenticationException(Throwable e) {
        super(e);
    }
	
	public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
