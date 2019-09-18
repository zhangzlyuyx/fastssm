package com.zhangzlyuyx.fastssm.util;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串操作类
 *
 */
public class StringUtils {

	/**
	 * 判断是否为空字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(CharSequence str){
		return StrUtil.isEmpty(str);
	}
	
	/**
	 * 去除首尾空格
	 * @param str
	 * @return
	 */
	public static String trim(CharSequence str){
		return StrUtil.trim(str);
	}
	
	/**
	 * 去除前缀
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static String trimStart(CharSequence str, CharSequence prefix){
		return StrUtil.removePrefix(str, prefix);
	}
	
	/**
	 * 去除后缀
	 * @param str
	 * @param suffix 
	 * @return
	 */
	public static String trimEnd(CharSequence str, CharSequence suffix){
		return StrUtil.removeSuffix(str, suffix);
	}
	
	/**
	 * 格式化字符串
	 * @param format
	 * @param args
	 * @return
	 */
	public static String format(String format, Object... args){
		if(format == null){
			return format;
		}
		if(format.contains("{0}")){
			return StrUtil.indexedFormat(format, args);
		}else{
			return String.format(format, args);
		}
	}

	/**
	 * 格式化字符串
	 * @param format
	 * @param args
	 * @return
	 */
	public static String formatSql(String format, Object... args){
		if(format.contains("{0}")){
			for (int i = 0; i < args.length; i++) {
				if(args[i] != null && args[i].getClass().equals(Integer.class)){
					args[i] = args[i].toString();
				}
			}
			return StrUtil.indexedFormat(format.replace("'", "''"), args);
		}
		return format;
	}
}
