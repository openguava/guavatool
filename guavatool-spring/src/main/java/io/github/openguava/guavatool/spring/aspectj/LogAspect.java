package io.github.openguava.guavatool.spring.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import io.github.openguava.guavatool.spring.aspectj.annotation.Log;
import io.github.openguava.guavatool.spring.aspectj.handler.SimpleAspectHandler;

@Aspect
@Component
public class LogAspect {

	/**
	 * 配置织入点
	 */
	@Pointcut("@annotation(io.github.openguava.guavatool.spring.aspectj.annotation.Log)")
	public void logPointCut() {
		
	}

	/**
	 * 处理完请求后执行
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
		SimpleAspectHandler.getInstance().handleLog(this, joinPoint, controllerLog, null, jsonResult);
	}

	/**
	 * 拦截异常操作
	 * 
	 * @param joinPoint 切点
	 * @param e         异常
	 */
	@AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
		SimpleAspectHandler.getInstance().handleLog(this, joinPoint, controllerLog, e, null);
	}
}
