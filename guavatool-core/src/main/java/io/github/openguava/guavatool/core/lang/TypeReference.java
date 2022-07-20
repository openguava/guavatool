package io.github.openguava.guavatool.core.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Type类型参考
 * 通过构建一个类型参考子类，可以获取其泛型参数中的Type类型
 *
 * @param <T>
 */
public abstract class TypeReference<T> implements Type {

	/** 泛型参数 */
	private final Type type;

	/**
	 * 构造
	 */
	public TypeReference() {
		Class<?> clazz = this.getClass();
		Type tempType = (Type)clazz;
		//获取泛型父类
		if(!(tempType instanceof ParameterizedType)) {
			tempType = clazz.getGenericSuperclass();
		}
		//获取泛型参数
		if (tempType instanceof ParameterizedType) {
			final ParameterizedType genericSuperclass = (ParameterizedType)tempType;
			final Type[] typeArguments = genericSuperclass.getActualTypeArguments();
			if(typeArguments != null && typeArguments.length > 0) {
				tempType = typeArguments[0];
			}
		}
		this.type = tempType;
	}

	/**
	 * 获取用户定义的泛型参数
	 *
	 * @return 泛型参数
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.type.toString();
	}
	
	public static void main(String[] args) {
		TypeReference<Long> a = new TypeReference<Long>() {};
		System.out.println(a.getType());
	}
}