package io.github.openguava.guavatool.spring.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志记录注解
 * 
 * @author openguava
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	/**
	 * 模块
	 */
	public String title() default "";
	
	/**
	 * 功能
	 */
	public String businessType() default "other";
	
	/**
	 * 操作人类别
	 */
	public String operatorType() default "MANAGE";
	
	/**
	 * 是否保存请求的参数
	 */
	public boolean isSaveRequestData() default true;

	/**
	 * 是否保存响应的参数
	 */
	public boolean isSaveResponseData() default true;
}
