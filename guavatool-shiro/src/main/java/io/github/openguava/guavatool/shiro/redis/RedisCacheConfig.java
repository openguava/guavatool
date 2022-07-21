package io.github.openguava.guavatool.shiro.redis;

import io.github.openguava.guavatool.shiro.common.AbstractShiroCacheConfig;

/**
 * redis 配置
 * @author openguava
 *
 */
public class RedisCacheConfig extends AbstractShiroCacheConfig {
	
	public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
	
	public RedisCacheConfig() {
		super.setKeyPrefix(DEFAULT_CACHE_KEY_PREFIX);
	}
}
