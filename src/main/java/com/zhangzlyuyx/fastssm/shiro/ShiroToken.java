package com.zhangzlyuyx.fastssm.shiro;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;

public class ShiroToken implements AuthenticationToken {

	private static final long serialVersionUID = 2882388334390762040L;
	
	/**
	 * userId
	 */
	private String userId;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * userName
	 */
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * userType
	 */
	private String userType;
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	 * userTag
	 */
	private Object userTag;
	
	public Object getUserTag() {
		return userTag;
	}
	
	public void setUserTag(Object userTag) {
		this.userTag = userTag;
	}
	
	/**
	 * orgId
	 */
	private String orgId;
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * orgTag
	 */
	private Object orgTag;
	
	public Object getOrgTag() {
		return orgTag;
	}
	
	public void setOrgTag(Object orgTag) {
		this.orgTag = orgTag;
	}
	
	/**
	 * deptId
	 */
	private String deptId;
	
	public String getDeptId() {
		return deptId;
	}
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	/**
	 * deptTag
	 */
	private Object deptTag;
	
	public Object getDeptTag() {
		return deptTag;
	}
	
	public void setDeptTag(Object deptTag) {
		this.deptTag = deptTag;
	}
	
	/**
	 * roles
	 */
	private Collection<String> roles;
	
	public Collection<String> getRoles() {
		return roles;
	}
	
	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
	
	/**
	 * stringPermissions
	 */
	private Collection<String> stringPermissions;
	
	public Collection<String> getStringPermissions() {
		return stringPermissions;
	}
	
	public void setStringPermissions(Collection<String> stringPermissions) {
		this.stringPermissions = stringPermissions;
	}
	
	/**
	 * objectPermissions
	 */
	private Collection<Permission> objectPermissions;
	
	public Collection<Permission> getObjectPermissions() {
		return objectPermissions;
	}
	
	public void setObjectPermissions(Collection<Permission> objectPermissions) {
		this.objectPermissions = objectPermissions;
	}
	
	/**
     * principal
     */
	private Object principal;
	
	public Object getPrincipal() {
		return this.principal;
	}
	
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	/**
	 * credentials
	 */
	private Object credentials;
	
	public Object getCredentials() {
		return this.credentials;
	}
	
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	
	/**
	 * host
	 */
	private String host;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * requestParam
	 */
	private Map<String, String> requestParams;
	
	public Map<String, String> getRequestParams() {
		if(this.requestParams == null) {
			this.requestParams = new HashMap<>();
		}
		return this.requestParams;
	}
	
	public String getRequestParam(String name){
		if(this.requestParams == null){
			return null;
		}
		for(Entry<String, String> kv : this.requestParams.entrySet()){
			if(kv.getKey().equalsIgnoreCase(name)){
				return kv.getValue();
			}
		}
		return null;
	}
	
	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}
	
	/**
	 * requestHeaders
	 */
	private Map<String, String> requestHeaders;
	
	public Map<String, String> getRequestHeaders() {
		if(this.requestHeaders == null) {
			this.requestHeaders = new HashMap<>();
		}
		return this.requestHeaders;
	}
	
	public String getRequestHeader(String name){
		if(this.requestHeaders == null){
			return null;
		}
		for(Entry<String, String> kv : this.requestHeaders.entrySet()){
			if(kv.getKey().equalsIgnoreCase(name)){
				return kv.getValue();
			}
		}
		return null;
	}
	
	public void setRequestHeaderss(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	
	/**
	 * properties
	 */
	private Map<String, String> properties;
	
	public Map<String, String> getProperties() {
		if(this.properties == null){
			this.properties = new HashMap<>();
		}
		return this.properties;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	/**
     * isAuthenticated
     */
    private boolean isAuthenticated = false;
    
    public boolean isAuthenticated() {
		return this.isAuthenticated;
	}
    
    public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
    
    /**
     * authenticateResult
     */
    private String authenticateResult;
    
    public String getAuthenticateResult() {
    	if(this.authenticateResult == null){
    		this.authenticateResult = "";
    	}
		return this.authenticateResult;
	}
    
    public void setAuthenticateResult(String authenticateResult) {
		this.authenticateResult = authenticateResult;
	}
	
    /**
     * 
     */
	public ShiroToken(){
		
	}
	
	/**
	 * 获取 ShiroToken 实例
	 * @return 返回 ShiroToken 实例
	 */
	public static ShiroToken getInstance(){
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject == null){
				return null;
			}
			if(!(subject.isAuthenticated() || subject.isRemembered())){
				return null;
			}
			Object primaryPrincipal = subject.getPrincipals().getPrimaryPrincipal();
			if(primaryPrincipal instanceof ShiroToken){
				return (ShiroToken)primaryPrincipal;
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
