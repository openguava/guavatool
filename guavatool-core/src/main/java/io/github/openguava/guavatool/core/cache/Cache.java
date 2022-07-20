package io.github.openguava.guavatool.core.cache;

import java.io.Serializable;

/**
 * 缓存接口
 * @author openguava
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public interface Cache<K,V> extends Serializable {

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回{@code null}
	 * @param key
	 * @return
	 */
	V get(K key);
	
	/**
	 * 将对象加入到缓存，使用默认失效时长
	 * @param key
	 * @param value
	 * @return
	 */
	V put(K key, V value);
	
	/**
	 * 从缓存中移除对象
	 * @param key
	 * @return
	 */
	V remove(K key);
	
	/**
	 * 清空缓存
	 */
	void clear();
}
