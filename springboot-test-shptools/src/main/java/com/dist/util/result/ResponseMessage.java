package com.dist.util.result;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/12 13:23
 */
public enum ResponseMessage {

	/**
	 * 成功
	 */
	OK(200,"成功"),
	
	/**
	 * 失败
	 */
	BAD_REQUEST(400,"错误的请求");

	
	private final int status;
	private final String message;
	
	 ResponseMessage(int status, String message){
		this.status = status;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	
}
