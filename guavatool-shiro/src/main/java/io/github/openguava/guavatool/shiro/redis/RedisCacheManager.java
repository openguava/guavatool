package io.github.openguava.guavatool.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import io.github.openguava.guavatool.shiro.common.AbstractShiroCacheManager;
import io.github.openguava.guavatool.shiro.common.AbstractShiroCacheConfig;

/**
 * redis 缓存管理器
 * @author openguava
 *
 */
public class RedisCacheManager extends AbstractShiroCacheManager {

	public RedisCacheManager() {
		super.setConfig(new RedisCacheConfig());
	}
	
	@Override
	protected <K, V> Cache<K, V> createCache(String name) throws CacheException {
		AbstractShiroCacheConfig newConfig = new RedisCacheConfig().clone(this.getConfig());
		newConfig.setKeyPrefix(this.getConfig().getKeyPrefix() + name + ":");
		return new RedisCache<>(name, newConfig, this.getDao());
	}
}
