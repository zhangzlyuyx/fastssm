package com.zhangzlyuyx.fastssm.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhangzlyuyx.fastssm.mybatis.PageQuery;
import com.zhangzlyuyx.fastssm.mybatis.PageResult;
import com.zhangzlyuyx.fastssm.mybatis.PageCondition;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 获取当前sql会话
	 * @return
	 */
	@Override
	public SqlSession getSqlSession() {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
		if(sqlSession == null){
			sqlSession = sqlSessionFactory.openSession();
		}
		return sqlSession;
	}
	
	/**
	 * 获取查询 MappedStatement Id
	 * @param sqlSession sql会话
	 * @param sql sql语句
	 * @param resultType 结果类型
	 * @return 返回 MappedStatement Id
	 */
	public String getSelectMappedStatementId(SqlSession sqlSession, String sql, final Class<?> resultType) {
		final Configuration configuration = sqlSession.getConfiguration();
		final String id = new StringBuilder(SqlCommandType.SELECT.toString()).append(".").append(sql.hashCode()).toString();
		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		if(configuration.hasStatement(id, false)) {
			return id;
		}else {
			MappedStatement ms = new MappedStatement.Builder(configuration, id, sqlSource, SqlCommandType.SELECT).resultMaps(new ArrayList<ResultMap>() {
            	{
                	add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
            	}
           	}).build();
            //缓存
            configuration.addMappedStatement(ms);
		}
		return id;
	}
	
	/**
	 * 查询单值
	 * @param sql
	 * @param resultType
	 * @return
	 */
	@Override
	public Object selectOne(String sql, Class<?> resultType) {
		SqlSession sqlSession = this.getSqlSession();
		String sqlId = this.getSelectMappedStatementId(sqlSession, sql, Date.class);
		return sqlSession.selectOne(sqlId);
	}
	
	/**
	 * 获取服务器时间
	 */
	@Override
	public Date getDate() {
		Object value =  this.selectOne("select sysdate()", Date.class);
		return (Date)value;
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
	 * 创建 Example
	 * @param pageQuery
	 * @return
	 */
	public Example createExample(PageQuery pageQuery) {
		Example example = new Example(this.getEntityClass());
		if(pageQuery != null) {
			//查询条件
			if(pageQuery.getConditions() != null && pageQuery.getConditions().size() > 0) {
				Criteria criteriaNew = example.createCriteria();
				for(int i = 0; i < pageQuery.getConditions().size(); i++) {
					PageCondition condition = pageQuery.getConditions().get(i);
					this.criteriaOperator(example, criteriaNew, condition);
				}
			}
			/**
			 * 排序
			 */
			if(pageQuery.getOrderBy() != null && pageQuery.getOrderBy().length() > 0) {
				example.setOrderByClause(pageQuery.getOrderBy());
			}
			//查询的属性
			if(pageQuery.getProperties() != null && pageQuery.getProperties().length > 0) {
				example.selectProperties(pageQuery.getProperties());
			}
		}
		return example;
	}
	
	/**
	 * criteria 操作处理
	 * @param example
	 * @param criteriaNew
	 * @param condition
	 */
	protected void criteriaOperator(Example example, Criteria criteriaNew, PageCondition condition) {
		if(condition == null) {
			return;
		}
		//当前条件处理
		if(condition.getColumnName() != null && condition.getColumnName().length() > 0) {
			//获取操作类型
			String operator = (condition.getOperator() != null && condition.getOperator().length() > 0) ? condition.getOperator() : "=";
			if(operator.equalsIgnoreCase("=")){
				criteriaNew.andEqualTo(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase("!=") || operator.equalsIgnoreCase("<>")){
				criteriaNew.andNotEqualTo(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase("like")){
				criteriaNew.andLike(condition.getColumnName(), condition.getValue().toString());
			}else if(operator.equalsIgnoreCase("not like")){
				criteriaNew.andNotLike(condition.getColumnName(), condition.getValue().toString());
			}else if(operator.equalsIgnoreCase(">")){
				criteriaNew.andGreaterThan(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase(">=")){
				criteriaNew.andGreaterThanOrEqualTo(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase("<")){
				criteriaNew.andLessThan(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase("<=")){
				criteriaNew.andLessThanOrEqualTo(condition.getColumnName(), condition.getValue());
			}else if(operator.equalsIgnoreCase("in")){
				criteriaNew.andIn(condition.getColumnName(), (List<?>)condition.getValue());
			}else if(operator.equalsIgnoreCase("not in")){
				criteriaNew.andNotIn(condition.getColumnName(), (List<?>)condition.getValue());
			}else if(operator.equalsIgnoreCase("isnull") || operator.equalsIgnoreCase("is null")){
				criteriaNew.andIsNull(condition.getColumnName());
			}else if(operator.equalsIgnoreCase("is not null")){
				criteriaNew.andIsNotNull(condition.getColumnName());
			}else {
				// 其他不支持操作处理
				if(condition.getValue() != null){
					criteriaNew.andCondition(condition.getColumnName(), condition.getValue());
				}else{
					criteriaNew.andCondition(condition.getColumnName());
				}
			}
		}
		//递归嵌套条件处理
		if(condition.getConditions() != null && condition.getConditions().size() > 0) {
			Criteria nextCriteria = example.createCriteria();
			for(PageCondition nextCondition : condition.getConditions()) {
				criteriaOperator(example, nextCriteria, nextCondition);
			}
		}
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
	 * 
	 */
	@Override
	public PageResult<T> select(PageQuery pageQuery){
		PageResult<T> pageResult = new PageResult<>();
		Example example = this.createExample(pageQuery);
		if(pageQuery.getProperties() != null && pageQuery.getProperties().length > 0) {
			example.selectProperties(pageQuery.getProperties());
		}
		List<T> rows;
		if (pageQuery.getPageNo() != null && pageQuery.getPageSize() != null) {
			int offset = (pageQuery.getPageNo() - 1) * pageQuery.getPageSize();
			RowBounds rowBounds = new RowBounds(offset, pageQuery.getPageSize());
			rows = this.getMapper().selectByExampleAndRowBounds(example, rowBounds);
			pageResult.setRows(rows);
			int total = this.getMapper().selectCountByExample(example);
			pageResult.setTotal(Long.parseLong(String.valueOf(total)));
		} else {
			rows = this.getMapper().selectByExample(example);
			pageResult.setRows(rows);
			int total = rows.size();
			pageResult.setTotal(Long.parseLong(String.valueOf(total)));
		}
		return pageResult;
	}

	/**
	 * 分页查询实体列表
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
	@Override
	public int selectCount(Map<String, Object> queryMap) {
		Example example = this.createExample(queryMap);
		return this.getMapper().selectCountByExample(example);
	}
	
	/**
	 * 查询记录数
	 */
	@Override
	public int selectCount(PageQuery pageQuery){
		Example example = this.createExample(pageQuery);
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
