package com.zja.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.common.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 请求体大小限制测试：包装器有界读取（单测） + 超限413（集成）
 *
 * <p>本测试类将 max-body-size 调小为1024字节，便于构造超限请求。</p>
 *
 * @author: zhengja
 */
@DisplayName("请求体缓存：有界读取与413保护")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "crypto.platform.max-body-size=1024")
class RequestBodyLimitTest {

	@LocalServerPort
	private int port;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("包装器：请求体超过上限抛出PayloadTooLargeException")
	void wrapperRejectsOversizeBody() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContent(new byte[2048]);

		assertThrows(PayloadTooLargeException.class, () -> new CachedBodyRequestWrapper(request, 1024));
	}

	@Test
	@DisplayName("包装器：上限内请求体正常缓存且可重复读取")
	void wrapperCachesWithinLimit() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContent("hello".getBytes(StandardCharsets.UTF_8));

		CachedBodyRequestWrapper wrapper = new CachedBodyRequestWrapper(request, 1024);
		assertEquals(5, wrapper.getCachedBody().length);
		// 拦截器读一次、Controller读一次：同一body可重复读取
		assertEquals(5, StreamUtils.copyToByteArray(wrapper.getInputStream()).length);
		assertEquals(5, StreamUtils.copyToByteArray(wrapper.getInputStream()).length);
	}

	@Test
	@DisplayName("过滤器：超限请求返回413，上限内请求放行进入认证")
	void filterRejectsOversizeHttpRequest() throws Exception {
		// 超限请求（无需合法签名：过滤器在认证之前拦截）
		HttpURLConnection overflow = post("/api/crypto/digest", new byte[2048]);
		assertEquals(413, overflow.getResponseCode());
		String overflowBody = readAll(overflow.getErrorStream());
		overflow.disconnect();
		assertEquals(413, objectMapper.readValue(overflowBody, ApiResponse.class).getCode());

		// 上限内请求通过过滤器，被认证拦截器拒绝（无签名 → 401）
		HttpURLConnection normal = post("/api/crypto/digest", new byte[16]);
		assertEquals(200, normal.getResponseCode());
		String normalBody = readAll(normal.getInputStream());
		normal.disconnect();
		assertEquals(401, objectMapper.readValue(normalBody, ApiResponse.class).getCode());
	}

	private HttpURLConnection post(String path, byte[] body) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + port + path).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		try (OutputStream out = connection.getOutputStream()) {
			out.write(body);
		}
		return connection;
	}

	private String readAll(InputStream inputStream) throws Exception {
		if (inputStream == null) {
			return "";
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] chunk = new byte[4096];
		int n;
		while ((n = inputStream.read(chunk)) != -1) {
			buffer.write(chunk, 0, n);
		}
		return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
	}
}
