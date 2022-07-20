package io.github.openguava.guavatool.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import io.github.openguava.guavatool.core.serializer.Serializer;
import io.github.openguava.guavatool.core.util.LogUtils;
import io.github.openguava.guavatool.shiro.ShiroCacheDao;
import io.github.openguava.guavatool.shiro.redis.serializer.ObjectSerializer;
import io.github.openguava.guavatool.shiro.redis.serializer.StringSerializer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * redis 缓存管理器
 * @author openguava
 *
 */
public class RedisCacheManager implements CacheManager {

	public static final int DEFAULT_EXPIRE = 1800;
	
	public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
	
	public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "id";

	// fast lookup by name map
	private final ConcurrentMap<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
	
	private Serializer<String, byte[]> keySerializer = new StringSerializer();
	
	public Serializer<String, byte[]> getKeySerializer() {
		return this.keySerializer;
	}

	public void setKeySerializer(Serializer<String, byte[]> keySerializer) {
		this.keySerializer = keySerializer;
	}
	
	private Serializer<Object, byte[]> valueSerializer = new ObjectSerializer();
	
	public Serializer<Object, byte[]> getValueSerializer() {
		return this.valueSerializer;
	}

	public void setValueSerializer(Serializer<Object, byte[]> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	/** shiro缓存数据访问*/
	private ShiroCacheDao cacheDao;
	
	public ShiroCacheDao getCacheDao() {
		return cacheDao;
	}
	
	public RedisCacheManager setCacheDao(ShiroCacheDao cacheDao) {
		this.cacheDao = cacheDao;
		return this;
	}

	/** 默认有效期(秒) */
	private int expire = DEFAULT_EXPIRE;
	
	public int getExpire() {
		return this.expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	/** redis缓存key前缀 */
	private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;
	
	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	/** 认证主体字段名称 */
	private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;
	
	public String getPrincipalIdFieldName() {
		return this.principalIdFieldName;
	}

	public void setPrincipalIdFieldName(String principalIdFieldName) {
		this.principalIdFieldName = principalIdFieldName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		LogUtils.debug(this.getClass(), "get cache, name=" + name);

		Cache cache = caches.get(name);
		// 自动创建缓存对象
		if (cache == null) {
			cache = new RedisCache<K, V>(this.cacheDao, keySerializer, valueSerializer, keyPrefix + name + ":", expire, principalIdFieldName);
			caches.put(name, cache);
		}
		return cache;
	}
}
