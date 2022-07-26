package io.github.openguava.guavatool.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.github.openguava.guavatool.core.constant.HttpConstants;
import io.github.openguava.guavatool.core.lang.AjaxResult;
import io.github.openguava.guavatool.core.lang.Result;
import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.shiro.ShiroPrincipal;
import io.github.openguava.guavatool.shiro.ShiroToken;
import io.github.openguava.guavatool.shiro.util.JwtUtils;
import io.github.openguava.guavatool.shiro.util.ShiroUtils;
import io.github.openguava.guavatool.spring.constant.AuthConstants;
import io.github.openguava.guavatool.spring.util.SpringUtils;

/**
 * shiro 过滤器
 * @author openguava
 *
 */
public class ShiroFilter extends AuthenticatingFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilter.class);

	/**
	 * token类型
	 */
	private String tokenType = AuthConstants.DEFAULT_TOKEN_TYPE;
	
	public String getTokenType() {
		return this.tokenType;
	}
	
	public ShiroFilter setTokenType(String tokenType) {
		this.tokenType = tokenType;
		return this;
	}
	
	/**
	 * token设备
	 */
	private String tokenDevice = AuthConstants.DEFAULT_TOKEN_DEVICE;
	
	public String getTokenDevice() {
		return this.tokenDevice;
	}
	
	public ShiroFilter setTokenDevice(String tokenDevice) {
		this.tokenDevice = tokenDevice;
		return this;
	}
	
	/**
	 * 是否允许Header参数
	 */
	private boolean allowHeader = true;
	
	public boolean isAllowHeader() {
		return this.allowHeader;
	}
	
	public ShiroFilter setAllowHeader(boolean allowHeader) {
		this.allowHeader = allowHeader;
		return this;
	}
	
	/**
	 * 是否允许Cookie参数
	 */
	private boolean allowCookie = true;
	
	public boolean isAllowCookie() {
		return this.allowCookie;
	}
	
	public ShiroFilter setAllowCookie(boolean allowCookie) {
		this.allowCookie = allowCookie;
		return this;
	}
	
	/**
	 * 是否允许Url参数
	 */
	private boolean allowUrlParam = true;
	
	public boolean isAllowUrlParam() {
		return this.allowUrlParam;
	}
	
	public ShiroFilter setAllowUrlParam(boolean allowUrlParam) {
		this.allowUrlParam = allowUrlParam;
		return this;
	}
	
	/**
	 * 注销 url
	 */
	private String logoutUrl;
	
	public String getLogoutUrl() {
		return this.logoutUrl;
	}
	
	public ShiroFilter setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
		return this;
	}
	
	/**
	 * 是否为注销请求
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isLogoutRequest(ServletRequest request, ServletResponse response) {
		String logoutUrl = this.getLogoutUrl();
		if(StringUtils.isEmpty(logoutUrl)) {
			return false;
		}
		return this.pathsMatch(logoutUrl, request);
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(SpringUtils.isRequestOptionsMethod((HttpServletRequest)request)) {
        	return true;
        }
        // 允许登录请求
        if(this.isLoginRequest(request, response)) {
        	return true;
        }
        // 白名单
        if(this.isPermissive(mappedValue)) {
        	return true;
        }
        // subject
        Subject subject = ShiroUtils.getSubject();
        // 是否注销请求
        if(this.isLogoutRequest(request, response)) {
        	this.executeLogout(request, response, subject);
        	return true;
        }
        // 是否已登录
        if(!ShiroUtils.isLogin(subject)) {
        	return false;
        }
        // 获取主体信息
        Object principal = ShiroUtils.getPrincipal(subject);
        if(principal == null) {
        	this.executeLogout(request, response, subject);
        	return false;
        }
        // 验证主体信息
        if(principal instanceof ShiroPrincipal) {
        	ShiroPrincipal shiroPrincipal = (ShiroPrincipal)principal;
            if(!this.isAccessAllowed(request, response, subject, shiroPrincipal)) {
            	return false;
            }
        }
        return true;
	}

	/**
	 * 是否允许操作
	 * @param request
	 * @param response
	 * @param subject
	 * @param principal
	 * @return
	 */
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Subject subject, ShiroPrincipal principal) {
		// shiroToken
		ShiroToken shiroToken = ShiroUtils.getShiroToken(principal);
		if(shiroToken == null) {
			this.executeLogout(request, response, subject);
			return false;
		}
		// 读取token值
		String tokenValue = this.readTokenValue(request);
		if(StringUtils.isEmpty(tokenValue)) {
			this.executeLogout(request, response, subject);
			return false;
		}
		// 验证 读取的token值是否与当前用户匹配
		if(shiroToken.getTokenValue() == null || !tokenValue.equalsIgnoreCase(shiroToken.getTokenValue().toString())) {
			this.executeLogout(request, response, subject);
			return false;
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = this.readTokenValue(request);
        if(StringUtils.isBlank(token)){
            this.onNotLogin(request, response, null);
            return false;
        }
        // 执行登录
        return this.executeLogin(request, response);
	}
	
	/**
	 * 执行登录
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationToken token = null;
		try {
        	token = this.createToken(request, response);
            if (token == null) {
                String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                        "must be created in order to execute a login attempt.";
                throw new IllegalStateException(msg);
            }
            Subject subject = this.getSubject(request, response);
            subject.login(token);
            return this.onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return this.onLoginFailure(token, e, request, response);
        }
	}
	
	/**
	 * 执行注销
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean executeLogout(ServletRequest request, ServletResponse response, Subject subject) {
		// shiro 注销
		if(ShiroUtils.isLogin(subject)) {
			ShiroUtils.logout(subject);
		}
		// 判断是否清理cookie
		if(this.isAllowCookie()) {
			String tokenName = ShiroUtils.getShiroConfigHandler().getTokenName();
			SpringUtils.setRequestCookie((HttpServletResponse)response, tokenName, null, null, 0);
		}
		return false;
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// 读取 token 值
		String tokenValue = this.readTokenValue(request);
		if(StringUtils.isEmpty(tokenValue)) {
			return null;
		}
		// 根据密钥 jwt 校验
		String tokenSecret = ShiroUtils.getShiroConfigHandler().getTokenSecret();
		Result<DecodedJWT> retVerify = JwtUtils.verifyWithHS512(tokenSecret, tokenValue);
		if(!retVerify.isSuccess()) {
			throw new AuthenticationException(retVerify.getMsg());
		}
		ShiroToken shiroToken = new ShiroToken(tokenValue);
		shiroToken.setTokenApp(ShiroUtils.getShiroConfigHandler().getTokenApp());
		shiroToken.setTokenType(this.getTokenType());
		shiroToken.setTokenDevice(this.getTokenDevice());
		shiroToken.setTokenName(ShiroUtils.getShiroConfigHandler().getTokenName());
		shiroToken.setTokenTimeout(ShiroUtils.getShiroConfigHandler().getTokenTimeout());
		return shiroToken;
	}
	
	/**
	 * 登录成功
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 登录失败
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
        // 处理登录失败的异常
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        this.onNotLogin(request, response, throwable.getMessage());
        return false;
	}

	/**
	 * 未登录
	 * @param request
	 * @param response
	 * @param message
	 */
	protected void onNotLogin(ServletRequest request, ServletResponse response, String message) {
		// GET请求如存在loginUrl，则重定向
		if(SpringUtils.isRequestGetMethod((HttpServletRequest)request) && !StringUtils.isEmpty(this.getLoginUrl())) {
			try {
				this.redirectToLogin(request, response);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			return;
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader(HttpConstants.HTTP_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        httpResponse.setHeader(HttpConstants.HTTP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, SpringUtils.getRequestHeader((HttpServletRequest)request, HttpConstants.HTTP_HEADER_ORIGIN));
        AjaxResult ajaxJson = AjaxResult.unauthorized(message);
		String json = JSONObject.toJSONString(ajaxJson);
		try {
			response.setContentType(HttpConstants.HTTP_CONTENTTYPE_APPLICATION_JSON_UTF8);
			response.getWriter().print(json);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 读取 token 值
	 * @param request
	 * @return
	 */
	protected String readTokenValue(ServletRequest request) {
		String tokenName = ShiroUtils.getShiroConfigHandler().getTokenName();
		String tokenValue = SpringUtils.getRequestToken((HttpServletRequest)request, tokenName, this.allowHeader, this.allowCookie, this.allowUrlParam);
		return tokenValue;
	}
}
