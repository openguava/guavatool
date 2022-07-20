package io.github.openguava.guavatool.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 在 springboot 2.6.x 不兼容问题的处理
 *
 * @author openguava
 */
@Component
public class SwaggerBeanPostProcessor implements BeanPostProcessor {
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
			customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
		}
		return bean;
	}

	private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
		List<T> copy = mappings.stream().filter(mapping -> !hasPatternParser(mapping))
				.collect(Collectors.toList());
		mappings.clear();
		mappings.addAll(copy);
	}

	@SuppressWarnings("unchecked")
	private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
		try {
			Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
			field.setAccessible(true);
			return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static <T extends RequestMappingInfoHandlerMapping> boolean hasPatternParser(T mapping) {
		//return mapping.getPatternParser() != null;
		//org.springframework.web.util.pattern.PathPatternParser
		try {
			Method method = mapping.getClass().getMethod("getPatternParser");
			if(method == null) {
				return false;
			}
			return method.invoke(mapping) != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}