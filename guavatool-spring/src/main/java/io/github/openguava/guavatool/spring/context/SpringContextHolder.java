package io.github.openguava.guavatool.spring.context;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * SpringContextHolder
 * @author openguava
 *
 */
@Component
public class SpringContextHolder implements BeanFactoryPostProcessor, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);
	
	/** ApplicationContext */
    private static ApplicationContext applicationContext;
    
    public static ApplicationContext getApplicationContext() {
    	if(SpringContextHolder.applicationContext == null) {
    		ServletContext sc = SpringContextHolder.getServletContext();
    		if(sc != null) {
    			SpringContextHolder.applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
    		} else {
    			SpringContextHolder.applicationContext = ContextLoader.getCurrentWebApplicationContext();
    		}
    	}
		return SpringContextHolder.applicationContext;
	}
    
    /** ServletContext */
    private static ServletContext servletContext;
    
    public static ServletContext getServletContext() {
    	if(SpringContextHolder.servletContext == null) {
    		try {
    			RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    			if(attributes != null) {
    				SpringContextHolder.servletContext = ((ServletRequestAttributes)attributes).getRequest().getServletContext();
    			}
    		} catch (Exception e) {
    			LOGGER.trace(e.getMessage(), e);
    			return null;
    		}
    	}
		return SpringContextHolder.servletContext;
	}
    
    public static void setServletContext(ServletContext servletContext) {
		SpringContextHolder.servletContext = servletContext;
	}
    
    /** Environment */
    private static Environment environment;
    
    public static Environment getEnvironment() {
    	if(SpringContextHolder.environment == null) {
    		SpringContextHolder.environment = getApplicationContext().getEnvironment();
    	}
		return SpringContextHolder.environment;
	}
    
    /** MessageSourceAccessor */
    private static MessageSourceAccessor messageSourceAccessor;
    
    public static MessageSourceAccessor getMessageSourceAccessor() {
		return SpringContextHolder.messageSourceAccessor;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		init(applicationContext);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		LOGGER.info("Spring Context Holder initialized Successful!");
	}

	/**
	 * 初始化
	 * @param context
	 */
	private static void init(ApplicationContext context) {
		SpringContextHolder.applicationContext = context;
		SpringContextHolder.environment = context.getEnvironment();
		if(context instanceof WebApplicationContext) {
			SpringContextHolder.servletContext = ((WebApplicationContext)context).getServletContext();
		}
		SpringContextHolder.messageSourceAccessor = new MessageSourceAccessor(context, Locale.SIMPLIFIED_CHINESE);
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param args 请求的bean参数
	 * @return
	 */
	public static Object getBean(String name, Object... args) {
		ApplicationContext ac = SpringContextHolder.getApplicationContext();
		return ac != null ? getApplicationContext().getBean(name, args) : null;
	}
	
	/**
	 * 获取 bean 对象
	 * @param requiredType 请求的bean类型
	 * @param args 请求的bean参数
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType, Object... args) {
		ApplicationContext ac = SpringContextHolder.getApplicationContext();
		return ac != null ? getApplicationContext().getBean(requiredType, args) : null;
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param requiredType 请求的bean类型
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		ApplicationContext ac = SpringContextHolder.getApplicationContext();
		return ac != null ? getApplicationContext().getBean(name, requiredType) : null;
	}
	
	/**
     * 获取properties的值，没有获取到返回null<br>
     * 
     * @return 该key对应的value值
     */
    public static String getProperty(String key) {
    	Environment env = SpringContextHolder.getEnvironment();
    	return env != null ? env.getProperty(key) : null;
    }
    
	/**
	 * 获取properties的值，没有获取到返回null
	 * @param key 该key对应的value值
	 * @param defaultValue 默认值
	 * @return
	 */
    public static String getProperty(String key, String defaultValue) {
    	Environment env = SpringContextHolder.getEnvironment();
    	return env != null ? env.getProperty(key, defaultValue) : null;
    }
}
