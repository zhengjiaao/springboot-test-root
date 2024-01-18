package com.zja.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/18 14:24
 */
public class FastjsonFileUtilTest {

    private static final String jsonFilePathJSONObject = "D:\\temp\\json\\JSONObject.json";
    private static final String jsonFilePathJSONArray = "D:\\temp\\json\\JSONArray.json";

    @Test
    public void JSONObjectTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("李四");
        userInfo.setAge(18);

        String jsonString = JSON.toJSONString(userInfo);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println("jsonObject=" + jsonObject);

        FastjsonFileUtil.writeJsonToFile(jsonObject, jsonFilePathJSONObject, true);
        JSONObject jsonObject1 = FastjsonFileUtil.readJsonFromFile(jsonFilePathJSONObject);

        System.out.println("jsonObject1=" + jsonObject1);
    }

    @Test
    public void JSONArrayTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("李四");
        userInfo.setAge(18);

        List<UserInfo> userInfoList = Collections.singletonList(userInfo);

        String jsonString = JSON.toJSONString(userInfoList);

        JSONArray jsonArray = JSON.parseArray(jsonString);
        System.out.println("jsonArray=" + jsonArray);

        FastjsonFileUtil.writeJsonArrayToFile(jsonArray, jsonFilePathJSONArray, true);
        JSONArray jsonArray1 = FastjsonFileUtil.readJsonArrayFromFile(jsonFilePathJSONArray);

        System.out.println("jsonArray1=" + jsonArray1);
    }


    @Data
    static class UserInfo {
        private String name;
        private int age;
    }
}
