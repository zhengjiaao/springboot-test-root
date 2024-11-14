package com.zja.mvc.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-11-13 21:35
 */
@Slf4j
public class HttpServletResponseUtil {

    private HttpServletResponseUtil() {
    }

    /**
     * 下载文件
     *
     * @param response HttpServletResponse
     * @param filePath 文件路径，默认市把文件名称设置为下载名称
     * @throws IOException IO异常
     */
    public static void writeFileStream(HttpServletResponse response, String filePath) throws IOException {
        writeFileStream(response, FilenameUtils.getName(filePath), filePath);
    }

    /**
     * 下载文件
     *
     * @param response HttpServletResponse
     * @param fileName 设置文件下载名称
     * @param filePath 文件路径
     * @throws IOException IO异常
     */
    public static void writeFileStream(HttpServletResponse response, String fileName, String filePath) throws IOException {
        writeFileStream(response, fileName, Files.newInputStream(Paths.get(filePath)));
    }

    /**
     * 下载文件
     *
     * @param response HttpServletResponse
     * @param fileName 设置文件下载名称
     * @param stream 文件流，注：写入完成后，会进行关闭文件流
     * @throws IOException IO异常
     */
    public static void writeFileStream(HttpServletResponse response, String fileName, InputStream stream) throws IOException {
        // 设置下载时的文件名称
        if (StringUtils.hasText(fileName)) {
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
        }

        // 设置响应头以支持文件下载
        response.setContentType("application/octet-stream");

        // 将输入流中的数据写入输出流
        try (OutputStream out = response.getOutputStream()) {
            IOUtils.copy(stream, out);
        } catch (IOException e) {
            log.error("写入文件流时发生异常: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
