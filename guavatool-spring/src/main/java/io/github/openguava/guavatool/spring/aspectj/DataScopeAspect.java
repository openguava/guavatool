package io.github.openguava.guavatool.spring.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import io.github.openguava.guavatool.spring.aspectj.annotation.DataScope;
import io.github.openguava.guavatool.spring.aspectj.handler.SimpleAspectHandler;

@Aspect
@Component
public class DataScopeAspect {

	@Before("@annotation(controllerDataScope)")
	public void doBefore(JoinPoint joinPoint, DataScope controllerDataScope) {
		SimpleAspectHandler.getInstance().handleDataScope(this, joinPoint, controllerDataScope);
	}
}
