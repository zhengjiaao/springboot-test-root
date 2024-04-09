package com.zja.poitl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author: zhengja
 * @since: 2024/04/01 14:16
 */
public class ResourcesFileUtil {

    public static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    public static String getPath() {
        return Objects.requireNonNull(ResourcesFileUtil.class.getResource("/")).getPath();
    }

    public static File getFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static JSONObject readJSONObjectFromFile(String pathName) {
        File file = getFile(pathName);
        return readJsonFromFile(file);
    }

    public static <T> T readJSONObjectFromFile(String pathName, Class<T> clazz) {
        File file = getFile(pathName);
        JSONObject jsonObject = readJsonFromFile(file);
        return jsonObject.toJavaObject(clazz);
    }

    public static JSONArray readJSONArrayFromFile(String pathName) {
        File file = getFile(pathName);
        return readJsonArrayFromFile(file);
    }

    public static <T> T readJSONArrayFromFile(String pathName, Class<T> clazz) {
        File file = getFile(pathName);
        JSONArray jsonArray = readJsonArrayFromFile(file);
        return jsonArray.toJavaObject(clazz);
    }

    private static JSONObject readJsonFromFile(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = fileReader.read()) != -1) {
                stringBuilder.append((char) character);
            }
            return JSON.parseObject(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("readJsonFromFile fail：" + e);
        }
    }

    private static JSONArray readJsonArrayFromFile(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = fileReader.read()) != -1) {
                stringBuilder.append((char) character);
            }
            return JSON.parseArray(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("readJsonArrayFromFile fail：" + e);
        }
    }

}
