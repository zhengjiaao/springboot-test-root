package com.zja.jackson.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/18 17:01
 */
public class JacksonFileUtilTest {

    @Test
    public void test() {
        // Writing to file
        Person person = new Person("John Doe", 30);
        JacksonFileUtil.writeToFile("person.json", person);

        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Jane Smith", 25));
        personList.add(new Person("Bob Johnson", 35));
        JacksonFileUtil.writeToFile("personList.json", personList);

        // Reading from file
        Person personFromFile = JacksonFileUtil.readFromFile("person.json", Person.class);
        System.out.println("Name: " + personFromFile.getName());
        System.out.println("Age: " + personFromFile.getAge());

        List<Person> personListFromFile = JacksonFileUtil.readListFromFile("personList.json", Person.class);
        for (Person p : personListFromFile) {
            System.out.println("Name: " + p.getName());
            System.out.println("Age: " + p.getAge());
            System.out.println();
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Person {
        @JsonProperty("name")
        private String name;

        @JsonProperty("age")
        private int age;
    }
}
