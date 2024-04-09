package com.zja.poitl.factory;

import com.alibaba.fastjson.JSONObject;
import com.zja.poitl.util.ResourcesFileUtil;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/04/02 10:03
 */
public class Main {
    public static void main(String[] args) {
        // 根据需要选择生成策略

        // 产品
        GenerationStrategy strategy = GenerationStrategyFactory.createGenerationStrategy("产品");
        JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("json/PlanCondition-canpin.json");
        String data = jsonObject.toJSONString();
        String template = "templates/word/canpin-规划条件提取报告模版.docx";

        // 衢州
        // GenerationStrategy strategy = GenerationStrategyFactory.createGenerationStrategy("衢州");
        // JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("json/PlanCondition-quzhou.json");
        // String data = jsonObject.toJSONString();
        // String template = "templates/word/quzhou-规划条件提取报告模版.docx";

        String wordPath = "D:\\tmp\\报告\\111.docx";
        try {
            // 使用策略生成文档
            strategy.generate(wordPath, data, template);
            System.out.println(wordPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
