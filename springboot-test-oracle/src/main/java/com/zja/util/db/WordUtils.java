package com.zja.util.db;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

/**
 * @author wsw
 * 2018/6/23 - 10:05
 * word工具
 */
public class WordUtils {

    /**
     * 生成 DOC 格式报告
     * @param dataMap 数据集
     * @param templateName 模板名字
     * @param path 存储位置
     */
    public static void createReport(Map dataMap, String templateName, String path) {
        try {
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("UTF-8");
            configuration.setClassForTemplateLoading(WordUtils.class, "/templates");
            Template template = configuration.getTemplate(templateName);
            File file = new File(path);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            template.process(dataMap, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
