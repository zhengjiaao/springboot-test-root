package com.zja.poitl.pictures;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.zja.poitl.mdoel.PicturesTableData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图片插入 表格中
 *
 * @Author: zhengja
 * @Date: 2024-11-20 10:43
 */
@Deprecated // todo 待完善
public class PicturesDynamicTableRenderPolicyTest {

    PicturesTableData data = new PicturesTableData();

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }

    @BeforeEach
    public void init() {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row 5 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        RowRenderData picturesRow = createPicturesRow("pictures/logo.png", 3);
        rows1.add(picturesRow);

        Collections.reverse(rows1); // 反正序列
        data.setTable1(rows1);

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("RowA", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("RowA", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("Row 5 Col A", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("Row 5 Col A", "Row 2 Col B", "Row 2 Col C").create());

        data.setTable2(rows2);
    }

    private RowRenderData createPicturesRow(String fileName, int addCell) {
        // 本地图片
        String localPath = getPath(fileName);
        RowRenderData row1 = new RowRenderData();
        CellRenderData cell = new CellRenderData();
        PictureRenderData pictureRenderData = Pictures.ofLocal(localPath).size(200, 200).create();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();

        paragraphRenderData.addPicture(pictureRenderData);
        cell.addParagraph(paragraphRenderData);

        for (int i = 1; i < addCell; i++) {
            row1.addCell(cell);
        }

        return row1;
    }

    @Test
    public void test_1() throws Exception {
        PicturesDynamicTableRenderPolicyV1 tableRenderPolicyV1 = PicturesDynamicTableRenderPolicyV1.builder().startRowIndex(3).build();

        Configure config = Configure.builder()
                // .bind("detail_table1", new PicturesDynamicTableRenderPolicyV1())
                // .bind("detail_table2", new PicturesDynamicTableRenderPolicyV2())
                .bind("detail_table1", tableRenderPolicyV1)
                .build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/pictures/dynamic_table_policy_1.docx"), config).render(data);
        template.writeToFile("target/out_dynamic_table_images_2.docx");
    }

    @Test
    public void test_2() throws Exception {

    }

    @Test
    public void test_3() throws Exception {

    }


}
