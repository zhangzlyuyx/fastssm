package com.zhangzlyuyx.fastssm.shiro;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zhangzlyuyx.fastssm.base.BaseAuthenticationService;

/**
 * app 证认过滤器
 *
 */
public class AppAuthenticationFilter extends FormAuthenticationFilter {

	public static String NAME_USERTYPE = "app";
	
	public static String NAME_TOKENNAME = "accessToken";
	
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
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		
		Subject subject = this.getSubject(request, response);
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
		//获取当前请求的 url
		String requestUrl = ((HttpServletRequest)request).getRequestURL().toString();
		//获取当前 get 请求的参数
		String queryString = ((ShiroHttpServletRequest) request).getQueryString();
		if(!StringUtils.isEmpty(requestUrl)){
			requestUrl += ("?" + queryString);
		}
		//请求token
		String loginToken = ((HttpServletRequest)request).getHeader(NAME_TOKENNAME);
		if(StringUtils.isEmpty(loginToken)) {
			loginToken = request.getParameter(NAME_TOKENNAME);
		}
		//登证token
		if(StringUtils.isEmpty(loginToken)) {
			WebUtils.issueRedirect(request, response, this.getLoginUrl());
			return;
		}
		//创建token
		ShiroToken token = new ShiroToken();
		token.setUserType(this.userType);
		token.getRequestParams().put(NAME_TOKENNAME, loginToken);
		//获取证认信息
		ShiroToken authenticationInfo = this.getAuthenticationService(request.getServletContext()).getAuthenticationInfo(token);
		if(authenticationInfo == null) {
			WebUtils.issueRedirect(request, response, this.getLoginUrl());
			return;
		}
		//认证信息同步
		if(authenticationInfo.getPrincipal() == null) {
			authenticationInfo.setPrincipal(loginToken);
		}
		if(authenticationInfo.getCredentials() == null) {
			authenticationInfo.setCredentials(loginToken);
		}
		//执行授权登录
		Subject subject = this.getSubject(request, response);
		subject.login(authenticationInfo);	
		//执行重定向
		WebUtils.issueRedirect(request, response, requestUrl);
	}
}
