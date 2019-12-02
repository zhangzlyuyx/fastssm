package com.zhangzlyuyx.fastssm.callback;

/**
 * 对象转换回调
 *
 * @param <IN> 输入对象
 * @param <OUT> 输出对象
 */
public interface ConvertCallback<IN, OUT> {
	/**
	 * 回调函数
	 * @param input 输入对象
	 * @return 返回输出对象
	 */
	OUT callback(IN input);
}
