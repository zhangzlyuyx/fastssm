package com.zhangzlyuyx.fastssm.base;

import java.util.List;
import java.util.Map;

import tk.mybatis.mapper.entity.Example;

public interface BaseService<T> {

	/**
	 * 获取实体类型
	 * @return
	 */
	Class<T> getEntityClass();
	
	/**
	 * 创建 Example
	 * @return
	 */
	Example createExample();
	
	/**
	 * 根据主键查询数据
	 * @param id
	 * @return
	 */
	T selectByPrimaryKey(Long id);
	
	/**
	 * 查询唯一数据
	 * @param queryMap
	 * @return
	 */
	T selectOne(Map<String, Object> queryMap);
	
	/**
	 * 查询第一条数据
	 * @param queryMap
	 * @return
	 */
	T selectFirst(Map<String, Object> queryMap);
	
	/**
	 * 查询数据列表
	 * @param queryMap
	 * @return
	 */
	List<T> select(Map<String, Object> queryMap);
		
	/**
	 * 分页查询数据列表
	 * @param example
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<T> select(Example example, Integer pageNo, Integer pageSize);
	
	/**
	 * 查询记录数
	 * @param example
	 * @return
	 */
	int selectCount(Example example);
	
	/**
	 * 选择性插入数据
	 * @param record
	 * @return
	 */
	int insertSelective(T record);
	
	/**
	 * 根据主键删除数据
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);
}
