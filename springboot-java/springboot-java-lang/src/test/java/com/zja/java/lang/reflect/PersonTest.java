package com.zja.java.lang.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @Author: zhengja
 * @Date: 2024-05-28 9:58
 */
public class PersonTest {

    // 通过反射，打印出任何 "Person" 对象的属性值
    @Test
    public void printPersonInfo_test() {
        Person person = new Person("John Doe", 18);
        printPersonInfo(person);
    }

    @Test
    public void modifyPrivateFields_test() {
        modifyPrivateFields();
    }

    @Data
    public static class Person {

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }


    /**
     * 通过反射，打印出任何 "Person" 对象的属性值
     * <p>
     * * 获取 "Person" 对象的类
     * * 获取类的所有字段
     * * 设置字段为可访问
     * * 获取每个字段的名称和值
     * * 打印出这些信息
     * </p>
     *
     * @param obj
     */
    public static void printPersonInfo(Object obj) {
        try {
            // 获取对象的类
            Class<?> clazz = obj.getClass();

            // 获取类的所有字段
            Field[] fields = clazz.getDeclaredFields();

            System.out.println("Object information:");
            for (Field field : fields) {
                // 设置字段为可访问
                field.setAccessible(true);

                // 获取字段名称和值
                String fieldName = field.getName();
                Object fieldValue = field.get(obj);

                System.out.println(fieldName + ": " + fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 使用反射访问和修改私有字段
    public static void modifyPrivateFields() {
        try {
            // 创建 Person 对象
            Person person = new Person("John Doe", 30);

            // 获取 Person 类的 Class 对象
            Class<?> personClass = person.getClass();

            // 获取 name 字段
            Field nameField = personClass.getDeclaredField("name");

            // 设置 name 字段可访问
            nameField.setAccessible(true);

            // 修改 name 字段的值
            nameField.set(person, "Jane Doe");

            System.out.println("Name: " + person.getName()); // 输出 "Jane Doe"

            // 获取 age 字段
            Field ageField = personClass.getDeclaredField("age");

            // 设置 age 字段可访问
            ageField.setAccessible(true);

            // 修改 age 字段的值
            ageField.set(person, 35);

            System.out.println("Age: " + person.getAge()); // 输出 "35"
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
