package io.github.openguava.guavatool.core.cache;

import java.util.Set;

/**
 * 缓存数据访问
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public interface CacheDao<K, V> {
	
	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回{@code null}
	 * @param key
	 * @return
	 */
	V get(K key);
	
	/**
	 * 将对象加入到缓存，使用指定失效时长
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	V put(K key, V value, int expire);
	
	/**
	 * 从缓存中移除对象
	 * @param key
	 * @return
	 */
	void remove(K key);
	
	/**
	 * 缓存的键集合
	 * @param pattern
	 * @return
	 */
	Set<K> keys(K pattern);
	
	/**
	 * 缓存的对象数量
	 * @return
	 */
	int size(K pattern);
	
	/**
	 * 清空缓存
	 */
	void clear();
}
