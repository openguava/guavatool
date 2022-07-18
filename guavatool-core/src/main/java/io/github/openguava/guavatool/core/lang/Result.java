package io.github.openguava.guavatool.core.lang;

/**
 * 结果接口
 * @author openguava
 *
 * @param <T>
 */
public interface Result<T> {

	/**
	 * 获取 code
	 * @return
	 */
	int getCode();
	
	/**
	 * 获取 msg
	 * @return
	 */
	String getMsg();
	
	/**
	 * 获取 data
	 * @return
	 */
	T getData();
	
	/**
	 * 是否成功
	 * @return
	 */
	boolean isSuccess();
	
	/**
	 * 是否失败
	 * @return
	 */
	boolean isFail();
}
