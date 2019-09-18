package com.zhangzlyuyx.fastssm.common;

import java.io.Serializable;

/**
 * 通用返回值
 *
 */
public class Return<T> implements Serializable {

	private static final long serialVersionUID = 2386783247904032707L;
	
	/**
	 * 返回标志
	 */
	private String code;
	
	public String getCode() {
		return this.code;
	}
	
	public Return<T> setCode(String code) {
		this.code = code;
		return this;
	}
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess(){
		return this.code != null && this.code.equalsIgnoreCase("success");
	}
	
	/**
	 * 是否失败
	 * @return
	 */
	public boolean isFail(){
		return this.code != null && this.code.equalsIgnoreCase("error");
	}
	
	/**
	 * 返回消息
	 */
	private String msg;
	
	public String getMsg() {
		return this.msg;
	}
	
	public Return<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	
	/**
	 * 返回关联的值
	 */
	private T data;
	
	public T getData() {
		return data;
	}
	
	public Return<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	/**
	 * 初始化
	 */
	public Return(){
		this.setFail("");
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 */
	public Return(boolean success, String msg){
		if(success){
			this.setSuccess(msg);
		}else{
			this.setFail(msg);
		}
	}
	
	public Return<T> setSuccess(String msg){
		this.code = "success";
		this.msg = msg;
		return this;
	}
	
	public Return<T> setSuccess(T data, String msg){
		this.code = "success";
		this.msg = msg;
		this.data = data;
		return this;
	}
	
	public Return<T> setFail(String errorMsg){
		this.code = "error";
		this.msg = errorMsg;
		return this;
	}
}
