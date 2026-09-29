package com.zja.security;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 可重复读取请求体的Request包装（有界缓存，防止超大请求体耗尽内存）
 *
 * <p>拦截器校验签名需要读取一次body，Controller反序列化@RequestBody还需要再读一次，
 * 故先由过滤器将body缓存为字节数组，之后每次读取都从缓存中返回；
 * 读取总量超过上限时抛出 {@link PayloadTooLargeException}，由过滤器统一返回413。</p>
 *
 * @author: zhengja
 */
public class CachedBodyRequestWrapper extends HttpServletRequestWrapper {

	/** 读取块大小（字节） */
	private static final int CHUNK_SIZE = 4096;

	private final byte[] cachedBody;

	public CachedBodyRequestWrapper(HttpServletRequest request, long maxBodySize) throws IOException {
		super(request);
		this.cachedBody = readBounded(request.getInputStream(), maxBodySize);
	}

	/** 有界读取请求体：累计超过上限立即中断，防止超大请求体占用内存 */
	private static byte[] readBounded(ServletInputStream inputStream, long maxBodySize) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] chunk = new byte[CHUNK_SIZE];
		long total = 0;
		int n;
		while ((n = inputStream.read(chunk)) != -1) {
			total += n;
			if (total > maxBodySize) {
				throw new PayloadTooLargeException("请求体超过大小上限：" + maxBodySize + "字节");
			}
			buffer.write(chunk, 0, n);
		}
		return buffer.toByteArray();
	}

	/** 获取缓存的请求体（GET请求返回空数组） */
	public byte[] getCachedBody() {
		return cachedBody;
	}

	@Override
	public ServletInputStream getInputStream() {
		ByteArrayInputStream bais = new ByteArrayInputStream(cachedBody);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return bais.available() == 0;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// 同步读取无需异步监听
			}

			@Override
			public int read() {
				return bais.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
	}
}
