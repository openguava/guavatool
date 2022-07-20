package io.github.openguava.guavatool.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import io.github.openguava.guavatool.core.util.LogUtils;

/**
 * shiro 会话管理器
 * @author openguava
 *
 */
public class ShiroWebSessionManager extends DefaultWebSessionManager {

	public static final String RequestSessionCacheAttribute = "";
	
	private static final boolean DEFAULT_ENBALESESSIONCACHE = true;
	
	/**
	 * 是否允许 session 缓存
	 */
	private boolean enableSessionCache = DEFAULT_ENBALESESSIONCACHE;
	
	/**
	 * 判断是否开启临时会话缓存
	 * @return
	 */
	public boolean isEnableSessionCache() {
		return this.enableSessionCache;
	}
	
	/**
	 * 设置是否开启临时会话缓存
	 * @param enableSessionCache
	 */
	public void setEnableSessionCache(boolean enableSessionCache) {
		this.enableSessionCache = enableSessionCache;
	}
	
	public ShiroWebSessionManager() {
		super();
	}
	
	@Override
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

		//get Session Id
        Serializable sessionId = this.getSessionId(sessionKey);
        if (sessionId == null) {
        	LogUtils.debug(this.getClass(), "Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a " +
                    "session could not be found.", sessionKey);
            return null;
        }
        //当前web请求
        ServletRequest request = null;
        
        //尝试获取临时session缓存
        if(this.isEnableSessionCache() && sessionKey instanceof WebSessionKey) {
        	//获取web请求
        	request = ((WebSessionKey)sessionKey).getServletRequest();
        	if(request != null) {
        		Object sessionObj = request.getAttribute(this.getSessionCacheName(sessionId));
        		if(sessionObj instanceof Session) {
        			return (Session)sessionObj;
        		}
        	}
        }
        
        Session s = this.retrieveSessionFromDataSource(sessionId);
        if (s == null) {
            //session ID was provided, meaning one is expected to be found, but we couldn't find one:
            String msg = "Could not find session with ID [" + sessionId + "]";
            throw new UnknownSessionException(msg);
        }
        
        //尝试保存临时会话缓存
        if(this.isEnableSessionCache() && request != null) {
        	request.setAttribute(this.getSessionCacheName(sessionId), s);
        }
        
        return s;
	}
	
	/**
	 * 获取临时会话缓存名称
	 * @param sessionId
	 * @return
	 */
	protected String getSessionCacheName(Serializable sessionId) {
		return sessionId.toString();
	}
}
