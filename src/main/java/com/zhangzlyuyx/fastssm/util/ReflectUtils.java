package com.zhangzlyuyx.fastssm.util;

import java.lang.reflect.ParameterizedType;

/**
 * 反射工具类
 *
 */
public class ReflectUtils {

	/**
	 * 获取泛型参数类型
	 * @param classz
	 * @return
	 */
	public static <T> Class<T> getGenericityType(Class<?> classz){
		return getGenericityType(classz, 0);
	}
	
	/**
	 * 获取泛型参数类型
	 * @param classz
	 * @param index
	 * @return
	 */
	public static <T> Class<T> getGenericityType(Class<?> classz, int index){
		Class<T> cls = (Class<T>) ((ParameterizedType)classz.getGenericSuperclass()).getActualTypeArguments()[index];
		return cls;
	}
}
