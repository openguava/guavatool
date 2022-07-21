package io.github.openguava.guavatool.shiro.common;

import io.github.openguava.guavatool.core.serializer.Serializer;
import io.github.openguava.guavatool.shiro.serializer.ObjectSerializer;
import io.github.openguava.guavatool.shiro.serializer.StringSerializer;

/**
 * shiro session配置
 * @author openguava
 *
 */
public abstract class AbstractShiroSessionConfig {

	/** 默认shiro会话前缀 */
	private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";
	
	/** 默认shiro会话内存超时时间 */
	private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
	
	private static final boolean DEFAULT_SESSION_IN_MEMORY_ENABLED = true;
	
	public static final int DEFAULT_EXPIRE = -2;
	
	public static final int NO_EXPIRE = -1;
	
	/** key序列化 */
	private Serializer<String, byte[]> keySerializer = new StringSerializer();
	
	public Serializer<String, byte[]> getKeySerializer() {
		return this.keySerializer;
	}
	
	public AbstractShiroSessionConfig setKeySerializer(Serializer<String, byte[]> keySerializer) {
		this.keySerializer = keySerializer;
		return this;
	}
	
	/** 值序列化 */
	private Serializer<Object, byte[]> valueSerializer = new ObjectSerializer();
	
	public Serializer<Object, byte[]> getValueSerializer() {
		return this.valueSerializer;
	}
	
	public AbstractShiroSessionConfig setValueSerializer(Serializer<Object, byte[]> valueSerializer) {
		this.valueSerializer = valueSerializer;
		return this;
	}
	
	/** 请保证有效期时间大于会话超时时间 */
	private int expire = DEFAULT_EXPIRE;
	
	public int getExpire() {
		return this.expire;
	}
	
	public AbstractShiroSessionConfig setExpire(int expire) {
		this.expire = expire;
		return this;
	}
	
	/** 会话key前缀 */
	private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;
	
	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public AbstractShiroSessionConfig setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
		return this;
	}
	
	/** 是否启用shiro会话内存缓存 */
	private boolean sessionInMemoryEnabled = DEFAULT_SESSION_IN_MEMORY_ENABLED;
	
	public boolean getSessionInMemoryEnabled() {
		return this.sessionInMemoryEnabled;
	}

	public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
		this.sessionInMemoryEnabled = sessionInMemoryEnabled;
	}
	
	/** 内存会话超时时间 */
	private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;

	public long getSessionInMemoryTimeout() {
		return this.sessionInMemoryTimeout;
	}

	public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
		this.sessionInMemoryTimeout = sessionInMemoryTimeout;
	}
	
	/**
	 * 克隆配置
	 * @param config
	 */
	public AbstractShiroSessionConfig clone(AbstractShiroSessionConfig config) {
		if(config == null) {
			return this;
		}
		this.setKeySerializer(config.getKeySerializer());
		this.setValueSerializer(config.getValueSerializer());
		this.setExpire(config.getExpire());
		this.setKeyPrefix(config.getKeyPrefix());
		return this;
	}
}
