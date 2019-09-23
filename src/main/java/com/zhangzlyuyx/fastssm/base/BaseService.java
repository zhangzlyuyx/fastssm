package com.zhangzlyuyx.fastssm.base;

import java.util.List;
import java.util.Map;

import com.zhangzlyuyx.fastssm.mybatis.PageQuery;
import com.zhangzlyuyx.fastssm.mybatis.PageResult;

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
	 * 创建 Example
	 * @param queryMap 查询条件
	 * @return
	 */
	Example createExample(Map<String, Object> queryMap);
	
	/**
	 * 根据实体查询列表
	 * @param record
	 * @return
	 */
	List<T> select(T record);
	
	/**
	 * 查询实体列表
	 * @param queryMap
	 * @return
	 */
	List<T> select(Map<String, Object> queryMap);
	
	/**
	 * 查询实体列表
	 * @param queryMap
	 * @return
	 */
	List<T> select(Map<String, Object> queryMap, Integer page, Integer rows);
	
	/**
	 * 查询实体 列表
	 * @param queryMap 查询条件
	 * @param page 页码
	 * @param rows 每页记录数
	 * @param orderByClause 排序
	 * @param properties 查询字段
	 * @return
	 */
	List<T> select(Map<String, Object> queryMap, Integer page, Integer rows, String orderByClause, String... properties);
	
	/**
	 * 分页数据查询
	 * @param pageQuery
	 * @return
	 */
	PageResult<T> select(PageQuery pageQuery);
	
	/**
	 * 分页查询数据列表
	 * @param example
	 * @param page 分页序号
	 * @param rows 每页记录数
	 * @return
	 */
	List<T> select(Example example, Integer page, Integer rows);
	
	/**
	 * 根据主键查询实体
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
	 * @param queryMap 查询条件
	 * @param orderByClause 排序
	 * @return
	 */
	T selectFirst(Map<String, Object> queryMap, String orderByClause);
	
	/**
	 * 查询全部结果
	 * @return
	 */
	List<T> selectAll();
	
	/**
	 * 查询记录数
	 * @param queryMap 查询条件
	 * @return
	 */
	int selectCount(Map<String, Object> queryMap);
	
	/**
	 * 查询记录数
	 * @param pageQuery
	 * @return
	 */
	int selectCount(PageQuery pageQuery);
	
	/**
	 * 查询记录数
	 * @param example
	 * @return
	 */
	int selectCount(Example example);
	
	/**
	 * 根据实体中的属性查询总数，查询条件使用等号
	 * @param record
	 * @return
	 */
	int selectCount(T record);
	
	/**
	 * 根据主键字段查询总数，方法参数必须包含完整的主键属性，查询条件使用等号
	 * @param id
	 * @return
	 */
	boolean exists(Long id);
	
	/**
	 * 插入数据实体
	 * @param record
	 * @return
	 */
	int insert(T record);
	
	/**
	 * 选择性插入实体
	 * @param record
	 * @return
	 */
	int insertSelective(T record);
	
	/**
	 * 根据主键字段进行删除，方法参数必须包含完整的主键属性
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);
	
	/**
	 * 根据实体属性作为条件进行删除，查询条件使用等号
	 * @param record
	 * @return
	 */
	int delete(T record);
	
	/**
	 * 根据queryMap条件删除数据
	 * @param queryMap
	 * @return
	 */
	int delete(Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件删除数据
	 * @param example
	 * @return
	 */
	int delete(Example example);
	
	/**
	 * 根据主键更新实体全部字段，null值会被更新
	 * @param record
	 * @return
	 */
	int update(T record);
	
	/**
	 * 根据queryMap条件更新实体`record`包含的全部属性，null值会被更新
	 * @param record
	 * @param queryMap
	 * @return
	 */
	int update(T record, Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
	 * @param record
	 * @param example
	 * @return
	 */
	int update(T record, Example example);
	
	/**
	 * 根据主键更新属性不为null的值
	 * @param record
	 * @return
	 */
	int updateSelective(T record);
	
	/**
	 * 根据queryMap条件更新实体`record`包含的不是null的属性值
	 * @param record
	 * @param queryMap
	 * @return
	 */
	int updateSelective(T record, Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件更新实体`record`包含的不是null的属性值
	 * @param record
	 * @param example
	 * @return
	 */
	int updateSelective(T record, Example example);
}
