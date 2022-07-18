package io.github.openguava.guavatool.core.lang;

/**
 * 无参数带返回值的函数对象
 * @author openguava
 *
 * @param <R>
 */
public interface FuncR<R> {
	
	/**
	 * 执行函数
	 * 
	 * @return 函数执行结果
	 */
	R call();
}