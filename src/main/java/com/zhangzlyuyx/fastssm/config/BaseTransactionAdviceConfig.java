package com.zhangzlyuyx.fastssm.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * aop切面方式事物配置
 * @Aspect
 * @Configuration
 *
 */
public class BaseTransactionAdviceConfig {

	/**
	 * transactionManager
	 */
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	/**
	 * REQUIRED
	 */
	protected DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute(Propagation.REQUIRED.value());
	
	/**
	 * SUPPORTS
	 */
	protected DefaultTransactionAttribute txAttr_SUPPORTS = new DefaultTransactionAttribute(Propagation.SUPPORTS.value());
	
	/**
	 * Advice
	 * @return
	 */
	@Bean
    public TransactionInterceptor txAdvice() {
		NameMatchTransactionAttributeSource source = this.getTransactionAttributeSource();
		return new TransactionInterceptor(transactionManager, source);
	}
	
	/**
	 * Advisor
	 * @return
	 */
	@Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(this.getPointcutExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
	
	/**
	 * 获取事务属性
	 * @return
	 */
	protected NameMatchTransactionAttributeSource getTransactionAttributeSource() {
		
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		
		source.addTransactionalMethod("save*", txAttr_REQUIRED);
		source.addTransactionalMethod("delete*", txAttr_REQUIRED);
		source.addTransactionalMethod("update*", txAttr_REQUIRED);
		source.addTransactionalMethod("exec*", txAttr_REQUIRED);
		source.addTransactionalMethod("set*", txAttr_REQUIRED);
		source.addTransactionalMethod("insert*", txAttr_REQUIRED);
		source.addTransactionalMethod("add*", txAttr_REQUIRED);
		source.addTransactionalMethod("append*", txAttr_REQUIRED);
		source.addTransactionalMethod("modify*", txAttr_REQUIRED);
		source.addTransactionalMethod("edit*", txAttr_REQUIRED);
		source.addTransactionalMethod("remove*", txAttr_REQUIRED);
		
		
		source.addTransactionalMethod("get*", txAttr_SUPPORTS);
		source.addTransactionalMethod("query*", txAttr_SUPPORTS);
		source.addTransactionalMethod("find*", txAttr_SUPPORTS);
		source.addTransactionalMethod("list*", txAttr_SUPPORTS);
		source.addTransactionalMethod("count*", txAttr_SUPPORTS);
		source.addTransactionalMethod("is*", txAttr_SUPPORTS);
		source.addTransactionalMethod("select*", txAttr_SUPPORTS);
		source.addTransactionalMethod("search*", txAttr_SUPPORTS);
		source.addTransactionalMethod("load*", txAttr_SUPPORTS);
		
		source.addTransactionalMethod("*", txAttr_SUPPORTS);
		
		return source;
	}
	
	/**
	 * 获取切面切点表达式
	 * @return
	 */
	protected String getPointcutExpression() {
		return "execution (* com.***.service.*.*(..))";
	}
}
