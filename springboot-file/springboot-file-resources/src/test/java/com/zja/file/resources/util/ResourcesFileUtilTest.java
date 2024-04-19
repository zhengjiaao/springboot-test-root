package com.zja.file.resources.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 单元测试：读取资源下的文件，支持打成jar包后读取资源文件流程和json文件
 * <p>
 * 结论：当test/resources 下面存在 "mock/UserV2.json" 文件时，则直接读取。不存在时，会从mian/resources 下面读取"mock/UserV2.json" 文件
 *
 * @author: zhengja
 * @since: 2024/04/01 14:06
 */
// @SpringBootTest  //  添加此注解，不影响，“结论”是不变的
public class ResourcesFileUtilTest {

    @Test
    public void readJSONObject1_test() {
        JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("mock/User.json");
        System.out.println(jsonObject);

        User user = jsonObject.toJavaObject(User.class);
        System.out.println(user);
    }

    @Test
    public void readJSONObject2_test() {
        User user = ResourcesFileUtil.readJSONObjectFromFile("mock/User.json", User.class);
        System.out.println(user);
    }

    // 验证 当test/resources 下面不存在 "mock/UserV2.json" 文件时，会从mian/resources 下面读取"mock/UserV2.json" 文件
    @Test
    public void readJSONObject2_1_test() {
        User user = ResourcesFileUtil.readJSONObjectFromFile("mock/UserV2.json", User.class);
        System.out.println(user);
    }

    // 验证资源文件不存在，则抛出异常
    @Test
    public void readJSONObject2_2_test() {
        try {
            User user = ResourcesFileUtil.readJSONObjectFromFile("mock/UserV3.json", User.class);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void readJSONObject3_test() {
        JSONArray jsonArray = ResourcesFileUtil.readJSONArrayFromFile("mock/UserList.json");
        System.out.println(jsonArray);
    }

    @Test
    public void readJSONObject4_test() {
        List<User> list = ResourcesFileUtil.readJSONArrayFromFile("mock/UserList.json", List.class);
        System.out.println(list);
    }


    @Data
    static class User {
        private String name;
        private int age;
    }

}
