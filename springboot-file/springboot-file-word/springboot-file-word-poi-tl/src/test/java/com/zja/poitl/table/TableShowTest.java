package com.zja.poitl.table;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Pictures;
import com.zja.poitl.util.ResourcesFileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-11-26 16:20
 */
public class TableShowTest {

    public static final Map<String, Object> data = new HashMap<>();

    private static void put(String key, Object value) {
        data.put(key, value);
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }

    @Test
    public void test_1() throws IOException {
        // put("tableShow", true);
        put("tableShow", true);
        put("tableShow2", false);
        put("user", "李四");

        XWPFTemplate template = XWPFTemplate
                .compile(ResourcesFileUtil.getResourceAsStream("templates/word/table/Table_Show.docx")).render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_Table_Show.docx")));
    }

}
