package com.zja.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2024/01/18 16:43
 */
public class JacksonTest {

    @Test
    public void test() {
        String json = "{\"id\": 1, \"name\": \"John Doe\", \"age\": 30}";
        // json字符串转 对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Person person = objectMapper.readValue(json, Person.class);

            System.out.println("Deserialized Person object:");
            System.out.println("ID: " + person.getId());
            System.out.println("Name: " + person.getName());
            System.out.println("Age: " + person.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Serialize Java objects to JSON
        // 对象转 json字符串
        Person person = new Person(2, "Jane Smith", 25);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化
            String serializedJson = objectMapper.writeValueAsString(person); // 支持对象、对象集合、Map等转为json字符串

            System.out.println("\nSerialized JSON:");
            System.out.println(serializedJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() {
        String json = "{\"name\":\"John Doe\",\"age\":30}";

        // json字符串转 Map
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            System.out.println("JSON to Map:");
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // json字符串转 对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Person person = objectMapper.readValue(json, Person.class);
            System.out.println("JSON to Object:");
            System.out.println("Name: " + person.getName());
            System.out.println("Age: " + person.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // json字符串转 对象集合
        String json2 = "[{\"name\":\"John Doe\",\"age\":30},{\"name\":\"Jane Smith\",\"age\":25}]";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Person> personList = objectMapper.readValue(json2, new TypeReference<List<Person>>() {
            });
            System.out.println("JSON to Object Collection:");
            for (Person person : personList) {
                System.out.println("Name: " + person.getName());
                System.out.println("Age: " + person.getAge());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 复杂json 提取值
    @Test
    public void test3() {
        String json = "{\n" +
                "  \"person\": {\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"age\": 30,\n" +
                "    \"address\": {\n" +
                "      \"street\": \"123 Main St\",\n" +
                "      \"city\": \"New York\",\n" +
                "      \"country\": \"USA\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            // Extracting values
            String name = rootNode.get("person").get("name").asText();
            int age = rootNode.get("person").get("age").asInt();
            String street = rootNode.get("person").get("address").get("street").asText();
            String city = rootNode.get("person").get("address").get("city").asText();
            String country = rootNode.get("person").get("address").get("country").asText();

            // Printing extracted values
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Street: " + street);
            System.out.println("City: " + city);
            System.out.println("Country: " + country);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Person {
        @JsonProperty("id")
        private int id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("age")
        private int age;
    }
}
