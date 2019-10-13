package com.zhangzlyuyx.fastssm.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroRealm extends AuthorizingRealm {

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException { 
		if(token == null) {
			return null;
		}
		if(!(token instanceof ShiroToken)) {
			throw new UnsupportedTokenException("Unsupported Token:" + token.getClass().getName());
		}
		try {
			ShiroToken shiroToken = (ShiroToken)token;
			if(!shiroToken.isAuthenticated()) {
				throw new AuthenticationException(shiroToken.getAuthenticateResult());
			}
			if(shiroToken.getPrincipal() == null) {
				Object credentials = shiroToken.getCredentials();
				if(credentials != null){
					throw new AuthenticationException(shiroToken.getCredentials().toString());
				}else{
					throw new AuthenticationException("Authentication Principal Not Allow Empty!");
				}
			}
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(shiroToken, shiroToken.getCredentials(), this.getName());
			return authenticationInfo;
		} catch (AuthenticationException e) {
			throw e;
		}
	}
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroToken token = (ShiroToken)principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//roles
		if(token.getRoles() != null) {
			authorizationInfo.addRoles(token.getRoles());
		}
		//stringPermissions
		if(token.getStringPermissions() != null) {
			authorizationInfo.addStringPermissions(token.getStringPermissions());
		}
		//objectPermissions
		if(token.getObjectPermissions() != null) {
			authorizationInfo.addObjectPermissions(token.getObjectPermissions());
		}
		return authorizationInfo;
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return (token instanceof ShiroToken) || super.supports(token);
	}
}
