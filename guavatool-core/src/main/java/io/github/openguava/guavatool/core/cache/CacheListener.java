package io.github.openguava.guavatool.core.cache;

/**
 * 缓存监听，用于实现缓存操作时的回调监听，例如缓存对象的移除事件等
 * @author openguava
 *
 * @param <K>
 * @param <V>
 */
public interface CacheListener<K, V> {

	/**
	 * 对象移除回调
	 *
	 * @param key          键
	 * @param cachedObject 被缓存的对象
	 */
	void onRemove(K key, V cachedObject);
}
