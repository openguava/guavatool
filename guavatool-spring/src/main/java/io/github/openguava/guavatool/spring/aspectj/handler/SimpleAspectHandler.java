package io.github.openguava.guavatool.spring.aspectj.handler;

import org.aspectj.lang.JoinPoint;

import io.github.openguava.guavatool.spring.aspectj.DataScopeAspect;
import io.github.openguava.guavatool.spring.aspectj.LogAspect;
import io.github.openguava.guavatool.spring.aspectj.annotation.DataScope;
import io.github.openguava.guavatool.spring.aspectj.annotation.Log;

public class SimpleAspectHandler implements AspectHandler {

	private static AspectHandler instance;
	
	/**
	 * 获取实例
	 * @return
	 */
	public static AspectHandler getInstance() {
		if(SimpleAspectHandler.instance == null) {
			SimpleAspectHandler.instance = new SimpleAspectHandler();
		}
		return SimpleAspectHandler.instance;
	}
	
	@Override
	public void handleLog(LogAspect logAspect, JoinPoint joinPoint, Log log, Exception e, Object jsonResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDataScope(DataScopeAspect aspect, JoinPoint joinPoint, DataScope controllerDataScope) {
		// TODO Auto-generated method stub
		
	}
}
