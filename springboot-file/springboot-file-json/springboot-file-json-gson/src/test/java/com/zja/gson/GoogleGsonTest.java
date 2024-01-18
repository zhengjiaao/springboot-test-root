package com.zja.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2024/01/18 14:48
 */
public class GoogleGsonTest {

    @Test
    public void test() {
        Gson gson = new Gson();
        // Gson gson = new GsonBuilder().setPrettyPrinting().create(); // 漂亮格式化json字符串打印

        String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"上海\"}";

        System.out.println("jsonString:" + jsonString);

        // json字符串转：对象、Map等
        System.out.println("===========");

        Person person = gson.fromJson(jsonString, Person.class);
        System.out.println("json字符串转 对象：" + person);

        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> dataMap = gson.fromJson(jsonString, mapType);
        System.out.println("json字符串转 Map：" + dataMap);

        // 转json字符串
        System.out.println("===========");
        String jsonString5 = gson.toJson(person);
        System.out.println("对象 转json字符串：" + jsonString5);
        String jsonString6 = gson.toJson(dataMap);
        System.out.println("Map 转json字符串：" + jsonString6);
        System.out.println("===========");

        String jsonString2 = "[{\"name\":\"John\",\"age\":30,\"city\":\"New York\"},{\"name\":\"Alice\",\"age\":25,\"city\":\"London\"}]";
        Type listType = new TypeToken<List<Person>>() {
        }.getType();
        List<Person> personList = gson.fromJson(jsonString2, listType);
        System.out.println("json字符串转 对象集合：" + personList);

        String jsonString8 = gson.toJson(personList);
        System.out.println("对象集合 转json字符串：" + jsonString8);
    }


    // gson 读取复杂json字符串实例
    @Test
    public void test2() {
        // Complex JSON string
        String jsonString = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"address\": {\n" +
                "    \"street\": \"123 Main St\",\n" +
                "    \"city\": \"New York\",\n" +
                "    \"country\": \"USA\"\n" +
                "  },\n" +
                "  \"phoneNumbers\": [\n" +
                "    \"123456789\",\n" +
                "    \"987654321\"\n" +
                "  ],\n" +
                "  \"languages\": {\n" +
                "    \"english\": true,\n" +
                "    \"french\": false,\n" +
                "    \"german\": true\n" +
                "  }\n" +
                "}";

        // Convert JSON string to Java objects
        Gson gson = new Gson();

        // Convert address JSON to Map
        Type dataType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> data = gson.fromJson(jsonString, dataType);

        // Extract address from the data map
        Map<String, String> address = (Map<String, String>) data.get("address");

        // Print the converted objects
        System.out.println("Name: " + data.get("name"));
        System.out.println("Age: " + data.get("age"));
        System.out.println("Address: " + address);
    }

    // gson 读取多级属性的值
    @Test
    public void test3() {
        String jsonString = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"address\": {\n" +
                "    \"street\": \"123 Main St\",\n" +
                "    \"city\": \"New York\",\n" +
                "    \"country\": \"USA\"\n" +
                "  },\n" +
                "  \"phoneNumbers\": [\n" +
                "    \"123456789\",\n" +
                "    \"987654321\"\n" +
                "  ],\n" +
                "  \"languages\": {\n" +
                "    \"english\": true,\n" +
                "    \"french\": false,\n" +
                "    \"german\": true\n" +
                "  }\n" +
                "}";

        // Convert JSON string to JsonObject
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // Access values of nested fields
        String name = jsonObject.get("name").getAsString();
        int age = jsonObject.get("age").getAsInt();
        String street = jsonObject.getAsJsonObject("address").get("street").getAsString();
        String city = jsonObject.getAsJsonObject("address").get("city").getAsString();
        String country = jsonObject.getAsJsonObject("address").get("country").getAsString();

        // Print the values
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Street: " + street);
        System.out.println("City: " + city);
        System.out.println("Country: " + country);
    }

    @Data
    static class Person {
        private String name;
        private int age;
        private String city;
    }
}
