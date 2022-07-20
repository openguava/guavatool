package io.github.openguava.guavatool.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import io.github.openguava.guavatool.spring.aop.SimpleAopHandler;
import io.github.openguava.guavatool.spring.aop.annotation.Log;

@Aspect
public class LogAspect {

	/**
	 * 处理完请求后执行
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
		SimpleAopHandler.getInstance().handleLog(this, joinPoint, controllerLog, null, jsonResult);
	}

	/**
	 * 拦截异常操作
	 * 
	 * @param joinPoint 切点
	 * @param e         异常
	 */
	@AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
		SimpleAopHandler.getInstance().handleLog(this, joinPoint, controllerLog, e, null);
	}
}
