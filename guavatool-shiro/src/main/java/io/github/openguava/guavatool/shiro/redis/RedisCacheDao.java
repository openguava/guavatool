package io.github.openguava.guavatool.shiro.redis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import io.github.openguava.guavatool.shiro.ShiroCacheDao;
import io.github.openguava.guavatool.spring.util.RedisUtils;

/**
 * redis 缓存数据访问
 *
 */
public class RedisCacheDao implements ShiroCacheDao {

	private RedisConnectionFactory redisConnectionFactory;
	
	public RedisConnectionFactory getRedisConnectionFactory() {
		if(this.redisConnectionFactory == null) {
			this.redisConnectionFactory = RedisUtils.getRedisConnectionFactory();
		}
		return this.redisConnectionFactory;
	}
	
	public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}
	
	private RedisTemplate<byte[], byte[]> redisTemplate;
	
	public RedisTemplate<byte[], byte[]> getRedisTemplate() {
		if(this.redisTemplate == null) {
			this.redisTemplate = RedisUtils.createRedisTemplate(getRedisConnectionFactory() ,byte[].class, byte[].class);
		}
		return this.redisTemplate;
	}
	
	public RedisCacheDao setRedisTemplate(RedisTemplate<byte[], byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
		return this;
	}
	
	/***
	 * 字符集
	 */
	private Charset charset = StandardCharsets.UTF_8;
	
	public Charset getCharset() {
		return this.charset;
	}
	
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	@Override
	public byte[] get(byte[] key) {
		return RedisUtils.getBytes(this.getRedisTemplate(), key);
	}

	@Override
	public byte[] put(byte[] key, byte[] value, int expire) {
		RedisUtils.setBytes(this.getRedisTemplate(), key, value, expire);
		return value;
	}

	@Override
	public void remove(byte[] key) {
		RedisUtils.delete(this.getRedisTemplate(), key);
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		return RedisUtils.keys(this.getRedisTemplate(), pattern);
	}
	
	@Override
	public int size(byte[] pattern) {
		Long sz = RedisUtils.dbSize(this.getRedisTemplate());
		return sz.intValue();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}
}
