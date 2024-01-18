package com.zja.gson.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/18 15:02
 */
public class GsonFileUtilTest {

    private static final String jsonFilePathGSON = "D:\\temp\\json\\GSON.json";

    @Test
    public void gson() {
        // Writing JSON to file
        Person person = new Person("John", 30, "上海");
        GsonFileUtil.writeJsonToFile(person, jsonFilePathGSON);

        // Reading JSON from file
        Person readPerson = GsonFileUtil.readJsonFromFile(jsonFilePathGSON, Person.class);
        System.out.println(readPerson);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Person {
        private String name;
        private int age;
        private String city;
    }
}
