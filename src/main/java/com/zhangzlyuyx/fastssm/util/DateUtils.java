package com.zhangzlyuyx.fastssm.util;

import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.date.DateUtil;

/**
 * 时间操作类
 *
 */
public class DateUtils {

	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date getDate(){
		return DateUtil.date();
	}
	
	/**
	 * 获取时间
	 * @param date
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒钟
	 * @param millisecond 毫秒
	 * @return
	 */
	public static Date getDate(Date date, int hour, int minute, int second, int millisecond){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return calendar.getTime();
	}
	
	/**
	 * 字符转日期
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr){
		return DateUtil.parse(dateStr);
	}
	
	/**
	 * 字符转日期
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date parse(String dateStr, String format){
		return DateUtil.parse(dateStr, format);
	}
	
	/**
	 * 日期转字符
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format){
		return DateUtil.format(date, format);
	}
	
	/**
	 * 日期转字符(yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return DateUtil.formatDate(date);
	}
	
	/**
	 * 日期转字符(HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date){
		return DateUtil.formatTime(date);
	}
	
	/**
	 * 日期转字符(yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date){
		return DateUtil.formatDateTime(date);
	}
}
