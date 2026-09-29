package com.zja.common.exception;

import com.zja.common.Constants;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author: zhengja
 */
@Getter
public class BusinessException extends RuntimeException {

	private final int code;

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public static BusinessException badRequest(String message) {
		return new BusinessException(Constants.CODE_BAD_REQUEST, message);
	}

	public static BusinessException unauthorized(String message) {
		return new BusinessException(Constants.CODE_UNAUTHORIZED, message);
	}

	public static BusinessException forbidden(String message) {
		return new BusinessException(Constants.CODE_FORBIDDEN, message);
	}

	public static BusinessException notFound(String message) {
		return new BusinessException(Constants.CODE_NOT_FOUND, message);
	}

	public static BusinessException error(String message) {
		return new BusinessException(Constants.CODE_INTERNAL_ERROR, message);
	}
}
