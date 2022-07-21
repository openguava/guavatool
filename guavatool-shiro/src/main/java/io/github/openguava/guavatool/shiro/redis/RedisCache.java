package io.github.openguava.guavatool.shiro.redis;

import io.github.openguava.guavatool.shiro.common.AbstractShiroCache;
import io.github.openguava.guavatool.shiro.common.AbstractShiroCacheConfig;
import io.github.openguava.guavatool.shiro.common.AbstractShiroCacheDao;

/**
 * redis缓存
 * 
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public class RedisCache<K, V> extends AbstractShiroCache<K, V> {

	/**
	 * 初始化
	 * @param name
	 * @param config
	 * @param dao
	 */
	public RedisCache(String name, AbstractShiroCacheConfig config, AbstractShiroCacheDao dao) {
		super(name, config, dao);
	}
}
