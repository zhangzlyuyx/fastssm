package com.zhangzlyuyx.fastssm.util;

import java.nio.charset.Charset;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串操作类
 *
 */
public class StringUtils {

	/**
	 * 判断是否为空字符串
	 * @param str 字符串
	 * @return 返回字符串
	 */
	public static boolean isEmpty(CharSequence str){
		return StrUtil.isEmpty(str);
	}
	
	/**
	 * 去除首尾空格
	 * @param str 字符串
	 * @return 返回字符串
	 */
	public static String trim(CharSequence str){
		return StrUtil.trim(str);
	}
	
	/**
	 * 去除前缀
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 返回字符串
	 */
	public static String trimStart(CharSequence str, CharSequence prefix){
		return StrUtil.removePrefix(str, prefix);
	}
	
	/**
	 * 去除后缀
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 返回字符串
	 */
	public static String trimEnd(CharSequence str, CharSequence suffix){
		return StrUtil.removeSuffix(str, suffix);
	}
	
	/**
	 * 格式化字符串
	 * @param format 格式
	 * @param args 参数列表
	 * @return 返回字符串
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
	 * @param format 格式
	 * @param args 参数
	 * @return 返回字符串
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
	
	/**
	 * 字符串转字节数组
	 * @param str 字符串
	 * @return 返回字节数组
	 */
	public static byte[] bytes(CharSequence str) {
		return bytes(str, CharsetUtils.CHARSET_UTF_8);
	}
	
	/**
	 * 字符串转字节数组
	 * @param str 字符串
	 * @param charset 字符集
	 * @return 返回字节数组
	 */
	public static byte[] bytes(CharSequence str, Charset charset) {
		if(charset == null) {
			charset = CharsetUtils.CHARSET_UTF_8;
		}
		return StrUtil.bytes(str, charset);
	}
}
