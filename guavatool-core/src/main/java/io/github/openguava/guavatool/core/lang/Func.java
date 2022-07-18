package io.github.openguava.guavatool.core.lang;

import java.io.Serializable;

/**
 * 有参数和返回值的函数对象
 * @author openguava
 *
 * @param <P>
 * @param <R>
 */
public interface Func<P, R> extends Serializable {
	
	/**
	 * 执行函数
	 *
	 * @param parameters 参数列表
	 * @return 函数执行结果
	 * @throws Exception 自定义异常
	 */
	@SuppressWarnings("unchecked")
	R call(P... parameters);
}