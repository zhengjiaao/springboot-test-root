package com.zja.common;

import lombok.Data;

/**
 * 统一响应结果
 *
 * @author: zhengja
 */
@Data
public class ApiResponse<T> {

	/** 成功响应码 */
	public static final int CODE_SUCCESS = 200;

	/** 响应码：200成功，其它为失败 */
	private int code;
	/** 提示信息 */
	private String message;
	/** 业务数据 */
	private T data;

	public ApiResponse() {
	}

	public ApiResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(CODE_SUCCESS, "success", data);
	}

	public static <T> ApiResponse<T> ok() {
		return new ApiResponse<>(CODE_SUCCESS, "success", null);
	}

	public static <T> ApiResponse<T> fail(int code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	/** 是否成功（供SDK端判断使用） */
	@com.fasterxml.jackson.annotation.JsonIgnore
	public boolean isSuccess() {
		return code == CODE_SUCCESS;
	}
}
