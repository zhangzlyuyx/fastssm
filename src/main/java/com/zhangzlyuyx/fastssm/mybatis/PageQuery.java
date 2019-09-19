package com.zhangzlyuyx.fastssm.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询
 *
 */
public class PageQuery {

	/**
	 * 分页序号
	 */
	private Integer pageNo;
	
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	
	/**
	 * 排序方式
	 */
	private String orderBy;
	
	/**
	 * 查询的属性
	 */
	private String[] properties;
	
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	private List<PageCondition> conditions;
	
	public List<PageCondition> getConditions() {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		return conditions;
	}
	
	public void setConditions(List<PageCondition> conditions) {
		this.conditions = conditions;
	}
	
	public String[] getProperties() {
		if(this.properties == null) {
			this.properties = new String[] {};
		}
		return properties;
	}
	
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
}
