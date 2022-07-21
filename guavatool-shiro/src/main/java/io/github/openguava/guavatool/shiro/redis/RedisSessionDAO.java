package io.github.openguava.guavatool.shiro.redis;

import io.github.openguava.guavatool.shiro.common.AbstractSessionDAO;

/**
 * redis 会话数据访问
 * @author openguava
 *
 */
public class RedisSessionDAO extends AbstractSessionDAO {
	
	public RedisSessionDAO() {
		super.setConfig(new RedisSessionConfig());
	}
}
