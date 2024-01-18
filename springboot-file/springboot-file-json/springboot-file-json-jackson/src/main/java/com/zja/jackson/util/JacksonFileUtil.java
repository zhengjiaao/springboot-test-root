package com.zja.jackson.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/18 16:58
 */
public class JacksonFileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void writeToFile(String filePath, T object) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(filePath), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readFromFile(String filePath, Class<T> valueType) {
        try {
            return objectMapper.readValue(new File(filePath), valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readListFromFile(String filePath, Class<T> elementType) {
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(List.class, elementType);
            return objectMapper.readValue(new File(filePath), collectionType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
