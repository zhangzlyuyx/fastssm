package com.zhangzlyuyx.fastssm.util;

import cn.hutool.crypto.SecureUtil;

/**
 * 加密/解密工具类
 *
 */
public class CryptoUtils {

	/**
	 * md5 加密
	 * @param data
	 * @return
	 */
	public static String encodeMd5(String data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * Base64 加密
	 * @param data
	 * @return
	 */
	public static String encodeBase64(byte[] data) {
    	return cn.hutool.core.codec.Base64.encode(data);
    }
	
	/**
	 * Base64解密
	 * @param s
	 * @return
	 */
	public static byte[] decodeBase64(String s){
    	return cn.hutool.core.codec.Base64.decode(s, "UTF-8");
    }
	
	/**
	 * Base64解密
	 * @param s
	 * @param charset
	 * @return
	 */
	public static byte[] decodeBase64(String s, String charset) {
    	if(StringUtils.isEmpty(charset)){
    		return cn.hutool.core.codec.Base64.decode(s);
    	}else{
    		return cn.hutool.core.codec.Base64.decode(s, charset);
    	}
    }
}
