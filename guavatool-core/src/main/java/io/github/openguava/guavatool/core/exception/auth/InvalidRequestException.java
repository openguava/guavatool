package io.github.openguava.guavatool.core.exception.auth;

/**
 * 无效请求议程
 * @author openguava
 *
 */
public class InvalidRequestException extends AuthenticationException {

	private static final long serialVersionUID = -1L;

	public InvalidRequestException(String message) {
		super(message);
		super.setCode(AuthenticationException.CODE_BAD_REQUEST);
	}
	
	public InvalidRequestException(String message, Throwable e) {
		super(message, e);
		super.setCode(AuthenticationException.CODE_BAD_REQUEST);
	}
}
