package com.zhangzlyuyx.fastssm.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件
 *
 */
public class PageCondition {

	private String connect = "and";
	
	private String columnName;
	
	private String operator = "=";
	
	private Object value;
	
	private List<PageCondition> conditions;
	
	public PageCondition() {
		
	}
	
	public PageCondition(String columnName, Object value) {
		this.columnName = columnName;
		this.value = value;
	}
	
	public PageCondition(String columnName, String operator, Object value) {
		this.columnName = columnName;
		this.operator = operator;
		this.value = value;
	}
	
	public PageCondition(String connect, String columnName, String operator, Object value){
		this.connect = connect;
		this.columnName = columnName;
		this.operator = operator;
		this.value = value;
	}
	
	public String getConnect() {
		return connect;
	}
	
	public void setConnect(String connect) {
		this.connect = connect;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public List<PageCondition> getConditions() {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		return conditions;
	}
	
	public void setConditions(List<PageCondition> conditions) {
		this.conditions = conditions;
	}
}
