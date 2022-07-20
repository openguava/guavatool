package io.github.openguava.guavatool.core.util;

import java.util.Map;

/**
 * map 工具类
 * @author openguava
 *
 */
public class MapUtils {

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
    
    public static <K> Object getObject(Map<? super K, ?> map, K key, Object defaultValue) {
    	if(map == null || !map.containsKey(key)) {
    		return defaultValue;
    	}
    	return map.get(key);
    }
    
    public static <K> String getString(Map<?, ?> map, String key, String defaultValue) {
    	if(map == null || key == null) {
    		return null;
    	}
    	Object value = map.get(key);
    	return defaultValue;
    }
}
