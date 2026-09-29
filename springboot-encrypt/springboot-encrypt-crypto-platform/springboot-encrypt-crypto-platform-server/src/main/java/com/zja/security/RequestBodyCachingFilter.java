package com.zja.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.common.ApiResponse;
import com.zja.common.Constants;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求体缓存过滤器：将 /api/** 请求包装为可重复读取，并限制请求体大小
 *
 * <p>认证拦截器需要先读取body计算签名，Controller再读取body反序列化；
 * 请求体超过 crypto.platform.max-body-size 时直接返回413，不进入后续处理，
 * 防止超大请求体（含缓存与反序列化的内存翻倍占用）造成的拒绝服务风险。</p>
 *
 * @author: zhengja
 */
public class RequestBodyCachingFilter extends OncePerRequestFilter {

	/** 过滤器在MVC之外执行，错误响应自行序列化 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/** 请求体大小上限（字节） */
	private final long maxBodySize;

	public RequestBodyCachingFilter(long maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!isApiPath(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 已声明Content-Length时提前拒绝，避免读取超大请求体
		if (request.getContentLengthLong() > maxBodySize) {
			writePayloadTooLarge(response);
			return;
		}
		try {
			filterChain.doFilter(new CachedBodyRequestWrapper(request, maxBodySize), response);
		} catch (PayloadTooLargeException e) {
			// 分块传输（无Content-Length）场景由包装器读取时兜底拦截
			writePayloadTooLarge(response);
		}
	}

	private boolean isApiPath(HttpServletRequest request) {
		return request.getRequestURI() != null && request.getRequestURI().startsWith("/api/");
	}

	private void writePayloadTooLarge(HttpServletResponse response) throws IOException {
		response.setStatus(Constants.CODE_PAYLOAD_TOO_LARGE);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(OBJECT_MAPPER.writeValueAsString(
				ApiResponse.fail(Constants.CODE_PAYLOAD_TOO_LARGE,
						"请求体超过平台限制（上限" + maxBodySize + "字节），请减小数据量后重试")));
	}
}
