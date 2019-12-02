package com.zhangzlyuyx.fastssm.callback;

/**
 * ActionCallback
 *
 * @param <T> 类型
 */
public interface ActionCallback<T> {
	/**
	 * 回调函数
	 * @param t 对象
	 */
	void callback(T t);
}
