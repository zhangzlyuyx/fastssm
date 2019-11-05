package com.zhangzlyuyx.fastssm.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志操作类
 * @author zhangzlyuyx
 *
 */
public class LogUtils {
	
	/**
	 * logger map
	 */
	private final static Map<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
	
	/**
	 * 获取日志访问器
	 * @param clazz 类型
	 * @return 返回 Logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		String name = clazz.getName();
		return getLogger(name);
	}
	
	/**
	 * 获取日志访问器
	 * @param name 名称
	 * @return 返回 Logger
	 */
	public static Logger getLogger(String name) {
		if(loggerMap.containsKey(name)) {
			return loggerMap.get(name);
		}else {
			Logger logger = LoggerFactory.getLogger(name);
			loggerMap.put(name, logger);
			return logger;
		}
	}
	
	/**
	 * 信息日志
	 * @param clazz 类型
	 * @param format 格式化文本
	 * @param arguments 参数
	 */
	public static void info(Class<?> clazz, String format, Object... arguments) {
		getLogger(clazz).info(format, arguments);
	}
	
	/**
	 * 错误日志
	 * @param clazz 类型
	 * @param msg 消息内容
	 * @param t 异常信息
	 */
	public static void error(Class<?> clazz, String msg, Throwable t) {
		getLogger(clazz).error(msg, t);
	}
}
