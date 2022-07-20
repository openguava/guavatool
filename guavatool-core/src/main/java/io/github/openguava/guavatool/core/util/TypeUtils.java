package io.github.openguava.guavatool.core.util;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import io.github.openguava.guavatool.core.enums.BasicType;
import io.github.openguava.guavatool.core.lang.TypeReference;

/**
 * type 工具类
 * @author openguava
 *
 */
public class TypeUtils {

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}
	
	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}
	
	/**
	 * 是否为简单值类型<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) //
				|| clazz.isEnum() //
				|| CharSequence.class.isAssignableFrom(clazz) //
				|| Number.class.isAssignableFrom(clazz) //
				|| Date.class.isAssignableFrom(clazz) //
				|| clazz.equals(URI.class) //
				|| clazz.equals(URL.class) //
				|| clazz.equals(Locale.class) //
				|| clazz.equals(Class.class);//
	}
	
	/**
	 * 检查目标类是否可以从原类转化<br>
	 * 转化包括：<br>
	 * 1、原类是对象，目标类型是原类型实现的接口<br>
	 * 2、目标类型是原类型的父类<br>
	 * 3、两者是原始类型或者包装类型（相互转换）
	 *
	 * @param targetType 目标类型
	 * @param sourceType 原类型
	 * @return 是否可转化
	 */
	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
			return targetType.equals(resolvedPrimitive);
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
			return resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper);
		}
	}
	
	/**
	 * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
	 *
	 * @param types1 类组1
	 * @param types2 类组2
	 * @return 是否相同、父类或接口
	 */
	public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
		if (CollectionUtils.isEmpty(types1) && CollectionUtils.isEmpty(types2)) {
			return true;
		}
		if (null == types1 || null == types2) {
			// 任何一个为null不相等（之前已判断两个都为null的情况）
			return false;
		}
		if (types1.length != types2.length) {
			return false;
		}

		Class<?> type1;
		Class<?> type2;
		for (int i = 0; i < types1.length; i++) {
			type1 = types1[i];
			type2 = types2[i];
			if (isBasicType(type1) && isBasicType(type2)) {
				// 原始类型和包装类型存在不一致情况
				if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
					return false;
				}
			} else if (false == type1.isAssignableFrom(type2)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否为标准的类<br>
	 * 这个类必须：
	 *
	 * <pre>
	 * 1、非接口
	 * 2、非抽象类
	 * 3、非Enum枚举
	 * 4、非数组
	 * 5、非注解
	 * 6、非原始类型（int, long等）
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz //
				&& false == clazz.isInterface() //
				&& false == isAbstract(clazz) //
				&& false == clazz.isEnum() //
				&& false == clazz.isArray() //
				&& false == clazz.isAnnotation() //
				&& false == clazz.isSynthetic() //
				&& false == clazz.isPrimitive();//
	}
	
	/**
	 * 是否为抽象类
	 *
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}
	
	/**
	 * 判断类是否为枚举类型
	 *
	 * @param clazz 类
	 * @return 是否为枚举类型
	 */
	public static boolean isEnum(Class<?> clazz) {
		return null != clazz && clazz.isEnum();
	}
	
	/**
	 * 是否为 String 类型
	 * @return
	 */
	public static boolean isString(Type type) {
		return type != null && type.equals(String.class);
	}
	
	/**
	 * 是否为 Character 类型
	 * @return
	 */
	public static boolean isCharacter(Type type) {
		return type != null && type.equals(Character.class);
	}
	
	/**
	 * 是否为 StringBuilder 类型
	 * @param type
	 * @return
	 */
	public static boolean isStringBuilder(Type type) {
		return type != null && type.equals(StringBuilder.class);
	}
	
	/**
	 * 是否为 Boolean 类型
	 * @return
	 */
	public static boolean isBoolean(Type type) {
		return type != null && type.equals(Boolean.class);
	}
	
	/**
	 * 是否为 Byte 类型
	 * @return
	 */
	public static boolean isByte(Type type) {
		return type != null && type.equals(Byte.class);
	}
	
	/**
	 * 是否为 Short 类型
	 * @return
	 */
	public static boolean isShort(Type type) {
		return type != null && type.equals(Short.class);
	}
	
	/**
	 * 是否为 Integer 类型
	 * @return
	 */
	public static boolean isInteger(Type type) {
		return type != null && type.equals(Integer.class);
	}

	/**
	 * 是否为 Long 类型
	 * @return
	 */
	public static boolean isLong(Type type) {
		return type != null && type.equals(Long.class);
	}
	
	/**
	 * 是否为 Float 类型
	 * @return
	 */
	public static boolean isFloat(Type type) {
		return type != null && type.equals(Float.class);
	}
	
	/**
	 * 是否为 Double 类型
	 * @return
	 */
	public static boolean isDouble(Type type) {
		return type != null && type.equals(Double.class);
	}
	
	/**
	 * 是否为 Number 类型
	 * @return
	 */
	public static boolean isNumber(Type type) {
		return type != null && type.equals(Number.class);
	}
	
	/**
	 * 是否为 BigDecimal 类型
	 * @return
	 */
	public static boolean isBigDecimal(Type type) {
		return type != null && type.equals(BigDecimal.class);
	}
	
	/**
	 * 是否为 Date 类型
	 * @return
	 */
	public static boolean isDate(Type type) {
		return type != null && type.equals(Date.class);
	}
	
	/**
	 * 比较对象
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return
	 */
	public static <T> Integer compareTo(T obj1, T obj2) {
		if(obj1 == null || obj2 == null) {
			return null;
		}
		TypeReference<T> typeReference = new TypeReference<T>() {
		};
		if(isString(typeReference.getType())) {
			return ((String)obj1).compareTo((String)obj2);
		} else if(isCharacter(typeReference.getType())) {
			return ((Character)obj1).compareTo((Character)obj2);
		} else if(isBoolean(typeReference.getType())) {
			return ((Boolean)obj1).compareTo((Boolean)obj2);
		} else if(isByte(typeReference.getType())) {
			return ((Byte)obj1).compareTo((Byte)obj2);
		} else if(isShort(typeReference.getType())) {
			return ((Short)obj1).compareTo((Short)obj2);
		} else if(isInteger(typeReference.getType())) {
			return ((Integer)obj1).compareTo((Integer)obj2);
		} else if(isLong(typeReference.getType())) {
			return ((Long)obj1).compareTo((Long)obj2);
		} else if(isFloat(typeReference.getType())) {
			return ((Float)obj1).compareTo((Float)obj2);
		} else if(isDouble(typeReference.getType())) {
			return ((Double)obj1).compareTo((Double)obj2);
		} else if(isBigDecimal(typeReference.getType())) {
			return ((BigDecimal)obj1).compareTo((BigDecimal)obj2);
		} else if(isDate(typeReference.getType())) {
			return ((Date)obj1).compareTo((Date)obj2);
		}
		return obj1.toString().compareTo(obj2.toString());
	}
	
	/**
	 * 默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T defaultValue(Class<T> clazz) {
		if(clazz == null) {
			return null;
		}
		if(isInteger(clazz)) {
			Integer value = 0;
			return (T)value;
		} else if(isLong(clazz)) {
			Long value = 0L;
			return (T)value;
		} else if(isDouble(clazz)) {
			Double value = 0.0;
			return (T)value;
		} else if(isBigDecimal(clazz)) {
			BigDecimal value = BigDecimal.ZERO;
			return (T)value;
		} else if(isString(clazz)) {
			String value = "";
			return (T)value;
		} else {
			Object value = ReflectUtils.newInstance(clazz, null);
			return value != null ? (T)value : null;
		}
	}
	
	/**
	 * 值相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T add(T v1, T v2) {
		if(v1 == null && v2 == null) {
			return null;
		}
		if(v1 == null) {
			return v2;
		}
		if(v2 == null) {
			return v1;
		}
		Type type = v1.getClass();
		if(isInteger(type)) {
			Integer v3 = (Integer)v1 + (Integer)v2;
			return (T)v3;
		} else if(isLong(type)) {
			Long v3 = (Long)v1 + (Long)v2;
			return (T)v3;
		} else if(isFloat(type)) {
			Float v3 = (Float)v1 + (Float)v2;
			return (T)v3;
		} else if(isDouble(type)) {
			Double v3 = (Double)v1 + (Double)v2;
			return (T)v3;
		} else if(isBigDecimal(type)) {
			BigDecimal v3 = ((BigDecimal)v1).add((BigDecimal)v2);
			return (T)v3;
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		Number a = add(1.0, 3.0);
		System.out.println(a);
	}
}