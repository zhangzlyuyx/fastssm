package com.zhangzlyuyx.fastssm.base;

import com.zhangzlyuyx.fastssm.shiro.ShiroToken;

/**
 * 通用认证服务接口
 *
 */
public interface BaseAuthenticationService {
	
	ShiroToken getAuthenticationInfo(ShiroToken token);
}
