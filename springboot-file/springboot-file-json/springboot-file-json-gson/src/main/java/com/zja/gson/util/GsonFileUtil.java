package com.zja.gson.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/01/18 14:58
 */
public class GsonFileUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void writeJsonToFile(Object object, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            gson.toJson(object, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readJsonFromFile(String filePath, Class<T> targetType) {
        try (FileReader fileReader = new FileReader(filePath)) {
            return gson.fromJson(fileReader, targetType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
