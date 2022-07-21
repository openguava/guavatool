package io.github.openguava.guavatool.shiro.common;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.openguava.guavatool.core.exception.SerializationException;
import io.github.openguava.guavatool.core.util.LogUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 会话数据访问
 * @author openguava
 *
 */
public class AbstractSessionDAO extends org.apache.shiro.session.mgt.eis.AbstractSessionDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSessionDAO.class);
	
	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	
	private AbstractShiroSessionConfig config;
	
	public AbstractShiroSessionConfig getConfig() {
		if(this.config == null) {
			this.config = new AbstractShiroSessionConfig() {
				
			};
		}
		return this.config;
	}
	
	public void setConfig(AbstractShiroSessionConfig config) {
		this.config = config;
	}


	/** redis 管理器 */
	private AbstractShiroCacheDao dao;
	
	public AbstractShiroCacheDao getDao() {
		return dao;
	}
	
	public AbstractSessionDAO setDao(AbstractShiroCacheDao cacheDao) {
		this.dao = cacheDao;
		return this;
	}
	
	/** 线程变量存储 */
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal sessionsInThread = new ThreadLocal();
	
	@Override
	protected void assignSessionId(Session session, Serializable sessionId) {
		((SimpleSession) session).setId(sessionId);
	}
	
	/**
	 * 创建会话
	 */
	@Override
	protected Serializable doCreate(Session session) {
		if (session == null) {
			LOGGER.error("session is null");
			throw new UnknownSessionException("session is null");
		}
		Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
		return sessionId;
	}
	
	/**
	 * 更新会话
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
		if (this.config.getSessionInMemoryEnabled()) {
			this.setSessionToThreadLocal(session.getId(), session);
		}
	}
	
	/**
	 * 保存会话
	 * @param session
	 * @throws UnknownSessionException
	 */
	protected void saveSession(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			throw new UnknownSessionException("session or session id is null");
		}
		byte[] key;
		byte[] value;
		try {
			key = this.config.getKeySerializer().serialize(this.getSessionKey(session.getId()));
			value = this.config.getValueSerializer().serialize(session);
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "serialize session error. session id=" + session.getId());
			throw new UnknownSessionException(e);
		}
		if (this.config.getExpire() == AbstractShiroSessionConfig.DEFAULT_EXPIRE) {
			this.dao.put(key, value, (int) (session.getTimeout() / MILLISECONDS_IN_A_SECOND));
			return;
		}
		if (this.config.getExpire() != AbstractShiroSessionConfig.NO_EXPIRE && this.config.getExpire() * MILLISECONDS_IN_A_SECOND < session.getTimeout()) {
			LogUtils.warn(this.getClass(), "Redis session expire time: "
					+ (this.config.getExpire() * MILLISECONDS_IN_A_SECOND)
					+ " is less than Session timeout: "
					+ session.getTimeout()
					+ " . It may cause some problems.");
		}
		this.dao.put(key, value, this.config.getExpire());
	}
	

	/**
	 * 读取会话
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			LOGGER.warn("session id is null");
			return null;
		}
		if(this.config.getSessionInMemoryEnabled()) {
			Session session = getSessionFromThreadLocal(sessionId);
			if (session != null) {
				return session;
			}
		}

		Session session = null;
		LOGGER.debug("read session from redis");
		try {
			byte[] value = this.dao.get(this.config.getKeySerializer().serialize(this.getSessionKey(sessionId)));
			session = (Session)this.config.getValueSerializer().deserialize(value);
			if (this.config.getSessionInMemoryEnabled()) {
				setSessionToThreadLocal(sessionId, session);
			}
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "read session error. settionId=" + sessionId);
		}
		return session;
	}
	
	/**
	 * 删除会话
	 */
	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			LOGGER.error("session or session id is null");
			return;
		}
		try {
			this.dao.remove(config.getKeySerializer().serialize(this.getSessionKey(session.getId())));
		} catch (SerializationException e) {
			LogUtils.error(this.getClass(), "delete session error. session id=" + session.getId());
		}
	}

	/**
	 * 获取有效会话
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new LinkedHashSet<>();
		try {
			Set<byte[]> keys = this.dao.keys(this.config.getKeySerializer().serialize(this.config.getKeyPrefix() + "*"));
			if (keys != null && !keys.isEmpty()) {
				for (byte[] key:keys) {
					Session s = (Session)this.config.getValueSerializer().deserialize(this.dao.get(key));
					sessions.add(s);
				}
			}
		} catch (SerializationException e) {
			LOGGER.error("get active sessions error.");
		}
		return sessions;
	}

	/**
	 * 设置会话线程变量存储
	 * @param sessionId
	 * @param s
	 */
	@SuppressWarnings("unchecked")
	protected void setSessionToThreadLocal(Serializable sessionId, Session s) {
		Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
		if (sessionMap == null) {
            sessionMap = new HashMap<>();
            sessionsInThread.set(sessionMap);
        }
		SessionInMemory sessionInMemory = new SessionInMemory();
		sessionInMemory.setCreateTime(new Date());
		sessionInMemory.setSession(s);
		sessionMap.put(sessionId, sessionInMemory);
	}

	/**
	 * 从线程变量获取会话
	 * @param sessionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Session getSessionFromThreadLocal(Serializable sessionId) {
		Session s = null;

		if (sessionsInThread.get() == null) {
			return null;
		}

		Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
		SessionInMemory sessionInMemory = sessionMap.get(sessionId);
		if (sessionInMemory == null) {
			return null;
		}
		Date now = new Date();
		long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
		if (duration < this.config.getSessionInMemoryTimeout()) {
			s = sessionInMemory.getSession();
			LOGGER.debug("read session from memory");
		} else {
			sessionMap.remove(sessionId);
		}
		return s;
	}

	/**
	 * 获取会话key
	 * @param sessionId
	 * @return
	 */
	protected String getSessionKey(Serializable sessionId) {
		return this.config.getKeyPrefix() + sessionId;
	}
}
