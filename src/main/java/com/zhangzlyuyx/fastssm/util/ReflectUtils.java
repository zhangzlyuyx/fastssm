package com.zhangzlyuyx.fastssm.util;

import java.lang.reflect.ParameterizedType;

/**
 * 反射工具类
 *
 */
public class ReflectUtils {

	/**
	 * 获取泛型参数类型
	 * @param <T> 泛型类型
	 * @param classz 输入类型
	 * @return 返回泛型类型
	 */
	public static <T> Class<T> getGenericityType(Class<?> classz){
		return getGenericityType(classz, 0);
	}
	
	/**
	 * 获取泛型参数类型
	 * @param <T> 泛型类型
	 * @param classz 输入 类型
	 * @param index 索引
	 * @return 返回泛型类型
	 */
	public static <T> Class<T> getGenericityType(Class<?> classz, int index){
		Class<T> cls = (Class<T>) ((ParameterizedType)classz.getGenericSuperclass()).getActualTypeArguments()[index];
		return cls;
	}
}
