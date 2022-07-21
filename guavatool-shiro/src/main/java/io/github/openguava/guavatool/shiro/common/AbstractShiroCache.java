package io.github.openguava.guavatool.shiro.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import io.github.openguava.guavatool.core.exception.SerializationException;
import io.github.openguava.guavatool.core.util.LogUtils;
import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.shiro.exception.CacheManagerPrincipalIdNotAssignedException;
import io.github.openguava.guavatool.shiro.exception.PrincipalIdNullException;
import io.github.openguava.guavatool.shiro.exception.PrincipalInstanceException;
import io.github.openguava.guavatool.shiro.serializer.StringSerializer;

/**
 * shiro 缓存
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractShiroCache<K, V> implements Cache<K, V> {

	protected String name;
	
	protected AbstractShiroCacheConfig config;
	
	protected AbstractShiroCacheDao dao;
	
	/**
	 * 初始化
	 * @param name
	 * @param config
	 * @param dao
	 */
	public AbstractShiroCache(String name, AbstractShiroCacheConfig config, AbstractShiroCacheDao dao) {
		this.name = name;
		// config
		if (config == null) {
			throw new IllegalArgumentException("cacheConfig cannot be null.");
		}
		this.config = config;
		if (this.config.getKeyPrefix() == null) {
			throw new IllegalArgumentException("keySerializer cannot be null.");
		}
		if (this.config.getValueSerializer() == null) {
			throw new IllegalArgumentException("valueSerializer cannot be null.");
		}
		// dao
		if (dao == null) {
			throw new IllegalArgumentException("cacheDao cannot be null.");
		}
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		if (key == null) {
			return null;
		}
		LogUtils.debug(this.getClass(), "shiroCache get key [" + key + "]");
		try {
			byte[] rawKey = this.getRawKey(key);
			byte[] rawValue = this.dao.get(rawKey);
			if (rawValue == null) {
				return null;
			}
			V value = (V)this.config.getValueSerializer().deserialize(rawValue);
			return value;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		if (key == null) {
			LogUtils.warn(this.getClass(),
					"Saving a null key is meaningless, return value directly without call Redis.");
			return value;
		}
		LogUtils.debug(this.getClass(), "shiroCache put key [" + key + "]");
		try {
			byte[] rawKey = this.getRawKey(key);
			byte[] rawValue = this.getRawValue(value);
			this.dao.put(rawKey, rawValue, this.config.getExpire());
			return value;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(K key) throws CacheException {
		if (key == null) {
			return null;
		}
		LogUtils.debug(this.getClass(), "shiroCache remove key [" + key + "]");
		try {
			byte[] rawKey = this.getRawKey(key);
			byte[] rawValue = this.dao.get(rawKey);
			V previous = (V) this.config.getValueSerializer().deserialize(rawValue);
			this.dao.remove(rawKey);
			return previous;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}
	
	@Override
	public void clear() throws CacheException {
		LogUtils.debug(this.getClass(), "ShiroCache clear cache");
		Set<byte[]> keys = null;
		try {
			keys = this.dao.keys(this.config.getKeySerializer().serialize(this.config.getKeyPrefix() + "*"));
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "ShiroCache get keys error", e);
		}
		if (keys == null || keys.isEmpty()) {
			return;
		}
		for (byte[] key : keys) {
			this.dao.remove(key);
		}
	}
	
	/**
	 * get all authorization key-value quantity
	 * 
	 * @return key-value size
	 */
	@Override
	public int size() {
		Long longSize = 0L;
		try {
			longSize = new Long(this.dao.size(this.config.getKeySerializer().serialize(this.config.getKeyPrefix() + "*")));
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "ShiroCache get size error", e);
		}
		return longSize.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		Set<byte[]> keys = null;
		try {
			keys = this.dao.keys(this.config.getKeySerializer().serialize(this.config.getKeyPrefix() + "*"));
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "ShiroCache get keys error", e);
			return Collections.emptySet();
		}
		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}
		Set<K> convertedKeys = new HashSet<>();
		for (byte[] key : keys) {
			try {
				convertedKeys.add((K)this.config.getKeySerializer().deserialize(key));
			} catch (SerializationException e) {
				LogUtils.error(this.getClass(), "ShiroCache deserialize keys error", e);
			}
		}
		return convertedKeys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Set<byte[]> keys = null;
		try {
			keys = this.dao.keys(this.config.getKeySerializer().serialize(this.config.getKeyPrefix()+ "*"));
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "ShiroCache get values error", e);
			return Collections.emptySet();
		}
		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}
		List<V> values = new ArrayList<>(keys.size());
		for (byte[] key : keys) {
			V value = null;
			try {
				value = (V)this.config.getValueSerializer().deserialize(this.dao.get(key));
			} catch (SerializationException e) {
				LogUtils.error(this.getClass(), "ShiroCache deserialize values error", e);
			}
			if (value != null) {
				values.add(value);
			}
		}
		return Collections.unmodifiableList(values);
	}
	
	/**
	 * 获取原始 key
	 * @param key
	 * @return
	 */
	protected byte[] getRawKey(K key) {
		Object redisCacheKey = this.getCacheKey(key);
		byte[] rawKey = this.config.getKeySerializer().serialize(redisCacheKey.toString());
		return rawKey;
	}
	
	/**
	 * 获取原始 value
	 * @param value
	 * @return
	 */
	protected byte[] getRawValue(V value) {
		byte[] rawValue = value != null ? this.config.getValueSerializer().serialize(value) : null;
		return rawValue;
	}

	protected Object getCacheKey(K key) {
		if (key == null) {
			return null;
		}
		if (this.config.getKeySerializer() instanceof StringSerializer) {
			return this.config.getKeyPrefix() + this.getStringCacheKey(key);
		}
		return key;
	}

	private String getStringCacheKey(K key) {
		String redisKey;
		if (key instanceof PrincipalCollection) {
			redisKey = getKeyFromPrincipalIdField((PrincipalCollection) key);
		} else {
			redisKey = key.toString();
		}
		return redisKey;
	}

	/**
	 * 根据认证主体id字段获取key
	 * @param key
	 * @return
	 */
	private String getKeyFromPrincipalIdField(PrincipalCollection key) {
		Object principalObject = key.getPrimaryPrincipal();
		Method pincipalIdGetter = getPrincipalIdGetter(principalObject);
		return getIdObj(principalObject, pincipalIdGetter);
	}

	/**
	 * 获取认证主体id值
	 * @param principalObject
	 * @param pincipalIdGetter
	 * @return
	 */
	private String getIdObj(Object principalObject, Method pincipalIdGetter) {
		String redisKey;
		try {
			Object idObj = pincipalIdGetter.invoke(principalObject);
			if (idObj == null) {
				throw new PrincipalIdNullException(principalObject.getClass(), this.config.getPrincipalIdFieldName());
			}
			redisKey = idObj.toString();
		} catch (IllegalAccessException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.config.getPrincipalIdFieldName(), e);
		} catch (InvocationTargetException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.config.getPrincipalIdFieldName(), e);
		}
		return redisKey;
	}

	/**
	 * 获取认证主体id get函数
	 * @param principalObject
	 * @return
	 */
	private Method getPrincipalIdGetter(Object principalObject) {
		Method pincipalIdGetter = null;
		String principalIdMethodName = this.getPrincipalIdMethodName();
		try {
			pincipalIdGetter = principalObject.getClass().getMethod(principalIdMethodName);
		} catch (NoSuchMethodException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.config.getPrincipalIdFieldName());
		}
		return pincipalIdGetter;
	}

	/**
	 * 获取认证主体id字段函数名
	 * @return
	 */
	private String getPrincipalIdMethodName() {
		if (StringUtils.isEmpty(this.config.getPrincipalIdFieldName())) {
			throw new CacheManagerPrincipalIdNotAssignedException();
		}
		return "get" + this.config.getPrincipalIdFieldName().substring(0, 1).toUpperCase() + this.config.getPrincipalIdFieldName().substring(1);
	}
}
