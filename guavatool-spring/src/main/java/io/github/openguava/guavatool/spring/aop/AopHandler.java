package io.github.openguava.guavatool.spring.aop;

import org.aspectj.lang.JoinPoint;

import io.github.openguava.guavatool.spring.aop.annotation.DataScope;
import io.github.openguava.guavatool.spring.aop.annotation.Log;
import io.github.openguava.guavatool.spring.aop.aspect.DataScopeAspect;
import io.github.openguava.guavatool.spring.aop.aspect.LogAspect;

public interface AopHandler {

	/**
	 * 处理操作日志
	 * @param aspect
	 * @param joinPoint
	 * @param log
	 * @param e
	 * @param jsonResult
	 */
	void handleLog(LogAspect aspect, JoinPoint joinPoint, Log log, final Exception e, Object jsonResult);
	
	/**
	 * 处理数据权限
	 * @param aspect
	 * @param joinPoint
	 * @param controllerDataScope
	 */
	void handleDataScope(DataScopeAspect aspect,JoinPoint joinPoint, DataScope controllerDataScope);
}
