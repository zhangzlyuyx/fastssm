package com.zhangzlyuyx.fastssm.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果
 *
 * @param <T>
 */
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -8887264195020396116L;
	
	private Long total;
	
	private List<T> rows;
	
	public PageResult() {
		this.total = 0L;
		this.rows = new ArrayList<>();
	}
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}
	
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
