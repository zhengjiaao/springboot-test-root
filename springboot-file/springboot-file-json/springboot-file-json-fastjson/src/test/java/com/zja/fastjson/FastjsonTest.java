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


    @Test
    public void test3() {
        // String jsonString = "{\"ServiceType\":\"压占分析服务\",\"imgString\":\"{\\\"envelope\\\":{\\\"xMax\\\":113.076753379,\\\"xMin\\\":112.961187699,\\\"yMax\\\":28.090095543,\\\"yMin\\\":28.005448417},\\\"imgData\\\":\\\"data:image/png;base64,5oiq5Zu+6IyD5Zu06L+H5aSnLCDmuIXmo4Dmn6Xlj4LmlbAK\\\"}\",\"ServiceName\":\"SCGTDCY2021G\",\"Results\":\"Read timed out\",\"CurrentYear\":\"2021\",\"status\":-1,\"ServiceAlias\":\"2021年国土调查年度变更地类图斑\",\"order\":1,\"ServiceId\":\"684e8165-4291-4efc-8def-f87d94f1dd8c\"}";
        String jsonString = "{\"ServiceType\":\"压占分析服务\",\"imgString\":\"{\\\"envelope\\\":{\\\"xMax\\\":113.082670726,\\\"xMin\\\":113.028793156,\\\"yMax\\\":28.08438304,\\\"yMin\\\":28.032028777},\\\"imgData\\\":\\\"data:image/png;base64,5oiq5Zu+6IyD5Zu06L+H5aSnLCDmuIXmo4Dmn6Xlj4LmlbAK\\\"}\",\"ServiceName\":\"LXGHGKJSGK\",\"Results\":[{\"OBJECTID\":1,\"DK_ID\":\"123\",\"DISTANCE\":50.0,\"YZBL\":0.010220384425120039,\"DK_MJ\":1.6637114268221118E7,\"YZMJ\":32459.83565148942,\"MJ\":3177521,\"GKFQ\":\"临水区\"},{\"OBJECTID\":2,\"DK_ID\":\"123\",\"DISTANCE\":100.0,\"YZBL\":0.009017127273083176,\"DK_MJ\":1.6637114268221118E7,\"YZMJ\":91349.35339975063,\"MJ\":10138638,\"GKFQ\":\"近水区\"}],\"CurrentYear\":\"2024\",\"status\":1,\"ServiceAlias\":\"长株潭绿心管控近水管控\",\"order\":12,\"ServiceId\":\"cf10b006-886b-4a23-a47b-94efc1ef71f4\"}";

        // Parse JSON string into JSONObject
        JSONObject jsonObject = JSON.parseObject(jsonString);

        // Access top-level properties
        String ServiceType = jsonObject.getString("ServiceType");
        JSONObject imgString = jsonObject.getJSONObject("imgString");
        String imgData = imgString.getString("imgData");
        String Results = jsonObject.getString("Results");

        System.out.println("ServiceType: " + ServiceType);
        System.out.println("imgData: " + imgData);
        System.out.println("Results: " + Results);

        if (Results.equals("Read timed out")) {
            System.out.println("Results: " + Results);
        } else {
            JSONArray resultsArray = JSON.parseArray(Results);
            for (int i = 0; i < resultsArray.size(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String DK_ID = resultObject.getString("DK_ID");
                String DISTANCE = resultObject.getString("DISTANCE");
                String YZBL = resultObject.getString("YZBL");
                String YZMJ = resultObject.getString("YZMJ");
                String DLBM = resultObject.getString("DLBM");
                String DLMC = resultObject.getString("DLMC");

                System.out.println("DK_ID: " + DK_ID);
                System.out.println("DISTANCE: " + DISTANCE);
                System.out.println("YZBL: " + YZBL);
                System.out.println("YZMJ: " + YZMJ);
                System.out.println("DLBM: " + DLBM);
                System.out.println("DLMC: " + DLMC);
            }
        }
    }
}
