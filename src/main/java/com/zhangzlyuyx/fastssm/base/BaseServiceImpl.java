package com.zhangzlyuyx.fastssm.base;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.RowBounds;

import com.zhangzlyuyx.fastssm.util.ReflectUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	public abstract com.zhangzlyuyx.fastssm.base.BaseMapper<T> getMapper();

	/**
	 * 获取实体类型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		return ReflectUtils.getGenericityType(this.getClass());
	}

	/**
	 * 创建 Exmple
	 * 
	 * @return
	 */
	public Example createExample() {
		Example example = new Example(this.getEntityClass());
		return example;
	}

	/**
	 * 创建 Exmple
	 * 
	 * @param queryMap
	 * @return
	 */
	protected Example createExample(Map<String, Object> queryMap) {
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
		// Example example = this.createExample(queryMap);
		// return this.getMapper().selectOneByExample(example)
		return null;
	}

	/**
	 * 查询第一条数据
	 */
	@Override
	public T selectFirst(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		List<T> list = this.select(example, 1, 1);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
	
	/**
	 * 查询列表
	 * 
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<T> select(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().selectByExample(example);
	}

	/**
	 * 分页查询列表
	 * 
	 * @param example
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<T> select(Example example, Integer pageNo, Integer pageSize) {
		if (pageNo != null && pageSize != null) {
			int offset = (pageNo - 1) * pageSize;
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			return this.getMapper().selectByExampleAndRowBounds(example, rowBounds);
		} else {
			return this.getMapper().selectByExample(example);
		}
	}

	/**
	 * 查询记录数
	 */
	@Override
	public int selectCount(Example example) {
		return this.getMapper().selectCountByExample(example);
	}
	
	/**
	 * 选择性插数数据
	 */
	@Override
	public int insertSelective(T record) {
		return this.getMapper().insertSelective(record);
	}

	/**
	 * 根据主键删除数据
	 */
	@Override
	public int deleteByPrimaryKey(Long id) {
		if (id == null) {
			return 0;
		}
		return this.getMapper().deleteByPrimaryKey(id);
	}
}
