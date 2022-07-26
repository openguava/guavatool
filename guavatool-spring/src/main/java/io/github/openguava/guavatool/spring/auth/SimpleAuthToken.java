package io.github.openguava.guavatool.spring.auth;

import java.util.HashMap;
import java.util.Map;

import io.github.openguava.guavatool.spring.constant.AuthConstants;

/**
 * 认证 token信息
 * @author openguava
 *
 */
public class SimpleAuthToken implements AuthToken {

	private static final long serialVersionUID = 1L;
	
	public SimpleAuthToken() {
		
	}
	
	public SimpleAuthToken(Object value) {
		this.setTokenValue(value);
	}
	
	/**
	 * token 应用
	 */
	protected String tokenApp = AuthConstants.DEFAULT_TOKEN_APP;
	
	public String getTokenApp() {
		return this.tokenApp;
	}
	
	public void setTokenApp(String tokenApp) {
		this.tokenApp = tokenApp;
	}
	
	/**
	 * token 类型
	 */
	protected String tokenType = AuthConstants.DEFAULT_TOKEN_TYPE;
	
	public String getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	/**
	 * token设备
	 */
	protected String tokenDevice = AuthConstants.DEFAULT_TOKEN_DEVICE;
	
	public String getTokenDevice() {
		return tokenDevice;
	}
	
	public void setTokenDevice(String tokenDevice) {
		this.tokenDevice = tokenDevice;
	}
	
	/**
	 * token 名称
	 */
	protected String tokenName = AuthConstants.DEFAULT_TOKEN_NAME;
	
	public String getTokenName() {
		return this.tokenName;
	}
	
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	/**
	 * token value
	 */
	protected Object tokenValue;

	public Object getTokenValue() {
		return this.tokenValue;
	}
	
	public void setTokenValue(Object tokenValue) {
		this.tokenValue = tokenValue;
	}
	
	/**
	 * token 超时时间(秒)
	 */
	protected int tokenTimeout = AuthConstants.DEFAULT_TOKEN_TIMEOUT;
	
	public int getTokenTimeout() {
		return this.tokenTimeout;
	}
	
	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}
	
	/**
	 * token主题信息
	 */
	protected String tokenSubject;
	
	public String getTokenSubject() {
		return tokenSubject;
	}
	
	public void setTokenSubject(String tokenSubject) {
		this.tokenSubject = tokenSubject;
	}
	
	/**
	 * 附加属性集合
	 */
	protected Map<String, Object> attributes;
	
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Object getAttributeValue(String key) {
		return this.getAttributes().get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttributeValue(String key, Class<T> cls) {
		Object value = this.getAttributeValue(key);
		return (value != null && cls.isInstance(value)) ? (T)value : null;
	}
	
	public void setAttributeValue(String key, Object value) {
		this.getAttributes().put(key, value);
	}
}
