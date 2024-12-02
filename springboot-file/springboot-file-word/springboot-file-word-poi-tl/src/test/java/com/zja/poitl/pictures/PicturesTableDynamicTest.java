package com.zja.poitl.pictures;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
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
 * 动态表格、动态单元格  填充图片
 *
 * @Author: zhengja
 * @Date: 2024-11-25 19:20
 */
public class PicturesTableDynamicTest {

    public static final Map<String, Object> data = new HashMap<>();

    private static void put(String key, Object value) {
        data.put(key, value);
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }

    // 动态生成表格：一个单元格内插入单张图片、多张图片
    @Test
    public void test_1() throws IOException {
        List<RowRenderData> rows1 = new ArrayList<>();
        List<RowRenderData> rows2 = new ArrayList<>();

        // 创建包含单张图片的单元格
        CellRenderData cellWithSingleImages = new CellRenderData();
        cellWithSingleImages.addParagraph(new ParagraphRenderData().addPicture(Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create()));

        // 创建包含多张图片的单元格
        CellRenderData cellWithMultipleImages = new CellRenderData();
        cellWithMultipleImages.addParagraph(new ParagraphRenderData().addPicture(Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create()));
        cellWithMultipleImages.addParagraph(new ParagraphRenderData().addPicture(Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create()));

        // 创建行
        RowRenderData row1 = new RowRenderData();
        row1.addCell(cellWithSingleImages);

        RowRenderData row2 = new RowRenderData();
        row2.addCell(cellWithMultipleImages);

        rows1.add(row1);
        rows2.add(row2);

        // 创建表格
        TableRenderData table1 = new TableRenderData();
        table1.setRows(rows1);

        TableRenderData table2 = new TableRenderData();
        table2.setRows(rows2);

        put("table1", table1);
        put("table2", table2);

        // 渲染模板
        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_dynamic_1.docx")).render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_table_dynamic_1.docx")));
    }

    // 动态生成表格：一个单元格内插入多张图片
    @Test
    public void test_2() throws IOException {
        // 创建包含多张图片的单元格
        CellRenderData cellWithMultipleImages = new CellRenderData();
        cellWithMultipleImages.addParagraph(new ParagraphRenderData().addPicture(Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create()));
        cellWithMultipleImages.addParagraph(new ParagraphRenderData().addPicture(Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create()));

        // 创建行
        RowRenderData row = new RowRenderData();
        row.addCell(cellWithMultipleImages);

        // 设置行
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(row);
        rows.add(row);

        // 创建表格
        TableRenderData table = new TableRenderData();
        table.setRows(rows);

        put("table", table); // 动态表格

        // 渲染模板
        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_dynamic_2.docx")).render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_table_dynamic_2.docx")));
    }
}
