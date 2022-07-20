package io.github.openguava.guavatool.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.openguava.guavatool.core.lang.Filter;
import io.github.openguava.guavatool.core.lang.FuncP;
import io.github.openguava.guavatool.core.lang.Func;

/**
 * 集合工具类
 * @author openguava
 *
 */
public class CollectionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtils.class);
	
	/**
	 * 判断集合是否为空
	 * @param c
	 * @return
	 */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
    
    /**
     * 判断集合是否为非空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    @SafeVarargs
    public static <T> boolean isEmpty(T... array) {
    	return ArrayUtils.isEmpty(array);
    }
    
    /**
     * 判断数组是否为非空
     * @param array
     * @return
     */
    @SafeVarargs
	public static <T> boolean isNotEmpty(T... array) {
    	return ArrayUtils.isNotEmpty(array);
    }
    
    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
    	return list == null || list.isEmpty();
	}
    
	/**
	 * 判断List是否为非空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}
	
	/**
	 * 判断 iterable 是否为空
	 * @param iterable
	 * @return
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return iterable == null || isEmpty(iterable.iterator());
	}
	
	/**
	 * 判断 iterable 是否为非空
	 * @param iterable
	 * @return
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return !isEmpty(iterable);
	}
	
	/**
	 * 判断 Iterator 是否为空
	 * @param iterator
	 * @return
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return iterator == null || !iterator.hasNext();
	}
	
	/**
	 * 判断 Iterator 是否为非空
	 * @param iterator
	 * @return
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return !isEmpty(iterator);
	}
	
	/**
	 * 判断 {@link Set} 是否为空
	 * @param set
	 * @return
	 */
	public static boolean isEmpty(Set<?> set) {
		return set == null || set.isEmpty();
	}
	
	/**
	 * 判断 {@link Set} 是否为非空
	 * @param set
	 * @return
	 */
	public static boolean isNotEmpty(Set<?> set) {
		return !isEmpty(set);
	}
	
	/**
	 * 新建一个空的 {@link List}
	 * @param componentType
	 * @param newSize
	 * @return
	 */
	public static <T> List<T> newList(Class<?> componentType, int newSize){
		return new ArrayList<>(newSize);
	}
	
	
	
	/**
	 * 遍历集合
	 * @param <T>
	 * @param list
	 * @param func
	 */
	public static <T> void each(List<T> list, FuncP<T> func) {
		if(isEmpty(list)) {
			return;
		}
		for(T item : list) {
			if(func != null) {
				func.call(item);
			}
		}
	}
	
	/**
	 * 查找所有匹配的元素(多个)
	 * @param <T>
	 * @param list
	 * @param filter
	 * @return
	 */
	public static <T> List<T> findAll(List<T> list, Filter<T> filter) {
		if(isEmpty(list)) {
			return new ArrayList<>();
		}
		List<T> findList = new ArrayList<>();
		for(T item : list) {
			if(filter.accept(item)) {
				findList.add(item);
			}
		}
		return findList;
	}
	
	/**
	 * 获取集合首个元素
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> T first(Collection<T> list) {
		if(list != null) {
			Iterator<T> iterator = list.iterator();
			if(iterator.hasNext()) {
				return iterator.next();
			}
		}
		return null;
	}
	
	/**
	 * 转换为List
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @return
	 */
	public static <T, V> List<V> toList(Collection<T> list, Func<T, V> func) {
		return toList(list, func, false);
	}
	
	/**
	 * 转换为List
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @param allowNull
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V> List<V> toList(Collection<T> list, Func<T, V> func, boolean allowNull) {
		if(isEmpty(list)) {
			return new ArrayList<>();
		}
		List<V> newList = new ArrayList<>();
		for (T item : list) {
			if(func != null) {
				V newItem = func.call(item);
				if(!allowNull && newItem == null) {
					continue;
				}
				newList.add(newItem);
			}
		}
		return newList;
	}
	
	/**
	 * 转换为Set
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @return
	 */
	public static <T, V> Set<V> toSet(Collection<T> list, Func<T, V> func) {
		return toSet(list, func, false);
	}
	
	/**
	 * 转换为Set
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @param allowNull
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V> Set<V> toSet(Collection<T> list, Func<T, V> func, boolean allowNull) {
		if(isEmpty(list)) {
			return new HashSet<>();
		}
		Set<V> newList = new HashSet<>();
		for (T item : list) {
			if(func != null) {
				V newItem = func.call(item);
				if(!allowNull && newItem == null) {
					continue;
				}
				newList.add(newItem);
			}
		}
		return newList;
	}
}
