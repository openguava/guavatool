package io.github.openguava.guavatool.shiro.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.LifecycleUtils;

import io.github.openguava.guavatool.core.util.StringUtils;

/**
 * shiro 缓存管理器
 * @author openguava
 *
 */
public abstract class AbstractShiroCacheManager implements CacheManager, Destroyable {

	private final ConcurrentMap<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
	
	/** shiro配置 */
	private AbstractShiroCacheConfig config;
	
	public AbstractShiroCacheConfig getConfig() {
		if(this.config == null) {
			this.config = new AbstractShiroCacheConfig() {
				
			};
		}
		return this.config;
	}
	
	public void setConfig(AbstractShiroCacheConfig config) {
		this.config = config;
	}
	
	/** shiro缓存数据访问*/
	private AbstractShiroCacheDao dao;
	
	public AbstractShiroCacheDao getDao() {
		return this.dao;
	}
	
	public AbstractShiroCacheManager setDao(AbstractShiroCacheDao dao) {
		this.dao = dao;
		return this;
	}
	
	public AbstractShiroCacheManager() {
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Cache name cannot be null or empty.");
        }
		Cache cache = this.caches.get(name);
		if(cache == null) {
			cache = this.createCache(name);
			Cache existing = caches.putIfAbsent(name, cache);
			if (existing != null) {
                cache = existing;
            }
		}
		return cache;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void destroy() throws Exception {
		while (!this.caches.isEmpty()) {
            for (Cache cache : this.caches.values()) {
                LifecycleUtils.destroy(cache);
            }
            caches.clear();
        }
	}
	
	/**
	 * 创建新的缓存实例
	 * @param name
	 * @return
	 * @throws CacheException
	 */
	protected abstract <K, V> Cache<K, V> createCache(String name) throws CacheException;
}
