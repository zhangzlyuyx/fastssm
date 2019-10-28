package com.zhangzlyuyx.fastssm.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * 加密/解密工具类
 *
 */
public class CryptoUtils {

	/**
	 * md5 加密
	 * @param data 要加密的字符串
	 * @return 返回加密后的字符
	 */
	public static String encodeMd5(String data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * Base64 加密
	 * @param data 要解密的字符串
	 * @return 返回加密后的字符
	 */
	public static String encodeBase64(byte[] data) {
    	return cn.hutool.core.codec.Base64.encode(data);
    }
	
	/**
	 * Base64解密
	 * @param s 要解密的字符
	 * @return 返回解密后的字符
	 */
	public static byte[] decodeBase64(String s){
    	return cn.hutool.core.codec.Base64.decode(s, "UTF-8");
    }
	
	/**
	 * Base64解密
	 * @param s 要解密的字符
	 * @param charset 字符集
	 * @return 返回解密后的字节数组
	 */
	public static byte[] decodeBase64(String s, String charset) {
    	if(StringUtils.isEmpty(charset)){
    		return cn.hutool.core.codec.Base64.decode(s);
    	}else{
    		return cn.hutool.core.codec.Base64.decode(s, charset);
    	}
    }
	
	/**
	 * 生成aes(AES/ECB/PKCS5Padding)密钥
	 * @return 返回16位长度密钥
	 */
	public static byte[] generateAESKey() {
		return SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
	}
	
	/**
	 * aes(AES/ECB/PKCS5Padding)加密
	 * @param data 明文
	 * @param key 16长度密钥
	 * @return 返回密文
	 */
	public static byte[] encodeAES(byte[] data, byte[] key) {
		AES aes = SecureUtil.aes(key);
		return aes.encrypt(data);
	}
	
	/**
	 * aes(AES/ECB/PKCS5Padding)加密成base64字符
	 * @param data 明文
	 * @param key 16长度密钥
	 * @return 返回密文base64字符
	 */
	public static String encodeAESBase64(byte[] data, String key) {
		byte[] buffer = encodeAES(data, StringUtils.bytes(key));
		return encodeBase64(buffer);
	}
	
	/**
	 * aes(AES/ECB/PKCS5Padding)解密
	 * @param data 密文
	 * @param key 16长度密钥
	 * @return 返回明文
	 */
	public static byte[] decodeAES(byte[] data, byte[] key) {
		AES aes = SecureUtil.aes(key);
		return aes.decrypt(data);
	}
	
	/**
	 * aes(AES/ECB/PKCS5Padding)解密base64字符
	 * @param dataBase64 密文bas64字符
	 * @param key 16长度密钥
	 * @return 返回明文
	 */
	public static byte[] decodeAESBase64(String dataBase64, String key) {
		byte[] buffer = decodeAES(decodeBase64(dataBase64), StringUtils.bytes(key));
		return buffer;
	}
	
	/**
	 * rsa 非对称加密私钥签名
	 * @param data 要签名的数据 
	 * @param privateKeyBase64 私钥bas64字符
	 * @return 返回签名base64字符
	 */
	public static String signMD5withRSA(String data, String privateKeyBase64) {
		byte[] buffer = signMD5withRSA(StringUtils.bytes(data), decodeBase64(privateKeyBase64));
		return encodeBase64(buffer);
	}
	
	/**
	 * rsa 非对称加密私钥签名
	 * @param data 要签名的数据
	 * @param privateKey 私钥
	 * @return 返回签名结果
	 */
	public static byte[] signMD5withRSA(byte[] data, byte[] privateKey) {
		Sign signUtils = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, null);
		return signUtils.sign(data);
	}
	
	/**
	 * rsa 非对称加密公钥签名验证 
	 * @param data 待验证的数据
	 * @param publicKeyBase64 公钥base64字符
	 * @param signBase64 签名base64结果
	 * @return 返回验证结果 
	 */
	public static boolean verifySignMD5withRSA(String data, String publicKeyBase64, String signBase64) {
		return verifySignMD5withRSA(StringUtils.bytes(data), decodeBase64(publicKeyBase64), decodeBase64(signBase64));
	}
	
	/**
	 * rsa 非对称加密公钥签名验证 
	 * @param data 待验证的数据
	 * @param publicKey 公钥
	 * @param sign 签名结果
	 * @return 返回验证结果
	 */
	public static boolean verifySignMD5withRSA(byte[] data, byte[] publicKey, byte[] sign) {
		Sign signUtils = SecureUtil.sign(SignAlgorithm.MD5withRSA, null, publicKey);
		return signUtils.verify(data, sign);
	}
}
