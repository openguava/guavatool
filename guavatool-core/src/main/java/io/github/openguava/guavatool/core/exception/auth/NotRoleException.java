package io.github.openguava.guavatool.core.exception.auth;

import io.github.openguava.guavatool.core.util.StringUtils;

/**
 * 未能通过的角色认证异常
 * 
 * @author openguava
 */
public class NotRoleException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotRoleException(String role) {
		super(role);
	}

	public NotRoleException(String[] roles) {
		super(StringUtils.join(roles, ","));
	}
}