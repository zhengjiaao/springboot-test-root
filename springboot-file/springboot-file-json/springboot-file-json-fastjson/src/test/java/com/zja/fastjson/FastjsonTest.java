package com.zja.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2024/01/18 13:57
 */
public class FastjsonTest {

    /**
     * Fastjson 进行 json 字符串互相转换：对象、Map、对象集合List、JSONObject
     */
    @Test
    public void test() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("李四");
        userInfo.setAge(18);

        String jsonString = JSON.toJSONString(userInfo);
        System.out.println("jsonString=" + jsonString);

        System.out.println("===========");

        // json字符串转：对象、Map、JSONObject
        UserInfo userInfo1 = JSON.parseObject(jsonString, UserInfo.class);
        System.out.println("json字符串转 对象：" + userInfo1);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println("json字符串转 JSONObject：" + jsonObject);
        Map<String, Object> map = (Map<String, Object>) jsonObject;
        System.out.println("json字符串转 map：" + map);

        System.out.println("===========");

        // 对象、Map、JSONObject 转为 json字符串
        String jsonStringMap = JSON.toJSONString(map);
        System.out.println("map 转为json字符串：" + jsonStringMap);
        String jsonStringObject = JSON.toJSONString(userInfo1);
        System.out.println("对象 转为json字符串：" + jsonStringObject);
        String jsonStringJSONObject = JSON.toJSONString(jsonObject);
        System.out.println("JSONObject 转为json字符串：" + jsonStringJSONObject);

        System.out.println("===========");

        // 字符串转：对象集合
        List<UserInfo> users = Arrays.asList(userInfo);
        String usersJsonStr = JSON.toJSONString(users);
        System.out.println("对象集合 转为json字符串：" + usersJsonStr);
        List<UserInfo> userList = JSON.parseArray(usersJsonStr, UserInfo.class);
        System.out.println("json字符串转 对象集合：" + userList);

    }


    /**
     * 复杂json解析
     */
    @Test
    public void test2() {
        String jsonString = "{ \"name\": \"John\", \"age\": 30, \"address\": { \"street\": \"123 Main St\", \"city\": \"New York\" }, \"hobbies\": [\"reading\", \"painting\", \"swimming\"] }";

        JSONObject jsonObject = JSON.parseObject(jsonString);

        // Access top-level properties
        String name = jsonObject.getString("name");
        int age = jsonObject.getIntValue("age");

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);

        // Access nested object
        JSONObject address = jsonObject.getJSONObject("address");
        String street = address.getString("street");
        String city = address.getString("city");

        System.out.println("Street: " + street);
        System.out.println("City: " + city);

        // Access nested array
        JSONArray hobbies = jsonObject.getJSONArray("hobbies");
        for (int i = 0; i < hobbies.size(); i++) {
            String hobby = hobbies.getString(i);
            System.out.println("Hobby " + (i + 1) + ": " + hobby);
        }
    }


    @Data
    static class UserInfo {
        private String name;
        private int age;
    }


}
