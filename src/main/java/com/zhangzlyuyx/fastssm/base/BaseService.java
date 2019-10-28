package com.zhangzlyuyx.fastssm.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.zhangzlyuyx.fastssm.mybatis.PageQuery;
import com.zhangzlyuyx.fastssm.mybatis.PageResult;

import tk.mybatis.mapper.entity.Example;

public interface BaseService<T> {

	/**
	 * 获取 sqlSession
	 * @return 返回sql会话
	 */
	SqlSession getSqlSession();
	
	/**
	 * 查询单值
	 * @param sql sql语句
	 * @param resultType 结果类类型
	 * @return 结果
	 */
	Object selectOne(String sql, Class<?> resultType);
	
	/**
	 * 获取服务器时间
	 * @return 返回服务器时间
	 */
	Date getDate();
	
	/**
	 * 获取实体类型
	 * @return 返回泛型
	 */
	Class<T> getEntityClass();
	
	/**
	 * 创建 Example
	 * @return 返回 Example
	 */
	Example createExample();
	
	/**
	 * 创建 Example
	 * @param queryMap 查询条件
	 * @return 返回Example
	 */
	Example createExample(Map<String, Object> queryMap);
	
	/**
	 * 根据实体查询列表
	 * @param record 实体条件
	 * @return 返回集合
	 */
	List<T> select(T record);
	
	/**
	 * 查询实体列表
	 * @param queryMap 查询条件
	 * @return 返回集合
	 */
	List<T> select(Map<String, Object> queryMap);
	
	/**
	 * 查询实体列表
	 * @param queryMap 查询条件 
	 * @param page 页
	 * @param rows 行
	 * @return 返回实体集合
	 */
	List<T> select(Map<String, Object> queryMap, Integer page, Integer rows);
	
	/**
	 * 查询实体 列表
	 * @param queryMap 查询条件
	 * @param page 页码
	 * @param rows 每页记录数
	 * @param orderByClause 排序
	 * @param properties 查询字段
	 * @return 返回集合
	 */
	List<T> select(Map<String, Object> queryMap, Integer page, Integer rows, String orderByClause, String... properties);
	
	/**
	 * 分页数据查询
	 * @param pageQuery 分页条件
	 * @return 返回分页结果
	 */
	PageResult<T> select(PageQuery pageQuery);
	
	/**
	 * 分页查询数据列表
	 * @param example 条件
	 * @param page 分页序号
	 * @param rows 每页记录数
	 * @return 返回集合
	 */
	List<T> select(Example example, Integer page, Integer rows);
	
	/**
	 * 根据主键查询实体
	 * @param id id
	 * @return 返回实体
	 */
	T selectByPrimaryKey(Long id);
	
	/**
	 * 查询唯一数据
	 * @param queryMap 查询条件
	 * @return 返回实体
	 */
	T selectOne(Map<String, Object> queryMap);
	
	/**
	 * 查询第一条数据
	 * @param queryMap 查询条件
	 * @param orderByClause 排序
	 * @return 返回实体
	 */
	T selectFirst(Map<String, Object> queryMap, String orderByClause);
	
	/**
	 * 查询全部结果
	 * @return 返回集合
	 */
	List<T> selectAll();
	
	/**
	 * 查询记录数
	 * @param queryMap 查询条件
	 * @return 返回记录数
	 */
	int selectCount(Map<String, Object> queryMap);
	
	/**
	 * 查询记录数
	 * @param pageQuery 查询条件
	 * @return 返回记录数
	 */
	int selectCount(PageQuery pageQuery);
	
	/**
	 * 查询记录数
	 * @param example 条件
	 * @return 返回记录数
	 */
	int selectCount(Example example);
	
	/**
	 * 根据实体中的属性查询总数，查询条件使用等号
	 * @param record 实体条件
	 * @return 返回记录数
	 */
	int selectCount(T record);
	
	/**
	 * 根据主键字段查询总数，方法参数必须包含完整的主键属性，查询条件使用等号
	 * @param id id
	 * @return 返回布尔值
	 */
	boolean exists(Long id);
	
	/**
	 * 插入数据实体
	 * @param record 实体条件
	 * @return 返回受影响记录行数 
	 */
	int insert(T record);
	
	/**
	 * 选择性插入实体
	 * @param record 实体
	 * @return 返回受影响记录行数 
	 */
	int insertSelective(T record);
	
	/**
	 * 根据主键字段进行删除，方法参数必须包含完整的主键属性
	 * @param id id
	 * @return 返回受影响记录行数 
	 */
	int deleteByPrimaryKey(Long id);
	
	/**
	 * 根据实体属性作为条件进行删除，查询条件使用等号
	 * @param record 实体
	 * @return 返回受影响记录行数 
	 */
	int delete(T record);
	
	/**
	 * 根据queryMap条件删除数据
	 * @param queryMap 查询条件
	 * @return 返回受影响记录行数 
	 */
	int delete(Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件删除数据
	 * @param example 条件
	 * @return 返回受影响记录行数 
	 */
	int delete(Example example);
	
	/**
	 * 根据主键更新实体全部字段，null值会被更新
	 * @param record 实体条件
	 * @return 返回受影响记录行数 
	 */
	int update(T record);
	
	/**
	 * 根据queryMap条件更新实体`record`包含的全部属性，null值会被更新
	 * @param record 实体
	 * @param queryMap 查询条件
	 * @return 返回受影响记录行数 
	 */
	int update(T record, Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
	 * @param record 实体
	 * @param example 条件
	 * @return 返回受影响记录行数 
	 */
	int update(T record, Example example);
	
	/**
	 * 根据主键更新属性不为null的值
	 * @param record 实体
	 * @return 返回受影响记录行数 
	 */
	int updateSelective(T record);
	
	/**
	 * 根据queryMap条件更新实体`record`包含的不是null的属性值
	 * @param record 实体 
	 * @param queryMap 查询条件
	 * @return 返回受影响记录行数 
	 */
	int updateSelective(T record, Map<String, Object> queryMap);
	
	/**
	 * 根据Example条件更新实体`record`包含的不是null的属性值
	 * @param record 实体
	 * @param example 条件
	 * @return 返回受影响记录行数 
	 */
	int updateSelective(T record, Example example);
}
