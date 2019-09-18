package com.zhangzlyuyx.fastssm.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * shiro 用户
 *
 */
public class ShiroUser implements Serializable {

	private static final long serialVersionUID = 4398710242712408130L;

	/**
	 * 获取 shiroUser 实例
	 * @return
	 */
	public static ShiroUser getInstance(){
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject == null){
				return null;
			}
			if(!(subject.isAuthenticated() || subject.isRemembered())){
				return null;
			}
			Object primaryPrincipal = subject.getPrincipals().getPrimaryPrincipal();
			if(primaryPrincipal instanceof ShiroUser){
				return (ShiroUser)primaryPrincipal;
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户名
	 */
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 用户类型
	 */
	private String userType;
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}
	
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	private Long deptId;
	
	public Long getDeptId() {
		return deptId;
	}
	
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	/**
	 * 资源
	 */
	private List<String> resources;
	
	public List<String> getResources() {
		if(resources == null){
			resources = new ArrayList<>();
		}
		return resources;
	}
	
	public void setResources(List<String> resources) {
		this.resources = resources;
	}
}
