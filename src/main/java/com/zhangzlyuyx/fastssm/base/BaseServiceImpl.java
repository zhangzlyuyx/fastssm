package com.zhangzlyuyx.fastssm.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 获取当前sql会话
	 * @return
	 */
	public SqlSession geSqlSession() {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
		if(sqlSession == null){
			sqlSession = sqlSessionFactory.openSession();
		}
		return sqlSession;
	}
	
	/**
	 * 获取 mapper
	 * @return
	 */
	public abstract com.zhangzlyuyx.fastssm.base.BaseMapper<T> getMapper();
	
	/**
	 * 实体类型
	 */
	protected Class<T> entityClass;

	/**
	 * 获取实体类型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if(this.entityClass == null && this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType)this.getClass().getGenericSuperclass();
			Type argType = paramType.getActualTypeArguments().length > 0 ? paramType.getActualTypeArguments()[0] : null;
			if(argType != null && argType instanceof Class) {
				this.entityClass = (Class<T>)argType;
			}
		}
		return this.entityClass;
	}

	/**
	 * 创建 Example
	 * 
	 * @return
	 */
	@Override
	public Example createExample() {
		Example example = new Example(this.getEntityClass());
		return example;
	}

	/**
	 * 创建 Example
	 * 
	 * @param queryMap
	 * @return
	 */
	@Override
	public Example createExample(Map<String, Object> queryMap) {
		Example example = new Example(this.getEntityClass());
		if (queryMap != null && queryMap.size() > 0) {
			Criteria criteria = example.createCriteria();
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					criteria.andIsNull(kv.getKey());
				} else {
					criteria.andEqualTo(kv.getKey(), kv.getValue());
				}
			}
		}
		return example;
	}

	/**
	 * 根据实体条件查询实体列表
	 * @param record
	 * @return
	 */
	@Override
	public List<T> select(T record){
		return this.getMapper().select(record);
	}
	
	/**
	 * 查询实体列表
	 */
	@Override
	public List<T> select(Map<String, Object> queryMap) {
		return this.select(queryMap, null, null, null);
	}
	
	/**
	 * 查询实体列表
	 */
	@Override
	public List<T> select(Map<String, Object> queryMap, Integer page, Integer rows) {
		return this.select(queryMap, page, rows, null);
	}
	
	/**
	 * 查询实体列表
	 */
	@Override
	public List<T> select(Map<String, Object> queryMap, Integer page, Integer rows, String orderByClause, String... properties) {
		Example example = this.createExample(queryMap);
		if(orderByClause != null && orderByClause.length() > 0) {
			example.setOrderByClause(orderByClause);
		}
		if(properties != null && properties.length > 0) {
			example.selectProperties(properties);
		}
		return this.select(example, page, rows);
	}

	/**
	 * 分页查询实体列表
	 * 
	 * @param example
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<T> select(Example example, Integer page, Integer rows) {
		if (page != null && page != null) {
			int offset = (page - 1) * rows;
			RowBounds rowBounds = new RowBounds(offset, rows);
			return this.getMapper().selectByExampleAndRowBounds(example, rowBounds);
		} else {
			return this.getMapper().selectByExample(example);
		}
	}

	
	/**
	 * 根据主键查询
	 */
	@Override
	public T selectByPrimaryKey(Long id) {
		if (id == null) {
			return null;
		}
		return this.getMapper().selectByPrimaryKey(id);
	}

	/**
	 * 查询唯一数据
	 */
	@Override
	public T selectOne(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		List<T> list = this.select(example, 1, 2);
		if(list == null || list.size() == 0) {
			return null;
		}else {
			if(list.size() > 1) {
				throw new RuntimeException("select multiple rows!");
			}else {
				return list.get(0);
			}
		}
	}

	/**
	 * 查询第一条数据
	 */
	@Override
	public T selectFirst(Map<String, Object> queryMap, String orderByClause) {
		Example example = this.createExample(queryMap);
		if(orderByClause != null && orderByClause.length() > 0) {
			example.setOrderByClause(orderByClause);
		}
		List<T> list = this.select(example, 1, 1);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
	
	/**
	 * 查询全部结果
	 */
	@Override
	public List<T> selectAll(){
		return this.getMapper().selectAll();
	}
	
	/**
	 * 查询记录数
	 */
	public int selectCount(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().selectCountByExample(example);
	}
	
	/**
	 * 查询记录数
	 */
	@Override
	public int selectCount(Example example) {
		return this.getMapper().selectCountByExample(example);
	}
	
	/**
	 * 根据实体中的属性查询总数，查询条件使用等号
	 * @param record
	 * @return
	 */
	@Override
	public int selectCount(T record) {
		return this.getMapper().selectCount(record);
	}
	
	/**
	 * 根据主键字段查询总数，方法参数必须包含完整的主键属性，查询条件使用等号
	 * @param id
	 * @return
	 */
	@Override
	public boolean exists(Long id) {
		return this.getMapper().existsWithPrimaryKey(id);
	}
	
	/**
	 * 插入数据
	 */
	@Override
	public int insert(T record) {
		return this.getMapper().insert(record);
	}
	
	/**
	 * 选择性插入数数据
	 */
	@Override
	public int insertSelective(T record) {
		return this.getMapper().insertSelective(record);
	}

	/**
	 * 根据主键字段进行删除，方法参数必须包含完整的主键属性
	 */
	@Override
	public int deleteByPrimaryKey(Long id) {
		if (id == null) {
			return 0;
		}
		return this.getMapper().deleteByPrimaryKey(id);
	}
	
	/**
	 * 根据实体属性作为条件进行删除，查询条件使用等号
	 */
	@Override
	public int delete(T record) {
		return this.getMapper().delete(record);
	}
	
	/**
	 * 根据queryMap条件删除数据
	 */
	@Override
	public int delete(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().deleteByExample(example);
	}
	
	/**
	 * 根据Example条件删除数据
	 */
	@Override
	public int delete(Example example) {
		return this.getMapper().deleteByExample(example);
	}
	
	/**
	 * 根据主键更新实体全部字段，null值会被更新
	 */
	@Override
	public int update(T record) {
		return this.getMapper().updateByPrimaryKey(record);
	}
	
	/**
	 * 根据queryMap条件更新实体`record`包含的全部属性，null值会被更新
	 */
	@Override
	public int update(T record, Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().updateByExample(record, example);
	}
	
	/**
	 * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
	 */
	@Override
	public int update(T record, Example example) {
		return this.getMapper().updateByExample(record, example);
	}
	
	/**
	 * 根据主键更新属性不为null的值
	 */
	@Override
	public int updateSelective(T record) {
		return this.getMapper().updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 根据queryMap条件更新实体`record`包含的不是null的属性值
	 */
	@Override
	public int updateSelective(T record, Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().updateByExampleSelective(record, example);
	}
	
	/**
	 * 根据Example条件更新实体`record`包含的不是null的属性值
	 */
	@Override
	public int updateSelective(T record, Example example) {
		return this.getMapper().updateByExampleSelective(record, example);
	}
}
