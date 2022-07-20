package io.github.openguava.guavatool.core.exception.auth;

import io.github.openguava.guavatool.core.util.StringUtils;

/**
 * 未能通过的权限认证异常
 * 
 * @author openguava
 */
public class NotPermissionException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public NotPermissionException(String permission) {
		super(permission);
	}

	public NotPermissionException(String[] permissions) {
		super(StringUtils.join(permissions, ","));
		super.setCode(AuthenticationException.CODE_FORBIDDEN);
	}
}