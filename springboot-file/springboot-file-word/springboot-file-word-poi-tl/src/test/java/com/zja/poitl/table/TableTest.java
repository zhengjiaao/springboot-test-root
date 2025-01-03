package com.zja.poitl.table;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-11-19 14:36
 */
public class TableTest {

    // 循环表格（直接创建完整表格）
    @Test
    public void test_0_1() throws IOException {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Header A", "Header B", "Header C").create());
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());

        // 创建表格对象
        TableRenderData table1 = new TableRenderData();
        table1.setRows(rows1);
        TableRenderData table2 = new TableRenderData();
        table2.setRows(rows2);

        Map<String, Object> data = new HashMap<>();
        data.put("tableName1", "table1");
        data.put("tableName2", "table2");
        data.put("table1", table1);
        data.put("table2", table2);

        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(data);
        dataList.add(data);

        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_0_1.docx")).render(new HashMap<String, Object>() {
            {
                put("dataList", dataList);
            }
        });
        template.writeToFile("target/out_Table_Test_0_1.docx");
    }

    // 动态表格：存在表头，需填表数据
    @Test
    public void test_0_2() throws Exception {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());

        // 创建表格对象
        TableRenderData table1 = new TableRenderData();
        table1.setRows(rows1);

        TableRenderData table2 = new TableRenderData();
        table2.setRows(rows2);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("detail_table_1", table1);
        data.put("detail_table_2", table2);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_0_2.docx")).render(data);

        // 处理表格数据
        List<XWPFTable> tables = template.getXWPFDocument().getTables();
        renderData(tables.get(0), rows1, 1, -1, -1);
        renderData(tables.get(1), rows2, 1, -1, -1);

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_0_2.docx");
    }

    private void renderData(XWPFTable table, List<RowRenderData> data, int startRow, int mergeStartCol, int mergeEndCol) throws Exception {
        if (table == null || data == null) {
            throw new IllegalArgumentException("table or data cannot be null");
        }

        table.removeRow(startRow);
        for (int i = 0; i < data.size(); i++) {
            XWPFTableRow newRow = table.insertNewTableRow(startRow);
            if (newRow == null) {
                throw new IllegalStateException("Failed to create new row in table");
            }
            for (int j = 0; j < 3; j++) newRow.createCell();

            // 合并单元格
            if (mergeStartCol >= 0 && mergeEndCol >= 0) {
                TableTools.mergeCellsHorizonal(table, startRow, mergeStartCol, mergeEndCol);
            }

            // 渲染数据
            TableRenderPolicy.Helper.renderRow(newRow, data.get(i));
        }
    }


    // 模版单个表格
    @Test
    public void test_1() throws IOException {
        // 创建表格数据
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());

        // 创建表格对象
        TableRenderData table = new TableRenderData();
        table.setRows(rows);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_1.docx")).render(data);
        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_1.docx");
    }

    // 模版多个表格
    @Test
    public void test_2() throws IOException {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Header A", "Header B", "Header C").create());
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());

        // 创建表格对象
        TableRenderData table1 = new TableRenderData();
        table1.setRows(rows1);

        TableRenderData table2 = new TableRenderData();
        table2.setRows(rows2);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table1", table1);
        data.put("table2", table2);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_2.docx")).render(data);
        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_2.docx");
    }

    // 动态单个表格(合并行+合并列)
    @Test
    public void test_3() throws IOException {
        // 创建表格数据
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows.add(Rows.of("Row 3 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());
        rows.add(Rows.of("Row 4 Col 1", "Row 4 Col 2", "Row 4 Col 3").create());
        rows.add(Rows.of("Row 5 Col 1", "Row 5 Col 2", "Row 5 Col 3").create());

        // 创建表格对象
        TableRenderData table = new TableRenderData();
        table.setRows(rows);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_1.docx")).render(data);

        // 获取模板中的第一个表格
        XWPFTable xwpfTable = template.getXWPFDocument().getTables().get(0);

        // 合并单元格
        TableTools.mergeCellsHorizonal(xwpfTable, 1, 0, 2); // 合并第1行的第0到第2列
        TableTools.mergeCellsVertically(xwpfTable, 0, 2, 5); // 合并第0列的第2到第5行

        // 获取合并后的单元格并居中内容
        XWPFTableCell cell1 = xwpfTable.getRow(1).getCell(0); // 第1行第0列
        for (XWPFParagraph paragraph : cell1.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
            // paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
        }

        XWPFTableCell cell2 = xwpfTable.getRow(2).getCell(0); // 第2行第0列
        for (XWPFParagraph paragraph : cell2.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
            // paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中，todo 未生效
        }
        cell2.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_3.docx");
    }

    // 动态多个表格(合并行+合并列)
    @Test
    public void test_4() throws IOException {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row 3 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());
        rows1.add(Rows.of("Row 4 Col 1", "Row 4 Col 2", "Row 4 Col 3").create());
        rows1.add(Rows.of("Row 5 Col 1", "Row 5 Col 2", "Row 5 Col 3").create());

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Header A", "Header B", "Header C").create());
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("Row 3 Col A", "Row 3 Col B", "Row 3 Col C").create());
        rows2.add(Rows.of("Row 4 Col A", "Row 4 Col B", "Row 4 Col C").create());
        rows2.add(Rows.of("Row 5 Col A", "Row 5 Col B", "Row 5 Col C").create());

        // 创建表格对象
        TableRenderData table1 = new TableRenderData();
        table1.setRows(rows1);

        TableRenderData table2 = new TableRenderData();
        table2.setRows(rows2);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table1", table1);
        data.put("table2", table2);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_2.docx")).render(data);

        // 获取模板中的第一个表格
        XWPFTable xwpfTable1 = template.getXWPFDocument().getTables().get(0);
        XWPFTable xwpfTable2 = template.getXWPFDocument().getTables().get(1);

        // 合并单元格
        TableTools.mergeCellsHorizonal(xwpfTable1, 1, 0, 2); // 合并第一个表格的第一行的第0到第2列
        TableTools.mergeCellsVertically(xwpfTable1, 0, 2, 5); // 合并第一个表格的第0列的第2到第5行

        TableTools.mergeCellsHorizonal(xwpfTable2, 1, 0, 2); // 合并第二个表格的第一行的第0到第2列
        TableTools.mergeCellsVertically(xwpfTable2, 0, 2, 5); // 合并第二个表格的第0列的第2到第5行

        // 获取模板中的第一个表格
        XWPFTable xwpfTable = template.getXWPFDocument().getTables().get(0);

        // 获取合并后的单元格并居中内容
        XWPFTableCell cell1 = xwpfTable.getRow(1).getCell(0); // 第1行第0列
        for (XWPFParagraph paragraph : cell1.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
            // paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
        }

        XWPFTableCell cell2 = xwpfTable.getRow(2).getCell(0); // 第2行第0列
        for (XWPFParagraph paragraph : cell2.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
            // paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中，todo 未生效
        }
        cell2.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_4.docx");
    }

    // 动态单个表格(合并相同值的行)
    @Test
    public void test_5() throws IOException {
        // 创建表格数据
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());
        rows.add(Rows.of("Row 4 Col 1", "Row 4 Col 2", "Row 4 Col 3").create());

        // 创建表格对象
        TableRenderData table = new TableRenderData();
        table.setRows(rows);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_1.docx")).render(data);

        // 获取模板中的第一个表格
        XWPFTable xwpfTable = template.getXWPFDocument().getTables().get(0);

        // 合并指定列的单元格
        mergeCells(xwpfTable, 0); // 合并第一列

        // 设置合并后的单元格内容居中
        centerMergedCells(xwpfTable, 0); // 第一列

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_5.docx");
    }

    // 合并单元格-合并指定列具有相同值的行
    private void mergeCells(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        if (rowCount <= 1) {
            return; // 如果只有一行，不需要合并
        }

        String previousValue = null;
        boolean isMergeStarted = false;

        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(columnIndex);

            String currentValue = cell.getText();

            if (i == 0 || !currentValue.equals(previousValue)) {
                // 新的值，开始新的合并
                if (isMergeStarted) {
                    // 结束上一次的合并
                    isMergeStarted = false;
                }
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                previousValue = currentValue;
                isMergeStarted = true;
            } else {
                // 相同的值，继续合并
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // 设置合并后的单元格内容 垂直居中
    private void centerMergedCells(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        for (int i = 0; i < rowCount; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(columnIndex);
            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
                paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
            }
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直
        }
    }

    // 动态单个表格(合并相同值的行)
    @Test
    public void test_6() throws IOException {
        // 创建表格数据
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());
        rows.add(Rows.of("Row 4 Col 1", "Row 4 Col 2", "Row 4 Col 3").create());

        // 创建表格对象
        TableRenderData table = new TableRenderData();
        table.setRows(rows);

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/Table_Test_1.docx")).render(data);

        // 获取模板中的第一个表格
        XWPFTable xwpfTable = template.getXWPFDocument().getTables().get(0);

        // 合并指定列的单元格并居中内容
        mergeAndCenterCells(xwpfTable, 0); // 合并第一列

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_6.docx");
    }

    // 合并单元格并垂直居中内容
    private void mergeAndCenterCells(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        if (rowCount <= 1) {
            return; // 如果只有一行，不需要合并
        }

        String previousValue = null;
        boolean isMergeStarted = false;

        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(columnIndex);

            String currentValue = cell.getText();

            if (i == 0 || !currentValue.equals(previousValue)) {
                // 新的值，开始新的合并
                if (isMergeStarted) {
                    // 结束上一次的合并
                    isMergeStarted = false;
                }
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                previousValue = currentValue;
                isMergeStarted = true;
            } else {
                // 相同的值，继续合并
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }

            // 设置单元格内容居中
            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
                paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
            }
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 单元格垂直居中
        }
    }


    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
