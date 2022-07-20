package io.github.openguava.guavatool.spring.auth;

import io.github.openguava.guavatool.core.cache.CacheDao;

/**
 * 用户认证缓存数据访问
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public interface AuthCacheDao extends CacheDao<byte[], byte[]> {
	
}
