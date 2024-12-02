package com.zja.poitl.base;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-27 9:25
 */
public class TableToolsTest {

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    @Test
    public void test_1() throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/base/TableTools_mergeCells_1.docx"));
        List<XWPFTable> tables = template.getXWPFDocument().getTables();
        for (XWPFTable table : tables) {
            System.out.println("合并前行数，table.getRows().size() = " + table.getRows().size()); // 行数 8

            // 合并单元格 行
            TableTools.mergeCellsHorizonal(table,  0, 1, 2);
            // 合并单元格 列
            TableTools.mergeCellsVertically(table, 0, 1, 2);

            System.out.println("合并后行数，table.getRows().size() = " + table.getRows().size()); // 行数 8

            // 渲染数据
            // TableRenderPolicy.Helper.renderRow(newRow, data.get(i));

            // System.out.println("渲染后行数，table.getRows().size() = " + table.getRows().size()); // 行数
        }

        // 输出生成的文档
        template.writeToFile("target/out_TableTools_mergeCells_1.docx");
    }


    @Test
    public void test_2() throws IOException {

    }

    @Test
    public void test_3() throws IOException {

    }

    @Test
    public void test_4() throws IOException {

    }

    // 合并行单元格
    private void mergeCellsHorizontally(XWPFTable table, int rowIndex, int startColIndex, int endColIndex) {
        for (int colIndex = startColIndex; colIndex <= endColIndex; colIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
            if (cell != null) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // 合并列单元格
    private void mergeCellsVertically(XWPFTable table, int colIndex, int startRowIndex, int endRowIndex) {
        for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
            if (cell != null) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}
