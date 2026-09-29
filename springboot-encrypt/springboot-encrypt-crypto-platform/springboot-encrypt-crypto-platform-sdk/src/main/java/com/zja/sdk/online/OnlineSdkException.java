package com.zja.sdk.online;

import lombok.Getter;

/**
 * 在线SDK异常：携带平台返回的业务码
 *
 * @author: zhengja
 */
@Getter
public class OnlineSdkException extends RuntimeException {

	private final int code;

	public OnlineSdkException(int code, String message) {
		super(message);
		this.code = code;
	}
}
