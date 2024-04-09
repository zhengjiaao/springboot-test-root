package com.zja.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2024/01/19 14:32
 */
public class PoiTlTest {

    // 采用 相对路径 模版
    @Test
    public void template1_test() throws IOException {
        // 方式1：
        XWPFTemplate template = XWPFTemplate.compile("template.docx").render(
                new HashMap<String, Object>() {{
                    put("title", "Hi, poi-tl Word模板引擎");
                    put("name", "John Doe");
                    // put("age", 30);
                }});
        template.writeAndClose(Files.newOutputStream(Paths.get("output1.docx")));
    }

    // 采用 Stream 模版
    @Test
    public void template2_test() throws IOException {
        // 方式2：
        try (FileInputStream fis = new FileInputStream("template.docx");
             FileOutputStream fos = new FileOutputStream("output2.docx")) {
            XWPFTemplate template = XWPFTemplate.compile(fis).render(
                    new HashMap<String, Object>() {{
                        put("title", "Hi, poi-tl Word模板引擎");
                        put("name", "John Doe");
                        put("age", 30);
                    }}
            );

            template.write(fos);
            template.close();

            System.out.println("Word文档生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 采用 Resource资源 模版
    @Test
    public void template3_test() throws IOException {
        // 方式3：
        HashMap<String, Object> data = new HashMap<String, Object>() {{
            put("title", "Hi, poi-tl Word模板引擎");
            put("name", "John Doe");
            put("age", 30);
        }};

        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(getResourceAsStream("templates/word/template.docx")).render(data);
        xwpfTemplate.writeAndClose(Files.newOutputStream(Paths.get("output3.docx")));
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}
