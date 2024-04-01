package com.zja.fastjson.util;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

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

    @Data
    static class User {
        private String name;
        private int age;
    }

}
