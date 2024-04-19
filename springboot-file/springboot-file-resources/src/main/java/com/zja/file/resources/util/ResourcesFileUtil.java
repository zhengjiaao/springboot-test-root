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

public class ResourcesFileUtil {

    private ResourcesFileUtil() {}

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
    public static InputStream getResourcesFileInputStreamV2(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
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
