package io.github.openguava.guavatool.core.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存对象
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public class CacheObj<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 键 */
	protected final K key;
	
	/**
	 * 获取键
	 *
	 * @return 键
	 */
	public K getKey() {
		return this.key;
	}
	
	/** 值 */
	protected final V value;
	
	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public V getValue() {
		return this.value;
	}

	/**
	 * 上次访问时间
	 */
	protected volatile long lastAccess;
	
	/**
	 * 获取上次访问时间
	 *
	 * @return 上次访问时间
	 */
	public long getLastAccess() {
		return this.lastAccess;
	}
	
	/**
	 * 访问次数
	 */
	protected AtomicLong accessCount = new AtomicLong();
	
	/**
	 * 对象存活时长，0表示永久存活
	 */
	protected final long ttl;

	/**
	 * 获取对象存活时长，即超时总时长，0表示无限
	 *
	 * @return 对象存活时长
	 */
	public long getTtl() {
		return this.ttl;
	}
	
	/**
	 * 构造
	 *
	 * @param key 键
	 * @param obj 值
	 * @param ttl 超时时长
	 */
	protected CacheObj(K key, V obj, long ttl) {
		this.key = key;
		this.value = obj;
		this.ttl = ttl;
		this.lastAccess = System.currentTimeMillis();
	}

	/**
	 * 获取过期时间，返回{@code null}表示永不过期
	 *
	 * @return 此对象的过期时间，返回{@code null}表示永不过期
	 */
	public Date getExpiredTime(){
		if(this.ttl > 0){
			return new Date(this.lastAccess + this.ttl);
		}
		return null;
	}

	/**
	 * 判断是否过期
	 *
	 * @return 是否过期
	 */
	protected boolean isExpired() {
		if (this.ttl > 0) {
			// 此处不考虑时间回拨
			return (System.currentTimeMillis() - this.lastAccess) > this.ttl;
		}
		return false;
	}

	/**
	 * 获取值
	 *
	 * @param isUpdateLastAccess 是否更新最后访问时间
	 * @return 获得对象
	 */
	protected V get(boolean isUpdateLastAccess) {
		if (isUpdateLastAccess) {
			this.lastAccess = System.currentTimeMillis();
		}
		this.accessCount.getAndIncrement();
		return this.value;
	}
	
	@Override
	public String toString() {
		return "CacheObj [key=" + key + ", obj=" + this.value + ", lastAccess=" + lastAccess + ", accessCount=" + accessCount + ", ttl=" + ttl + "]";
	}
}
