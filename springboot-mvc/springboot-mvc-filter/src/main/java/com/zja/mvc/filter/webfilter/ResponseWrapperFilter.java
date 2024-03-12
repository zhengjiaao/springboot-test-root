package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * 响应包装：
 * 场景：对响应进行包装，以添加额外的功能或修改响应的属性，例如压缩响应内容或添加自定义的HTTP头信息
 *
 * @author: zhengja
 * @since: 2024/03/11 16:26
 */
// @WebFilter(urlPatterns = "/*")
public class ResponseWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 创建自定义的响应包装器，并传递给下一个过滤器或Servlet
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper(httpResponse);

        chain.doFilter(request, responseWrapper);

        // 对响应进行处理，例如压缩响应内容
        compressResponse(responseWrapper);
    }

    // 其他方法：init() 和 destroy()

    private void compressResponse(CustomResponseWrapper responseWrapper) throws IOException {
        byte[] originalContent = responseWrapper.getOriginalContent();

        // 压缩响应内容
        ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedContent)) {
            gzipOutputStream.write(originalContent);
        }

        // 设置响应头
        responseWrapper.setHeader("Content-Encoding", "gzip");
        responseWrapper.setContentLength(compressedContent.size());
        responseWrapper.getOutputStream().write(compressedContent.toByteArray());
    }

    private static class CustomResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream outputStream;

        public CustomResponseWrapper(HttpServletResponse response) {
            super(response);
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return new ServletOutputStreamWrapper(outputStream);
        }

        public byte[] getOriginalContent() {
            return outputStream.toByteArray();
        }
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {
        private final ByteArrayOutputStream outputStream;

        public ServletOutputStreamWrapper(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

        // 其他重写方法
    }
}