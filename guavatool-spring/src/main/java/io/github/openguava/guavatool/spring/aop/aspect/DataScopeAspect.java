package io.github.openguava.guavatool.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import io.github.openguava.guavatool.spring.aop.SimpleAopHandler;
import io.github.openguava.guavatool.spring.aop.annotation.DataScope;

@Aspect
public class DataScopeAspect {

	@Before("@annotation(controllerDataScope)")
	public void doBefore(JoinPoint joinPoint, DataScope controllerDataScope) {
		SimpleAopHandler.getInstance().handleDataScope(this, joinPoint, controllerDataScope);
	}
}
