package io.github.openguava.guavatool.core.lang;

/**
 * 有参数无返回值的函数对象
 * @author openguava
 *
 * @param <P>
 */
public interface FuncP<P> {

	/**
	 * 执行函数
	 * 
	 * @param parameter 参数
	 */
	void call(P parameter);
}
