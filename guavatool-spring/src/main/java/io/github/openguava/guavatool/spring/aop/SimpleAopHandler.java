package io.github.openguava.guavatool.spring.aop;

import org.aspectj.lang.JoinPoint;

import io.github.openguava.guavatool.spring.aop.annotation.DataScope;
import io.github.openguava.guavatool.spring.aop.annotation.Log;
import io.github.openguava.guavatool.spring.aop.aspect.DataScopeAspect;
import io.github.openguava.guavatool.spring.aop.aspect.LogAspect;

public class SimpleAopHandler implements AopHandler {

	private static AopHandler instance;
	
	/**
	 * 获取实例
	 * @return
	 */
	public static AopHandler getInstance() {
		if(SimpleAopHandler.instance == null) {
			SimpleAopHandler.instance = new SimpleAopHandler();
		}
		return SimpleAopHandler.instance;
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
