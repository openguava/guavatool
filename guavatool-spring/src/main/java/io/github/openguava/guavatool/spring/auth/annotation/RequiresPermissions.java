package io.github.openguava.guavatool.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.openguava.guavatool.core.enums.Logical;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 * @author openguava
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /**
     * 需要校验的权限码
     * 
     */
    String[] value();
    
    /**
     * 验证模式：AND | OR，默认AND
     * 
     */
    Logical logical() default Logical.AND; 
}
