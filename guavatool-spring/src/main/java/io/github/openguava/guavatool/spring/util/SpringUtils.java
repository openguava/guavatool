package io.github.openguava.guavatool.spring.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import io.github.openguava.guavatool.core.constant.HttpConstants;
import io.github.openguava.guavatool.core.constant.StringConstants;
import io.github.openguava.guavatool.core.exception.UtilException;
import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.spring.context.SpringContextHolder;

/***
 * spring 工具类
 * @author openguava
 *
 */
public class SpringUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);
	
	public static final String REQUEST_METHOD_GET = "GET";
	
	public static final String REQUEST_METHOD_POST = "POST";
	
	public static final String REQUEST_SCHEME_HTTP = "http";
	
	public static final String REQUEST_SCHEME_HTTPS = "https";
	
	public static final int REQUEST_PORT_80 = 80;
	
	public static final int REQUEST_PORT_443 = 443;
	
	/**
	 * 获取 当前 spring 应用上下文环境
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return SpringContextHolder.getApplicationContext();
	}
	
	/**
	 * 获取当前 spring 环境
	 * @return
	 */
	public static Environment getEnvironment() {
		return SpringContextHolder.getEnvironment();
	}
	
	/**
	 * 获取 bean 对象
	 * @param requiredType 请求的bean类型
	 * @param args 请求的bean参数
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType, Object... args) {
		return SpringContextHolder.getBean(requiredType, args);
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param args 请求的bean参数
	 * @return
	 */
	public static Object getBean(String name, Object... args) {
		return SpringContextHolder.getBean(name, args);
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param requiredType 请求的bean类型
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return SpringContextHolder.getBean(name, requiredType);
	}

	/**
	 * 动态注册bean
	 * @param <T>
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static <T> T registerBean(Class<T> clazz, Object... args) {
		return registerBean(clazz.getName(), clazz, args);
	}
	
	/**
	 * 动态注册bean
	 * @param <T>
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static <T> T registerBean(String name, Class<T> clazz, Object... args) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext)getApplicationContext();
		return registerBean(applicationContext, name, clazz, args);
		
	}
	/**
	 * 动态注册 bean
	 * @param <T>
	 * @param applicationContext
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, Object... args) {
		if(applicationContext.containsBean(name)) {
			Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T)bean;
            } else {
                throw new RuntimeException("beanName 重复 " + name);
            }
		}
		// beanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		if(args != null && args.length > 0) {
			for (Object arg : args) {
	            beanDefinitionBuilder.addConstructorArgValue(arg);
	        }
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		// beanDefinitionRegistry
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
		beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
		return applicationContext.getBean(name, clazz);
	}
	
	/**
	 * 获取web请求属性信息
	 * @return
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		try {
			RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
	        return (ServletRequestAttributes)attributes;
		} catch (Exception e) {
			LOGGER.trace(e.getMessage(), e);
			return null;
		}
    }
	
	/**
     * 获取web请求对象
     */
    public static HttpServletRequest getRequest() {
    	ServletRequestAttributes attributes = getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    /**
     * 获取web响应对象
     */
    public static HttpServletResponse getResponse() {
    	ServletRequestAttributes attributes = getRequestAttributes();
    	return attributes != null ? attributes.getResponse() : null;
    }
    
	/**
	 * 获取 ServletContext
	 * @param request
	 * @return
	 */
	public static ServletContext getRequestServletContext(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		return request.getServletContext();
	}
	
	/**
	 * 获取请求函数
	 * @param request
	 * @return
	 */
	public static String getRequestMethod(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		return request.getMethod();
	}
    
	/**
	 * 请求是否为GET函数
	 * @param request
	 * @return
	 */
	public static boolean isRequestGetMethod(HttpServletRequest request) {
		String method = getRequestMethod(request);
		return HttpConstants.HTTP_REQUEST_METHOD_GET.equalsIgnoreCase(method);
	}
	
	/**
	 * 请求是否为POST函数
	 * @param request
	 * @return
	 */
	public static boolean isRequestPostMethod(HttpServletRequest request) {
		String method = getRequestMethod(request);
		return HttpConstants.HTTP_REQUEST_METHOD_POST.equalsIgnoreCase(method);
	}
	
	/**
	 * 请求是否为OPTIONS函数
	 * @param request
	 * @return
	 */
	public static boolean isRequestOptionsMethod(HttpServletRequest request) {
		String method = getRequestMethod(request);
		return HttpConstants.HTTP_REQUEST_METHOD_OPTIONS.equalsIgnoreCase(method);
	}
	
	/**
	 * 请求是否为ajax
	 * @param request
	 * @return
	 */
	public static boolean isRequestAjax(HttpServletRequest request) {
		String xRequestedWith = getRequestHeader(request, HttpConstants.HTTP_HEADER_X_REQUESTED_WITH, true);
		return StringUtils.isNotEmpty(xRequestedWith) && "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
	}
	
	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 * @param request
	 * @return
	 */
	public static boolean isRequestMultipart(HttpServletRequest request) {
		if(!isRequestPostMethod(request)) {
			return false;
		}
		String contentType = request.getContentType();
		if(StringUtils.isEmpty(contentType)) {
			return false;
		}
		return contentType.toLowerCase().startsWith("multipart/");
	}
	
	/**
	 * 获取 header map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new HashMap<>();
		}
		Map<String, String> headers = new LinkedHashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		return headers;
	}
	
	/**
	 * 获取请求 header
	 * @param request
	 * @param name header名称
	 * @return
	 */
	public static String getRequestHeader(HttpServletRequest request, String name) {
		return getRequestHeader(request, name, false);
	}
    
	/**
	 * 获取请求 header
	 * @param request
	 * @param headerName header名称
	 * @param ignoreCaseName 是否忽略header名称大小写
	 * @return
	 */
	public static String getRequestHeader(HttpServletRequest request, String headerName, boolean ignoreCaseName) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		String header = null;
		if(!ignoreCaseName) {
			header = request.getHeader(headerName);
		} else {
			Enumeration<String> names = request.getHeaderNames();
			String name = null;
			while (names.hasMoreElements()) {
				name = names.nextElement();
				if (name != null && name.equalsIgnoreCase(headerName)) {
					header = request.getHeader(name);
					break;
				}
			}
		}
		if(header == null) {
			return header;
		}
		header = header.trim();
		return header;
	}
	
	/**
	 * 设置响应的Header
	 * @param response
	 * @param name header名称
	 * @param value 值，可以是String，Date， int
	 */
	public static void setRequestHeader(HttpServletResponse response, String name, Object value) {
		if(response == null && (response = getResponse()) == null) {
			return;
		}
		if (value instanceof String) {
			response.setHeader(name, (String) value);
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			response.setDateHeader(name, ((Date) value).getTime());
		} else if (value instanceof Integer || "int".equals(value.getClass().getSimpleName().toLowerCase())) {
			response.setIntHeader(name, (Integer) value);
		} else {
			response.setHeader(name, value.toString());
		}
	}
	
	/**
	 * 获取 param map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParameterMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new HashMap<>();
		}
		Map<String, String> params = new LinkedHashMap<>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			params.put(paramName, request.getParameter(paramName));
		}
		return params;
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @param name 参数名称
	 * @param charset 字符集
	 * @return
	 */
	public static String getRequestParameter(HttpServletRequest request, String name, String charset) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		String parameter = request.getParameter(name);
		if(parameter == null) {
			return parameter;
		}
		//字符集不匹配则自动转换
		if(charset != null && !charset.equalsIgnoreCase(request.getCharacterEncoding())) {
			String characterEncoding = request.getCharacterEncoding();
			try {
				parameter = new String(parameter.getBytes(characterEncoding), charset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//清除首尾空白
		parameter = parameter.trim();
		return parameter;
	}
	
	/**
	 * 获取 cookie 集合
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getRequestCookieMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new LinkedHashMap<>();
		}
		Map<String, Cookie> cookieMap = new LinkedHashMap<>();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return cookieMap;
		}
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie);
		}
		return cookieMap;
	}
	
	/**
	 * 获取 cookie 值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getRequestCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getRequestCookie(request, name);
		if(cookie == null) {
			return null;
		}
		String value = cookie.getValue();
		if(value != null) {
			value = value.trim();
		}
		return value;
	}
	
	/**
	 * 获取 cookie
	 * @param request
	 * @param name cookie 名称(忽略大小写)
	 * @return
	 */
	public static Cookie getRequestCookie(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = getRequestCookieMap(request);
		if(cookieMap.size() == 0) {
			return null;
		}
		for(Entry<String, Cookie> kv : cookieMap.entrySet()) {
			if(kv.getKey().equalsIgnoreCase(name)) {
				return kv.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param domain 域名
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. >0 : Cookie存在的秒数. 
	 */
	public static void setRequestCookie(HttpServletResponse response, String name, String value, String domain, int maxAgeInSeconds) {
		setRequestCookie(response, name, value, domain, "/", maxAgeInSeconds, false);
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param domain 域名
	 * @param path 路径
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. >0 : Cookie存在的秒数.
	 * @param isHttpOnly 
	 */
	public static void setRequestCookie(HttpServletResponse response, String name, String value, String domain, String path, int maxAgeInSeconds, boolean isHttpOnly) {
		if(response == null && (response = getResponse()) == null) {
			return;
		}
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if(path != null) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(isHttpOnly);
		response.addCookie(cookie);
	}
	
	/**
	 * 获取请求MultiPart表单文件
	 * @param request
	 * @return
	 */
	public static List<MultipartFile> getRequestMultipartFiles(HttpServletRequest request){
		List<MultipartFile> multipartFiles = new ArrayList<>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(!multipartResolver.isMultipart(request)){
			return multipartFiles;
		}
		// 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iterator = multiRequest.getFileNames();
        while (iterator.hasNext()) {
        	String name = iterator.next();
        	List<MultipartFile> files = multiRequest.getFiles(name);
        	for(MultipartFile file : files){
        		String fileName = file.getOriginalFilename();
        		if (StringUtils.isEmpty(fileName)) {
        			continue;
        		}
        		multipartFiles.add(file);
        	}
        }
        return multipartFiles;
	}
	
	/**
	 * 获取请求客户端UA信息
	 * @param request
	 * @return
	 */
	public static String getRequestUserAgent(HttpServletRequest request) {
		return getRequestHeader(request, HttpConstants.HTTP_HEADER_USER_AGENT, true);
	}
	
	/**
	 * 获取请求根路径
	 * @param request
	 * @return
	 */
	public static String getRequestBasePath(ServletRequest request){
		if(request == null && (request = getRequest()) == null) {
			return StringConstants.STRING_EMPTY;
		}
		StringBuilder str = new StringBuilder();
		str.append(request.getScheme());
		str.append("://");
		str.append(request.getServerName());
		if(request.getScheme().equalsIgnoreCase(REQUEST_SCHEME_HTTP) && request.getServerPort() == REQUEST_PORT_80) {
			str.append("");
		}else if(request.getScheme().equalsIgnoreCase(REQUEST_SCHEME_HTTPS) && request.getServerPort() == REQUEST_PORT_443) {
			str.append("");
		}else {
			str.append(":");
			str.append(request.getServerPort());
		}
		str.append(request.getServletContext().getContextPath());
		return str.toString(); 
	}
	
	/**
	 * 获取请求 servlet 路径
	 * @param request
	 * @return
	 */
	public static String getRequestServletPath(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return StringConstants.STRING_EMPTY;
		}
		return request.getServletPath();
	}
	
	/**
	 * 获取请求 token
	 * @param request
	 * @param tokenName token名称
	 * @param allowHeader 是否允许header参数
	 * @param allowCookie 是否允许cookie参数
	 * @param allowUrlParam 是否允许url参数
	 * @return
	 */
	public static String getRequestToken(HttpServletRequest request, String tokenName, boolean allowHeader, boolean allowCookie, boolean allowUrlParam) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		String tokenValue = null;
		// header
		if(allowHeader) {
			tokenValue = SpringUtils.getRequestHeader(request, tokenName);
			if(!StringUtils.isEmpty(tokenValue)) {
				return tokenValue;
			}
		}
		// cookie
		if(allowCookie) {
			tokenValue = SpringUtils.getRequestCookieValue(request, tokenName);
			if(!StringUtils.isEmpty(tokenValue)) {
				return tokenValue;
			}
		}
		// parameter
		if(allowUrlParam) {
			tokenValue = SpringUtils.getRequestParameter(request, tokenName, null);
			if(!StringUtils.isEmpty(tokenValue)) {
				return tokenValue;
			}
		}
		return tokenValue;
	}
	
	/**
	 * 获取响应 PrintWriter 对象
	 * @param response
	 * @return
	 */
	public static PrintWriter getResponseWriter(ServletResponse response) {
		if(response == null && (response = getResponse()) == null) {
			return null;
		}
		try {
			return response.getWriter();
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}
}
