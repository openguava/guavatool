package io.github.openguava.guavatool.shiro.common;

import io.github.openguava.guavatool.core.serializer.Serializer;
import io.github.openguava.guavatool.shiro.serializer.ObjectSerializer;
import io.github.openguava.guavatool.shiro.serializer.StringSerializer;

/**
 * shiro 缓存配置
 * @author openguava
 *
 */
public abstract class AbstractShiroCacheConfig {

	public static final int DEFAULT_EXPIRE = 1800;
	
	public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
	
	public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "userId";
	
	/** key序列化 */
	private Serializer<String, byte[]> keySerializer = new StringSerializer();
	
	public Serializer<String, byte[]> getKeySerializer() {
		return this.keySerializer;
	}
	
	public AbstractShiroCacheConfig setKeySerializer(Serializer<String, byte[]> keySerializer) {
		this.keySerializer = keySerializer;
		return this;
	}
	
	/** 值序列化 */
	private Serializer<Object, byte[]> valueSerializer = new ObjectSerializer();
	
	public Serializer<Object, byte[]> getValueSerializer() {
		return this.valueSerializer;
	}
	
	public AbstractShiroCacheConfig setValueSerializer(Serializer<Object, byte[]> valueSerializer) {
		this.valueSerializer = valueSerializer;
		return this;
	}
	
	/** 有效时间*/
	private int expire = DEFAULT_EXPIRE;
	
	public int getExpire() {
		return this.expire;
	}
	
	public AbstractShiroCacheConfig setExpire(int expire) {
		this.expire = expire;
		return this;
	}
	
	/** key前缀 */
	private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;
	
	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public AbstractShiroCacheConfig setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
		return this;
	}
	
	/** 认证主体id字段名称 */
	private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;

	public String getPrincipalIdFieldName() {
		return this.principalIdFieldName;
	}

	public AbstractShiroCacheConfig setPrincipalIdFieldName(String principalIdFieldName) {
		this.principalIdFieldName = principalIdFieldName;
		return this;
	}
	
	/**
	 * 克隆配置
	 * @param config
	 */
	public AbstractShiroCacheConfig clone(AbstractShiroCacheConfig config) {
		if(config == null) {
			return this;
		}
		this.setKeySerializer(config.getKeySerializer());
		this.setValueSerializer(config.getValueSerializer());
		this.setExpire(config.getExpire());
		this.setKeyPrefix(config.getKeyPrefix());
		this.setPrincipalIdFieldName(config.getPrincipalIdFieldName());
		return this;
	}
}
