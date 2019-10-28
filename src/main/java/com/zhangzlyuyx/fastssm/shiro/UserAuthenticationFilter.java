package com.zhangzlyuyx.fastssm.shiro;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zhangzlyuyx.fastssm.base.BaseAuthenticationService;

/**
 * 用户认证过滤器
 *
 */
public class UserAuthenticationFilter extends FormAuthenticationFilter {

	public static String NAME_USERTYPE = "user";
	
	public static String NAME_USERNAME = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM;
	
	public static String NAME_PASSWORD = FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM;
	
	public static String NAME_REMEMBER = FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM;
	
	/**
	 * 用户类型
	 */
	private String userType = NAME_USERTYPE;
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	 * 通用认证服务接口
	 */
	@Autowired(required = false)
	private BaseAuthenticationService authenticationService;
	
	public BaseAuthenticationService getAuthenticationService(ServletContext servletContext) {
		if(this.authenticationService == null) {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    		this.authenticationService = webApplicationContext.getBean(BaseAuthenticationService.class);
		}
		return this.authenticationService;
	}
	
	public void setAuthenticationService(BaseAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = SecurityUtils.getSubject();
		//获取认证状态/记住状态
		boolean accessAllowed = subject.isAuthenticated() || subject.isRemembered();
		if(!accessAllowed){
			return false;
		}
		//获取认证令牌
		ShiroToken token = (ShiroToken)subject.getPrincipal();
		if(token == null){
			return false;
		}
		//验证用户类型
		if(!this.userType.equalsIgnoreCase(token.getUserType())){
			subject.logout();
			return false;
		}
		return true;
	}
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		super.redirectToLogin(request, response);
	}
	
	@Override
	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
		if(StringUtils.isEmpty(username)){
			throw new AuthenticationException("请输入用户名!");
		}
		if(StringUtils.isEmpty(password)){
			throw new AuthenticationException("请输入密码!");
		}
		ShiroToken token = new ShiroToken();
		token.setUserType(this.userType);
		token.setHost(host);
		token.getRequestParams().put(getUsernameParam(), username);
		token.getRequestParams().put(getPasswordParam(), password);
		token.getRequestParams().put(getRememberMeParam(), String.valueOf(rememberMe));
		return token;
	}
	
	@Override
	public String getUsernameParam() {
		return NAME_USERNAME;
	}
	
	@Override
	public String getPasswordParam() {
		return NAME_PASSWORD;
	}
	
	@Override
	public String getRememberMeParam() {
		return NAME_REMEMBER;
	}
	
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		try {
			String host = this.getHost(request);
			String username = this.getUsername(request);
            String password = this.getPassword(request);
            String rememberMeValue = WebUtils.getCleanParam(request, getRememberMeParam());
            boolean rememberMe = !StringUtils.isEmpty(rememberMeValue) && (rememberMeValue.equals("1") || rememberMeValue.equalsIgnoreCase("true"));
            ShiroToken token = (ShiroToken)this.createToken(username, password, rememberMe, host);
            //token请求参数
            Enumeration<String> parameterNames = request.getParameterNames();
        	while(parameterNames.hasMoreElements()){
        		String parameterName = parameterNames.nextElement();
        		String parameterValue = WebUtils.getCleanParam(request, parameterName);
        		token.getRequestParams().put(parameterName, parameterValue);
        	}
        	//获取认证信息
        	ShiroToken authenticationInfo = this.getAuthenticationService(request.getServletContext()).getAuthenticationInfo(token);
        	if(authenticationInfo == null || !authenticationInfo.isAuthenticated()) {
        		return onLoginFailure(authenticationInfo, new AuthenticationException(authenticationInfo != null ? authenticationInfo.getAuthenticateResult() : ""), request, response);
        	}
        	if(authenticationInfo.getPrincipal() == null) {
        		authenticationInfo.setPrincipal(authenticationInfo.getUserId());
        	}
        	if(authenticationInfo.getCredentials() == null) {
        		authenticationInfo.setCredentials(authenticationInfo.getUserId());
        	}
        	Subject subject = getSubject(request, response);
            subject.login(authenticationInfo);
            //如果当前为get请求，则执行url重定向
    		if(((HttpServletRequest)request).getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)){
    			return super.onLoginSuccess(token, subject, request, response);
    		}else {
    			return true;
    		}
		} catch (AuthenticationException e) {
			return onLoginFailure(null, e, request, response);
		}
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		request.setAttribute(this.getFailureKeyAttribute(), null);
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		request.setAttribute(this.getFailureKeyAttribute(), e);
		return true;
	}
}
