package com.zja.common.exception;

import com.zja.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author: zhengja
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ApiResponse<Void> handleBusinessException(BusinessException e) {
		return ApiResponse.fail(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({MissingServletRequestParameterException.class, IllegalArgumentException.class})
	public ApiResponse<Void> handleBadRequest(Exception e) {
		return ApiResponse.fail(400, "请求参数错误：" + e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ApiResponse<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		return ApiResponse.fail(405, "请求方式不支持：" + e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ApiResponse<Void> handleNotReadable(HttpMessageNotReadableException e) {
		return ApiResponse.fail(400, "请求体格式错误，请检查JSON格式是否正确");
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse<Void> handleException(Exception e) {
		log.error("平台内部异常", e);
		// 不向客户端透出内部异常细节，防止信息泄漏
		return ApiResponse.fail(500, "平台内部异常，请稍后重试或联系平台管理员");
	}
}
