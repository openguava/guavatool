package io.github.openguava.guavatool.shiro.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.shiro.ShiroPrincipal;
import io.github.openguava.guavatool.shiro.ShiroToken;
import io.github.openguava.guavatool.shiro.handler.ShiroAuthHandler;
import io.github.openguava.guavatool.shiro.handler.ShiroConfigHandler;
import io.github.openguava.guavatool.shiro.handler.SimpleShiroAuthHandler;
import io.github.openguava.guavatool.shiro.handler.SimpleShiroConfigHandler;
import io.github.openguava.guavatool.spring.util.SpringUtils;

/**
 * shiro 工具类
 * @author openguava
 *
 */
public class ShiroUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroUtils.class);
	
	/** shiro 认证处理器 */
	private static ShiroAuthHandler shiroAuthHandler;
	
	/**
	 * 获取 shiro 认证处理器
	 * @return
	 */
	public static ShiroAuthHandler getShiroAuthHandler() {
		if(ShiroUtils.shiroAuthHandler == null) {
			ShiroUtils.shiroAuthHandler = SpringUtils.getBean(ShiroAuthHandler.class);
		}
		if(ShiroUtils.shiroAuthHandler == null) {
			ShiroUtils.shiroAuthHandler = new SimpleShiroAuthHandler();
		}
		return ShiroUtils.shiroAuthHandler;
	}
	
	public static void setShiroAuthHandler(ShiroAuthHandler shiroAuthHandler) {
		ShiroUtils.shiroAuthHandler = shiroAuthHandler;
	}
	
	/** shiro 配置处理器 */
	private static ShiroConfigHandler shiroConfigHandler;
	
	/**
	 * 获取 shiro 配置处理器
	 * @return
	 */
	public static ShiroConfigHandler getShiroConfigHandler() {
		if(ShiroUtils.shiroConfigHandler == null) {
			ShiroUtils.shiroConfigHandler = SpringUtils.getBean(ShiroConfigHandler.class);
		}
		if(ShiroUtils.shiroConfigHandler == null) {
			ShiroUtils.shiroConfigHandler = new SimpleShiroConfigHandler();
		}
		return ShiroUtils.shiroConfigHandler;
	}
	
	public static void setShiroConfigHandler(ShiroConfigHandler shiroConfigHandler) {
		ShiroUtils.shiroConfigHandler = shiroConfigHandler;
	}
	
	/**
	 * 获取 shiro subject
	 * @return
	 */
	public static Subject getSubject() {
		try {
			return SecurityUtils.getSubject();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 执行登录
	 * @param subject
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	public static boolean login(Subject subject, AuthenticationToken token) throws AuthenticationException {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		subject.login(token);
		return true;
	}
	
	/**
	 * 执行注销
	 * @param subject
	 * @return
	 */
	public static boolean logout(Subject subject) {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		subject.logout();
		return true;
	}
	
	/**
	 * 是否已登录
	 * @return
	 */
	public static boolean isLogin() {
		return isLogin(null);
	}
	
	/**
	 * 是否已登录
	 * @param subject
	 * @return
	 */
	public static boolean isLogin(Subject subject) {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		return subject.isAuthenticated() || subject.isRemembered();
	}
	
	/**
	 * 是否包含指定角色
	 * @param subject
	 * @param roleIdentifier
	 * @return
	 */
	public static boolean hasRole(Subject subject, String roleIdentifier) {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		return subject.hasRole(roleIdentifier);
	}
	
	/**
	 * 校验角色
	 * @param subject
	 * @param roleIdentifier
	 */
	public static void checkRole(Subject subject, String roleIdentifier) {
		if(subject == null && (subject = getSubject()) == null) {
			return;
		}
		subject.checkRole(roleIdentifier);;
	}
	
	/**
	 * 判断是否授权
	 * @param subject
	 * @param permission
	 * @return
	 */
	public static boolean isPermitted(Subject subject, String permission) {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		return subject.isPermitted(permission);
	}
	
	/**
	 * 校验权限
	 * @param subject
	 * @param permission
	 */
	public static void checkPermission(Subject subject, String permission) {
		if(subject == null && (subject = getSubject()) == null) {
			return;
		}
		subject.checkPermission(permission);
	}
	
	/**
	 * 获取认证用户信息
	 * @return
	 */
	public static ShiroPrincipal getShiroPrincipal() {
		return getShiroPrincipal(getSubject());
	}
	
	/**
	 * 获取认证用户信息
	 * @param subject
	 * @return
	 */
	public static ShiroPrincipal getShiroPrincipal(Subject subject) {
		return getPrincipal(subject, ShiroPrincipal.class);
	}
	
	/**
	 * 获取认证令牌
	 * @param subject
	 * @return
	 */
	public static ShiroToken getShiroToken(Subject subject) {
		ShiroPrincipal principal = getShiroPrincipal(subject);
		return getShiroToken(principal);
	}
	
	/**
	 * 获取认证令牌
	 * @param principal
	 * @return
	 */
	public static ShiroToken getShiroToken(ShiroPrincipal principal) {
		return (principal != null && principal.getToken() instanceof ShiroToken) ? (ShiroToken)principal.getToken() : null;
	}
	
	/**
	 * 获取认证令牌
	 * @param token
	 * @return
	 */
	public static ShiroToken getShiroToken(AuthenticationToken token) {
		return (token instanceof ShiroToken) ? (ShiroToken)token : null;
	}

	/**
	 * 获取 shiro principal
	 * @param subject
	 * @return
	 */
	public static Object getPrincipal(Subject subject) {
		if(subject == null && (subject = getSubject()) == null) {
			return null;
		}
		if(!isLogin(subject)) {
			return null;
		}
		try {
			return subject.getPrincipal();
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 获取 shiro principal
	 * @param <T>
	 * @param subject
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrincipal(Subject subject, Class<T> cls) {
		Object principal = getPrincipal(subject);
		if(principal == null || !cls.isInstance(principal)) {
			return null;
		}
		return (T)principal;
	}
	
	/**
	 * 设置 shiro principal
	 * @param subject
	 * @param principal
	 * @param realmName
	 * @return
	 */
	public static boolean setPrincipal(Subject subject, Object principal, String realmName) {
		if(subject == null && (subject = getSubject()) == null) {
			return false;
		}
		if(StringUtils.isEmpty(realmName)) {
			Realm realm = getRealm(null);
			if(realm != null) {
				realmName = realm.getName();
			}
		}
		subject.runAs(new SimplePrincipalCollection(principal, realmName));
		return true;
	}
	
	/**
	 * 获取 shiro 会话
	 * @return
	 */
	public static Session getSession() {
		return getSession(getSubject(), null);
	}
	
	/**
	 * 获取 shiro 会话
	 * @param subject
	 * @param create
	 * @return
	 */
	public static Session getSession(Subject subject, Boolean create) {
		if(subject == null && (subject = getSubject()) == null) {
			return null;
		}
		return create == null ? subject.getSession() : subject.getSession(create);
	}
	
	/**
	 * 获取 shiro 会话属性值
	 * @param subject
	 * @param key
	 * @return
	 */
	public static Object getSessionAttribute(Subject subject, Object key) {
		Session session = getSession(subject, null);
		if(session == null) {
			return null;
		}
		return session.getAttribute(key);
	}
	
	/**
	 * 设置 shiro 会话属性值
	 * @param subject
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setSessionAttribute(Subject subject, Object key, Object value) {
		Session session = getSession(subject, null);
		if(session == null) {
			return false;
		}
		session.setAttribute(key, value);
		return true;
	}
	
	/**
	 * 获取 shiro realms 集合
	 * @return
	 */
	public static Collection<Realm> getRealms() {
		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
		if(!(securityManager instanceof RealmSecurityManager)) {
			return new ArrayList<>();
		}
		return ((RealmSecurityManager)securityManager).getRealms();
	}
	
	/**
	 * 根据名称获取 shiro realm
	 * @param realmName
	 * @return
	 */
	public static Realm getRealm(String realmName) {
		Collection<Realm> realms = getRealms();
		for(Realm realm : realms) {
			if(StringUtils.isEmpty(realmName) || StringUtils.equals(realmName, realm.getName())) {
				return realm;
			}
		}
		return null;
	}
}
