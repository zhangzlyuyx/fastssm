package com.zhangzlyuyx.fastssm.callback;

import java.util.Comparator;
import java.util.List;

import com.zhangzlyuyx.fastssm.util.ReflectUtils;

/**
 * 树递归回调
 *
 * @param <IN> 输入类型
 * @param <OUT>输出类型
 */
public abstract class TreeRecursionCallback<IN, OUT> {

	/**
	 * 判断是否为根节点
	 * @param id
	 * @return
	 */
	public boolean isRootId(Object id) {
		if (id == null) {
			return true;
		}
		if (id instanceof Long && (Long) id == 0L) {
			return true;
		}
		if (id.toString().equalsIgnoreCase("0")) {
			return true;
		}
		return false;
	} 
	
	/**
	 * 对象转换
	 * 
	 * @param input 输入对象
	 * @return 返回输出对象
	 */
	public abstract OUT convert(IN input);
	
	/**
	 * 获取输入父id值
	 * 
	 * @param input 输入对象
	 * @return 返回id
	 */
	public abstract Object getParentId(IN input);
	
	/**
	 * 获取输出 id 值
	 * 
	 * @param output
	 * @return
	 */
	public Object getOutputId(OUT output) {
		if(output == null) {
			return null;
		}
		Object id = ReflectUtils.getFieldValue(output, "id");
		if(id == null) {
			return null;
		}
		return id;
	}
	
	/**
	 * 判断值是否相等
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public boolean equals(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null) {
			return false;
		}
		if (obj1 instanceof Integer && obj2 instanceof Integer && ((Integer) obj1).equals((Integer) obj2)) {
			return true;
		}
		if (obj1 instanceof Long && obj2 instanceof Long && ((Long) obj1).equals((Long) obj2)) {
			return true;
		}
		if (obj1.toString().equalsIgnoreCase(obj2.toString())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 树处理
	 * 
	 * @param parentNode
	 * @param childs
	 */
	public abstract void tree(OUT parentNode, List<OUT> childs);
	
	/**
	 * 获取排序方式
	 * @return
	 */
	public Comparator<OUT> getComparator() {
		return null;
	}
}
