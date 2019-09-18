package com.zhangzlyuyx.fastssm.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroToken extends UsernamePasswordToken {

	private String usertype;
	
	public String getUsertype() {
		return usertype;
	}
	
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	
	public ShiroToken(){
		
	}
}
