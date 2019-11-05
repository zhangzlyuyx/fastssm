package com.zhangzlyuyx.fastssm.shiro;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.zhangzlyuyx.fastssm.base.BaseAuthenticationService;
import com.zhangzlyuyx.fastssm.util.LogUtils;

/**
 * 微信 OAuth 2.0 过滤器
 *
 */
public class WeixinAuthenticationFilter extends FormAuthenticationFilter {

	public static String NAME_USER_TYPE = "weixin";
	
	public static String NAME_OPENID = "openid";
	
	public static String NAME_ACCESSTOKEN = "accessToken";
	
	public static String NAME_USERINFO = "userInfo";
	
	/**
	 * 用户类型
	 */
	private String userType = NAME_USER_TYPE;
	
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
	
	/**
	 * 微信 OAuth 2.0 网页授权地址 
	 */
	private static String OATUH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
	
	/**
	 * 微信 OAuth 2.0 access_token地址
	 */
	private static String OATUH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	/**
	 *  微信 OAuth 2.0 拉取用户信息(需scope为 snsapi_userinfo)
	 */
	private static String OATUH2_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	
	/**
	 * 公众号的唯一标识
	 */
	protected String appId;
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * 公众号的appsecret
	 */
	protected String appSecret;
	
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	/**
	 * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 */
	protected String state = "weixin";
	
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * 应用授权作用域。
	 */
	protected String scope = "snsapi_base";
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * accessToken 名称
	 */
	protected String accessTokenName = NAME_ACCESSTOKEN;
	
	public void setAccessTokenName(String accessTokenName) {
		this.accessTokenName = accessTokenName;
	}
	
	/**
	 * openid 名称
	 */
	protected String openIdName = NAME_OPENID;
	
	public void setOpenIdName(String openIdName) {
		this.openIdName = openIdName;
	}
	
	/**
	 * userInfo 名称
	 */
	protected String userInfoName = NAME_USERINFO;
	
	public void setUserInfoName(String userInfoName) {
		this.userInfoName = userInfoName;
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
		// 获取当前请求的 url
		String requestUrl = ((HttpServletRequest)request).getRequestURL().toString();
		// 获取当前 get 请求的参数
		String queryString = ((ShiroHttpServletRequest) request).getQueryString();
		if(!StringUtils.isEmpty(requestUrl)){
			requestUrl += ("?" + queryString);
		}
		// 获取微信重定向 code 和 state
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		// 网页授权登录请求处理
		if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
			String loginUrl = this.getOAuthAuthorizeUrlUrl(requestUrl, this.state);
			WebUtils.issueRedirect(request, response, loginUrl);
			return;
		}
		// 获取网页授权 access_token
		Map<String, Object> accessTokenMap = this.getOAuthAccessToken(code);
		// 如果授权失败，则重新请求网页授权
		if(accessTokenMap.containsKey("errcode")){
			super.redirectToLogin(request, response);
			return;
		}
		//获取用户信息
		Map<String, Object> userInfoMap = this.getUserInfo(accessTokenMap.get("access_token").toString(), accessTokenMap.get("openid").toString());
		//获取openid
		String openid = accessTokenMap.get("openid").toString();
		ShiroToken token = new ShiroToken();
		token.setUserType(this.userType);
		token.getRequestParams().put("redirect", requestUrl);
		token.getRequestParams().put(this.openIdName, openid);
		token.getRequestParams().put(this.accessTokenName, JSON.toJSONString(accessTokenMap));
		token.getRequestParams().put(this.userInfoName, JSON.toJSONString(userInfoMap));
		//获取证认信息
		ShiroToken authenticationInfo = this.getAuthenticationService(request.getServletContext()).getAuthenticationInfo(token);
		if(authenticationInfo == null) {
			super.redirectToLogin(request, response);
			return;
		}
		//认证信息同步
		if(authenticationInfo.getPrincipal() == null) {
			authenticationInfo.setPrincipal(openid);
		}
		if(authenticationInfo.getCredentials() == null) {
			authenticationInfo.setCredentials(openid);
		}
		//执行授权登录
		Subject subject = this.getSubject(request, response);
		subject.login(authenticationInfo);	
		//执行重定向
		WebUtils.issueRedirect(request, response, requestUrl);
	}
	
	/**
	 * 获取网页授权地址
	 * @param redirect_uri 授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理
	 * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * @return
	 */
	private String getOAuthAuthorizeUrlUrl(String redirect_uri, String state){
		if(!StringUtils.isEmpty(redirect_uri)){
			redirect_uri = URLEncoder.encode(redirect_uri);
		}
		String authorizeUrl = String.format(OATUH2_AUTHORIZE_URL, this.appId, redirect_uri, this.scope, state);
		return authorizeUrl;
	}
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code 网页授权重定向的code参数
	 * @return
	 */
	protected Map<String, Object> getOAuthAccessToken(String code){
		String url = String.format(OATUH2_ACCESS_TOKEN_URL, this.appId, this.appSecret, code);
		Map<String, Object> map = new HashMap<>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> exchange = restTemplate.getForEntity(url, String.class);
			String json = exchange.getBody();
			LogUtils.info(this.getClass(), "OATUH2_ACCESS_TOKEN:{}", json);
			map = (Map<String, Object>)JSON.parse(json);
			return map;
		} catch (Exception e) {
			LogUtils.error(this.getClass(), "OATUH2_ACCESS_TOKEN:", e);
			map.put("errcode", "-1");
			map.put("errmsg", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 获取用户信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	protected Map<String, Object> getUserInfo(String accessToken, String openId){
		String url = String.format(OATUH2_USERINFO_URL, accessToken, openId);
		Map<String, Object> map = new HashMap<>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> exchange = restTemplate.getForEntity(url, String.class);
			String json = exchange.getBody();
			LogUtils.info(this.getClass(), "OATUH2_USERINFO:{}", json);
			map = (Map<String, Object>)JSON.parse(json);
			return map;
		} catch (Exception e) {
			LogUtils.error(this.getClass(), "OATUH2_USERINFO:", e);
			map.put("errcode", "-1");
			map.put("errmsg", e.getMessage());
			return map;
		}
	}
}
