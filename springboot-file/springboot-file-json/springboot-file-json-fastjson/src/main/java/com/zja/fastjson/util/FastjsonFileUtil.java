package com.zja.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/01/18 14:19
 */
public class FastjsonFileUtil {

    public static void writeJsonToFile(JSONObject jsonObject, String filePath) {
        writeJsonToFile(jsonObject, filePath, Boolean.FALSE);
    }

    public static void writeJsonToFile(JSONObject jsonObject, String filePath, boolean prettyFormat) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            String jsonString = null;
            if (prettyFormat) {
                jsonString = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
            } else {
                jsonString = jsonObject.toJSONString();
            }
            fileWriter.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("writeJsonToFile fail：" + e);
        }
    }

    public static JSONObject readJsonFromFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
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

    public static void writeJsonArrayToFile(JSONArray jsonArray, String filePath) {
        writeJsonArrayToFile(jsonArray, filePath, Boolean.FALSE);
    }

    public static void writeJsonArrayToFile(JSONArray jsonArray, String filePath, boolean prettyFormat) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            String jsonString = null;
            if (prettyFormat) {
                jsonString = JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat);
            } else {
                jsonString = jsonArray.toJSONString();
            }
            fileWriter.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("writeJsonArrayToFile fail：" + e);
        }
    }

    public static JSONArray readJsonArrayFromFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
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
