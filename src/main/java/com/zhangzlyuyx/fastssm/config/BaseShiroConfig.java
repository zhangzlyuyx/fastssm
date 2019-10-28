package com.zhangzlyuyx.fastssm.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.zhangzlyuyx.fastssm.shiro.AppAuthenticationFilter;
import com.zhangzlyuyx.fastssm.shiro.ShiroRealm;
import com.zhangzlyuyx.fastssm.shiro.UserAuthenticationFilter;
import com.zhangzlyuyx.fastssm.shiro.WeixinAuthenticationFilter;

/**
 * @Configuration
 *
 */
public class BaseShiroConfig {

	@Bean("shiroRealm")
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }
	
	@Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }
	
	 @Bean("shiroFilterFactory")
	 public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
		 Map<String, String> filterChainDefinitionMap = this.createFilterChainDefinitionMap();
		 ShiroFilterFactoryBean shiroFilterFactory = this.createShiroFilterFactory(securityManager);
		 shiroFilterFactory.setFilterChainDefinitionMap(filterChainDefinitionMap);
		 return shiroFilterFactory;
	 }
	
	/**
	 * 创建 过滤器链接定义
	 * @return
	 */
	protected Map<String, String> createFilterChainDefinitionMap(){
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/app/**", "app");
        filterChainDefinitionMap.put("/weixin/**", "app");
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
	}
	
	/**
	 * 创建 shiroFilterFactory
	 * @param securityManager
	 * @return
	 */
	protected ShiroFilterFactoryBean createShiroFilterFactory(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactory = new ShiroFilterFactoryBean();
        shiroFilterFactory.setSecurityManager(securityManager);
        shiroFilterFactory.setLoginUrl("/login");
        shiroFilterFactory.setSuccessUrl("/index");
        
        //用户登录过滤器
        UserAuthenticationFilter userAuthenticationFilter = this.createUserAuthenticationFilter();
        shiroFilterFactory.getFilters().put("authc", userAuthenticationFilter);
        shiroFilterFactory.getFilters().put("user", userAuthenticationFilter);
        
        //app认证过滤器
        shiroFilterFactory.getFilters().put("app", this.createAppAuthenticationFilter());
        
        //微信认证过滤器
        shiroFilterFactory.getFilters().put("weixin", this.createWeixinAuthenticationFilter());
        
        return shiroFilterFactory;
	}
	
	/**
	 * 创建用户登录过滤器
	 * @return
	 */
	protected UserAuthenticationFilter createUserAuthenticationFilter() {
		UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
		return userAuthenticationFilter;
	}
	
	/**
	 * 创建app登录过滤器
	 * @return
	 */
	protected AppAuthenticationFilter createAppAuthenticationFilter() {
		AppAuthenticationFilter appAuthenticationFilter = new AppAuthenticationFilter();
		return appAuthenticationFilter;
	}
	
	/**
	 * 创建微信oauth登录过滤器
	 * @return
	 */
	protected WeixinAuthenticationFilter createWeixinAuthenticationFilter() {
		WeixinAuthenticationFilter weixinAuthenticationFilter = new WeixinAuthenticationFilter();
		return weixinAuthenticationFilter;
	}
}
