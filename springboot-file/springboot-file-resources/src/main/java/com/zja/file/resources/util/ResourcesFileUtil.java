package com.zja.file.resources.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/04/18 16:40
 */
public class ResourcesFileUtil {

    private ResourcesFileUtil() {
    }

    /**
     * 读取文件流，优先读取classpath下的文件，如未找到，则读取本地文件
     */
    public static InputStream getInputStream(String fileName) throws IOException {
        try {
            return getResourcesFileInputStream(fileName);
        } catch (IOException e) {
            try {
                return getFileInputStream(fileName);
            } catch (IOException e1) {
                e.addSuppressed(e1);
                throw new IOException("file not found：" + fileName, e);
            }
        }
    }

    // 读取本地文件流
    private static InputStream getFileInputStream(String filePath) throws IOException {
        return Files.newInputStream(Paths.get(filePath));
    }

    /**
     * 读取资源文件流（支持读取jar下面的资源文件）
     */
    public static InputStream getResourcesFileInputStream(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return resource.getInputStream();
    }

    /**
     * 读取资源文件流（注：但无法读取jar下面的资源文件）
     */
    @Deprecated
    public static InputStream getResourcesFileInputStreamV2(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    public static String readFileByUTF8(String pathName) {
        return readFile(pathName, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件全部内容，例如：txt、json等
     *
     * @param pathName 文件路径
     * @param charset  编码格式
     * @return 文件内容
     */
    public static String readFile(String pathName, Charset charset) {
        try (InputStream inputStream = getInputStream(pathName)) {
            // 读取文件内容
            StringBuilder content = new StringBuilder();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                content.append(new String(buffer, 0, length, charset));
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + pathName, e);
        }
    }

    public static JSONObject readJSONObjectFromFile(String pathName) {
        return readJSONObjectFromFile(pathName, JSONObject.class);
    }

    public static <T> T readJSONObjectFromFile(String pathName, Class<T> clazz) {
        InputStream is = null;
        try {
            Charset charset = StandardCharsets.UTF_8;
            is = getResourcesFileInputStream(pathName);
            return JSON.parseObject(is, charset, clazz, Feature.IgnoreAutoType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray readJSONArrayFromFile(String pathName) {
        return readJSONObjectFromFile(pathName, JSONArray.class);
    }

    public static <T> T readJSONArrayFromFile(String pathName, Class<T> clazz) {
        return readJSONObjectFromFile(pathName, clazz);
    }

}
