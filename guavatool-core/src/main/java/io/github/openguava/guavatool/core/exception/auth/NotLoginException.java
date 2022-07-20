package io.github.openguava.guavatool.core.exception.auth;

/**
 * 未能通过的登录认证异常
 * 
 * @author openguava
 */
public class NotLoginException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public NotLoginException(String message) {
		super(message);
		super.setCode(AuthenticationException.CODE_UNAUTHORIZED);
	}
	
	public NotLoginException(String message, Throwable e) {
		super(message, e);
		super.setCode(AuthenticationException.CODE_UNAUTHORIZED);
	}
}