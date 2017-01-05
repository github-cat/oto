package com.changhong.oto.common.utils;

/**
 * 主要用于正常返回的情况
 * 
 * @ClassName AjaxResult
 * @Description TODO
 * @author liuchengyong
 * @Date 2016-12-7 上午10:52:39
 * @version 1.0.0
 */
public class AjaxResult {

	private String msg; // 响应消息
	private Integer errorCode = 0; // 错误编码
	private Object data; // 数据集
	
	/**
	 * @Description TODO
	 */
	public AjaxResult() {
		super();
	}

	
	public AjaxResult(String msg, Object data) {
		this.data = data;
		this.msg = msg;
	}

	public AjaxResult(Object data) {
		this.data = data;
	}

	public AjaxResult(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @Description TODO
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @Description TODO
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
