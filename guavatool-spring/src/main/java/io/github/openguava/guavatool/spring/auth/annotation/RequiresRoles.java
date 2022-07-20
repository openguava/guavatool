package io.github.openguava.guavatool.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.openguava.guavatool.core.enums.Logical;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRoles {

    /**
     * 需要校验的角色标识
     * 
     */
    String[] value();
    
    /**
     * 验证逻辑：AND | OR，默认AND
     * @since 1.1.0
     */
    Logical logical() default Logical.AND; 
}
