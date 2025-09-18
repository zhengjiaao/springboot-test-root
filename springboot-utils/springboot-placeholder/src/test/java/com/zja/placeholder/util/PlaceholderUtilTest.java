package com.zja.placeholder.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-09-17 14:40
 */
public class PlaceholderUtilTest {

    @Test
    public void jsonTemplateTest() {
        String jsonTemplate = ResourcesFileUtil.readFileByUTF8("json/json_template.json");

        Map<String, String> params = new HashMap<>();
        params.put("layer", "区域图层A");
        params.put("year", "2025");
        // params.put("区域图层", "区域图层A");
        // params.put("年份", "2025");

        // 执行替换
        String replaced2 = PlaceholderUtil.replacePlaceholders(jsonTemplate, params);
        System.out.println(replaced2);
    }

    @Test
    public void jsonTemplateTest2() {
        // 定义模板
        String template = "Hello ${name}, today is ${day}";

        // 定义参数
        Map<String, String> params = new HashMap<>();
        params.put("name", "Bob");
        params.put("day", "Monday");

        // 执行替换
        String result2 = PlaceholderUtil.replacePlaceholders(template, params, "${", "}");
        System.out.println(result2);
        // 结果: "Hello Bob, today is Monday"
    }
}
