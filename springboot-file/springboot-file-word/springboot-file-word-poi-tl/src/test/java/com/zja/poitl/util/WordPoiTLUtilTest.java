package com.zja.poitl.util;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.*;

/**
 * @Author: zhengja
 * @Date: 2024-11-14 9:23
 */
public class WordPoiTLUtilTest {

    @Test
    public void generateWord() throws Exception {
        String templatePath = "templates/word/建设项目审查意见表模版.docx";
        String outputPath = Paths.get("target", "建设项目审查意见表模版.docx").toString();

        Map<String, Object> data = new HashMap<>();
        data.put("title", "标题");

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("commentInstitution", "环节名称" + i);
            map.put("comment", "意见内容" + i);
            map.put("commentTime", new Date());
            list.add(map);
        }

        data.put("list", list);

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure configure = Configure.builder().useSpringEL(true).bind("list", policy).build();

        WordPoiTLUtil.generateWord(outputPath, data, templatePath, configure);
    }
}
