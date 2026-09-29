package com.zja.security;

import java.io.IOException;

/**
 * 请求体超过大小上限异常
 *
 * <p>由 {@link CachedBodyRequestWrapper} 在有界读取请求体时抛出，
 * {@link RequestBodyCachingFilter} 捕获后统一返回413，防止超大请求体耗尽内存。</p>
 *
 * @author: zhengja
 */
public class PayloadTooLargeException extends IOException {

	public PayloadTooLargeException(String message) {
		super(message);
	}
}
