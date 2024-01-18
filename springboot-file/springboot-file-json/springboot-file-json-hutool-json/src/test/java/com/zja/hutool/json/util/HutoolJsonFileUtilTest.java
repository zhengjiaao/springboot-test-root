package com.zja.hutool.json.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2024/01/18 16:34
 */
public class HutoolJsonFileUtilTest {

    private static final String JSON_FILE_JSONOBJECT = "D:\\temp\\json\\JSONObject2.json";
    private static final String JSON_FILE_JSONArray = "D:\\temp\\json\\JSONArray2.json";

    @Test
    public void writeAndReadJSON1() {
        JSONObject jsonObject = JSONUtil.createObj()
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3");

        // 写json文件
        HutoolJsonFileUtil.writeJSON(jsonObject, new File(JSON_FILE_JSONOBJECT), StandardCharsets.UTF_8);

        // 读json文件
        JSON json = HutoolJsonFileUtil.readJSON(new File(JSON_FILE_JSONOBJECT), StandardCharsets.UTF_8);
        System.out.println(json);
    }

    @Test
    public void writeAndReadJSON2() {
        JSONArray jsonArray = JSONUtil.createArray();
        jsonArray.add("value1");
        jsonArray.add("value2");
        jsonArray.add("value3");

        // 写json文件
        HutoolJsonFileUtil.writeJSON(jsonArray, new File(JSON_FILE_JSONArray), StandardCharsets.UTF_8);

        // 读json文件
        JSON json = HutoolJsonFileUtil.readJSON(new File(JSON_FILE_JSONArray), StandardCharsets.UTF_8);
        System.out.println(json);
    }
}
