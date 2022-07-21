package io.github.openguava.guavatool.spring.aspectj;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.spring.aspectj.annotation.DataSource;
import io.github.openguava.guavatool.spring.datasource.DynamicDataSourceContextHolder;

@Aspect
@Order(1)
@Component
public class DataSourceAspect {

	@Pointcut("@annotation(io.github.openguava.guavatool.spring.aspectj.annotation.DataSource)"
			+ "|| @within(io.github.openguava.guavatool.spring.aspectj.annotation.DataSource)")
	public void dsPointCut() {

	}

	@Around("dsPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		DataSource dataSource = getDataSource(point);
		if(dataSource == null || StringUtils.isEmpty(dataSource.value())) {
			return point.proceed();
		}
		DynamicDataSourceContextHolder.setDataSourceType(dataSource.value());
		try {
			return point.proceed();
		} finally {
			// 销毁数据源 在执行方法之后
			DynamicDataSourceContextHolder.clearDataSourceType();
		}
	}

	/**
	 * 获取需要切换的数据源
	 */
	public DataSource getDataSource(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
		if (Objects.nonNull(dataSource)) {
			return dataSource;
		}
		return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
	}
}
