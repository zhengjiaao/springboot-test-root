package com.zja.poitl;

import com.deepoove.poi.XWPFTemplate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author: zhengja
 * @since: 2024/01/19 14:32
 */
public class PoiTlTest {

    // 添加模板变量
    @Test
    public void test() throws IOException {
        // 方式1：
        XWPFTemplate template = XWPFTemplate.compile("template.docx").render(
                new HashMap<String, Object>() {{
                    put("title", "Hi, poi-tl Word模板引擎");
                    put("name", "John Doe");
                    put("age", 30);
                }});
        template.writeAndClose(Files.newOutputStream(Paths.get("output.docx")));

        // 方式2：
        /*try (FileInputStream fis = new FileInputStream("template.docx");
             FileOutputStream fos = new FileOutputStream("output.docx")) {
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
        }*/
    }

}
