package io.github.openguava.guavatool.spring.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 * 
 * @author openguava
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

	/**
	 * 机构表的别名
	 * @return
	 */
	public String orgAlias() default "";
	
	/**
	 * 部门表的别名
	 */
	public String deptAlias() default "";

	/**
	 * 用户表的别名
	 */
	public String userAlias() default "";
}
