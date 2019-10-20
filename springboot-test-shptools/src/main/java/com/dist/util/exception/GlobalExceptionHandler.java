package com.dist.util.exception;


import com.dist.util.result.ResponseResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**局异常捕获处理类
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 15:09
 */
@CrossOrigin
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseResult processException(Exception ex, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return new ResponseResult(400, ex);
	}

}
