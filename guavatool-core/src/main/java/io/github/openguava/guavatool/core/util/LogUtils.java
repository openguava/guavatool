package io.github.openguava.guavatool.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 */
public class LogUtils {

	private static Map<String, Logger> LoggerMap = new ConcurrentHashMap<String, Logger>();
	
	private static Logger getLogger(Class<?> cls) {
		return getLogger(cls.getName());
	}
	
	private static synchronized Logger getLogger(String name) {
		Logger logger;
		if(!LoggerMap.containsKey(name)) {
			logger = LoggerFactory.getLogger(name);
			LoggerMap.put(name, logger);
		} else {
			logger = LoggerMap.get(name);
		}
		return logger;
	}
	
	/**
	 * 消息日志
	 * @param tag
	 * @param format
	 * @param arguments
	 */
	public static void info(String tag, String format, Object... arguments) {
		Logger log = getLogger(tag);
		if(log.isInfoEnabled()){
			log.info(format, arguments);
		}
	}
	
	/**
	 * 消息日志
	 * @param cls
	 * @param format
	 * @param arguments
	 */
	public static void info(Class<?> cls, String format, Object... arguments) {
		Logger log = getLogger(cls);
		if(log.isInfoEnabled()){
			log.info(format, arguments);
		}
	}
	
	/**
	 * 调试日志
	 * @param tag
	 * @param format
	 * @param arguments
	 */
	public static void debug(String tag, String format, Object... arguments) {
		Logger log = getLogger(tag);
		if(log.isDebugEnabled()) {
			log.debug(format, arguments);
		}
	}
	
	/**
	 * 调试日志
	 * @param cls
	 * @param format
	 * @param arguments
	 */
	public static void debug(Class<?> cls, String format, Object... arguments) {
		Logger log = getLogger(cls);
		if(log.isDebugEnabled()) {
			log.debug(format, arguments);
		}
	}
	
	/**
	 * 错误日志
	 * @param tag
	 * @param format
	 * @param arguments
	 */
	public static void error(String tag, String format, Object... arguments) {
		Logger log = getLogger(tag);
		if(log.isErrorEnabled()) {
			log.error(format, arguments);
		}
	}
	
	/**
	 * 错误日志
	 * @param cls
	 * @param format
	 * @param arguments
	 */
	public static void error(Class<?> cls, String format, Object... arguments) {
		Logger log = getLogger(cls);
		if(log.isErrorEnabled()) {
			log.error(format, arguments);
		}
	}
	
	/**
	 * 错误日志
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void error(String tag, String msg, Throwable t) {
		Logger log = getLogger(tag);
		if(log.isErrorEnabled()) {
			log.error(msg, t);
		}
	}
	
	/**
	 * 错误日志
	 * @param cls
	 * @param msg
	 * @param t
	 */
	public static void error(Class<?> cls, String msg, Throwable t) {
		Logger log = getLogger(cls);
		if(log.isErrorEnabled()) {
			log.error(msg, t);
		}
	}
	
	/**
	 * 警告日志
	 * @param tag
	 * @param msg
	 * @param t
	 */
	public static void warn(String tag, String msg, Throwable t) {
		Logger log = getLogger(tag);
		if(log.isWarnEnabled()) {
			log.warn(msg, t);
		}
	}
	
	/**
	 * 警告日志
	 * @param cls
	 * @param msg
	 * @param t
	 */
	public static void warn(Class<?> cls, String msg, Throwable t) {
		Logger log = getLogger(cls);
		if(log.isWarnEnabled()) {
			log.warn(msg, t);
		}
	}
	
	/**
	 * 警告日志
	 * @param cls
	 * @param format
	 * @param arguments
	 */
	public static void warn(Class<?> cls, String format, Object... arguments) {
		Logger log = getLogger(cls);
		if(log.isWarnEnabled()) {
			log.warn(format, arguments);
		}
	}
}