package com.zja.fastjson.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/04/01 14:06
 */
public class ResourcesFileUtilTest {

    @Test
    public void readJSONObject1_test() {
        JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("zhengja/addUser.json");
        System.out.println(jsonObject);

        User user = jsonObject.toJavaObject(User.class);
        System.out.println(user);
    }

    @Test
    public void readJSONObject2_test() {
        User user = ResourcesFileUtil.readJSONObjectFromFile("zhengja/addUser.json", User.class);
        System.out.println(user);
    }

    @Test
    public void readJSONObject3_test() {
        JSONArray jsonArray = ResourcesFileUtil.readJSONArrayFromFile("zhengja/userList.json");
        System.out.println(jsonArray);
    }

    @Test
    public void readJSONObject4_test() {
        List<User> list = ResourcesFileUtil.readJSONArrayFromFile("zhengja/userList.json", List.class);
        System.out.println(list);
    }

    @Data
    static class User {
        private String name;
        private int age;
    }

}
