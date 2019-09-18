package com.zhangzlyuyx.fastssm.common;

import java.nio.charset.Charset;

public class Constant {

	/**
	 * 字符集 utf-8
	 */
	public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
	
	public static final String RESPONSE_KEY_CODE = "code";
	
	public static final String RESPONSE_KEY_MSG = "msg";
	
	public static final String RESPONSE_KEY_DATA = "data";
	
	public static final String RESPONSE_CODE_SUCCESS = "success";
	
	public static final String RESPONSE_CODE_ERROR = "error";
	
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public final static String DATE_FORMAT_CHINA = "yyyy年MM月dd日 HH:mm:ss";
	
	public final static String DATE_FORMAT_DATE = "yyyy-MM-dd";
	
	public final static String DATE_FORMAT_TIME = "HH:mm:ss";
}
