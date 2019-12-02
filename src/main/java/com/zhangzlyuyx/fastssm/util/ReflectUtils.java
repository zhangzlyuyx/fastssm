package com.zhangzlyuyx.fastssm.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import cn.hutool.core.util.ReflectUtil;

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
	
	/**
	 * 获取字段
	 * @param classz 类型
	 * @param fieldName 字段名
	 * @return
	 */
	public static Field getField(Class<?> classz, String fieldName) {
		return ReflectUtil.getField(classz, fieldName);
	}
	
	/**
	 * 获取字段值
	 * @param obj 对象
	 * @param fieldName 字段名
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		return ReflectUtil.getFieldValue(obj, fieldName);
	}
}
