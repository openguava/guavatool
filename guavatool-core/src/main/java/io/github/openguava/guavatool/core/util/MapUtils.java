package io.github.openguava.guavatool.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map 工具类
 * @author openguava
 *
 */
public class MapUtils {
	
	/**
	 * 创建HashMap
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> HashMap<K, V> createHashMap() {
		return new HashMap<K, V>();
	}
	
	/**
	 * 创建LinkedHashMap
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> LinkedHashMap<K, V> createLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}
	
	/**
	 * 创建 ConcurrentHashMap
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> ConcurrentHashMap<K, V> createConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	/**
     * 判断Map是否为空
     * @param m
     * @return
     */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
    /**
     * 判断Map是否为非空
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return !isEmpty(m);
    }
    
    /**
     * 获取Map值
     * @param <K>
     * @param map
     * @param key
     * @param defaultValue
     * @return
     */
    public static <K> Object getObject(Map<? super K, ?> map, K key, Object defaultValue) {
    	if(map == null || !map.containsKey(key)) {
    		return defaultValue;
    	}
    	Object value = map.get(key);
    	return value != null ? value : defaultValue;
    }
    
    /**
     * 获取Map字符串值
     * @param <K>
     * @param map
     * @param key
     * @param defaultValue
     * @return
     */
    public static <K> String getString(Map<?, ?> map, K key, String defaultValue) {
    	if(map == null || key == null) {
    		return null;
    	}
    	Object value = map.get(key);
    	return value != null ? StringUtils.toString(value) : defaultValue;
    }
}
