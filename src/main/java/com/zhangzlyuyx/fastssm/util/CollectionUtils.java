package com.zhangzlyuyx.fastssm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.zhangzlyuyx.fastssm.callback.TreeRecursionCallback;

/**
 * CollectionUtils
 *
 */
public class CollectionUtils<IN, OUT> {

	/**
	 * 树递归处理
	 * @param inputList 输入对象集合
	 * @param outputParent 输出父节点对象(可空)
	 * @param treeRecursionCallback 树递归回调
	 * @return
	 */
	public static <IN, OUT> List<OUT> treeRecursion(List<IN> inputList, OUT outputParent, TreeRecursionCallback<IN, OUT> treeRecursionCallback){
		List<IN> findList = new ArrayList<>();
		for(IN item : inputList){
			Object parentId = treeRecursionCallback.getParentId(item);
			//是否为首次递归
			if(outputParent == null){
				//判断是否为根节点
				if(treeRecursionCallback.isRootId(parentId)){
					findList.add(item);
				}
			}else{
				//判断是否为子节点
				if(treeRecursionCallback.equals(parentId, treeRecursionCallback.getOutputId(outputParent))){
					findList.add(item);
				}
			}
		}
		//转换成树节点
		List<OUT> childs = new ArrayList<>();
		for(IN item : findList){
			OUT tree = treeRecursionCallback.convert(item);
			if(tree == null){
				continue;
			}
			childs.add(tree);
			//递归查找子节点
			treeRecursion(inputList, tree, treeRecursionCallback);
		}
		//输出集合排序
		Comparator<OUT> comparator = treeRecursionCallback.getComparator();
		if(comparator != null){
			Collections.sort(childs, comparator);
		}
		//树处理
		if(outputParent != null){
			treeRecursionCallback.tree(outputParent, childs);
		}
		return childs;
	}
	
}
