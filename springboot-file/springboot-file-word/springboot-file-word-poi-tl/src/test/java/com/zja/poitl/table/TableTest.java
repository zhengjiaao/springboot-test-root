package com.zja.poitl.table;

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import com.zja.poitl.util.ContextPathUtil;
import com.zja.poitl.util.ResourcesFileUtil;
import com.zja.poitl.util.WordPoiTLUtil;
import com.zja.poitl.util.WordPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

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
        rows1.add(Rows.of("Row 3 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("Row 3 Col A", "Row 3 Col B", "Row 3 Col C").create());

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


    // 动态单个表格(合并相同值的行)
    @Test
    public void test7() throws IOException {
        // 创建表格数据
        List<RowRenderData> rows = new ArrayList<>();
        rows.add(Rows.of("Header 1", "Header 2", "Header 3").create());
        rows.add(Rows.of("Row 1 Col 1", "1.5", "Row 1 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "1.2", "Row 2 Col 3").create());
        rows.add(Rows.of("Row 2 Col 1", "1.5", "Row 3 Col 3").create());
        rows.add(Rows.of("Row 3 Col 1", "1.5", "Row 4 Col 3").create());
        rows.add(Rows.of("Row 3 Col 1", "1.3", "Row 5 Col 3").create());
        rows.add(Rows.of("Row 4 Col 1", "2", "Row 6 Col 3").create());

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
        List<String> mergedRows = mergeCellsV2(xwpfTable, 0); // 合并第一列

        // 合并其他列，保持与第0列合并行一致
        // mergeColumnsBasedOnMergedRows(xwpfTable, mergedRows, 1); // 合并第二列
        mergeColumnsBasedOnMergedRowsSetNewValue(xwpfTable, mergedRows, 2); // 合并第3列，并且读取计算第2列值，填入第3列

        // 设置合并后的单元格内容居中
        centerMergedCells(xwpfTable, 0); // 第一列
        centerMergedCells(xwpfTable, 1); // 第二列
        centerMergedCells(xwpfTable, 2); // 第三列

        // 输出合并信息
        System.out.println("Merged rows: " + mergedRows);
        // Merged rows: [1-1, 2-2, 3-4, 5-5]

        // 设置合并后的单元格新值
        setCellNewValue(xwpfTable, 1, 0, "New Value 1"); // 设置第1行第0列的新值
        setCellNewValue(xwpfTable, 2, 0, "New Value 2"); // 设置第2行第0列的新值

        // 输出生成的文档
        template.writeToFile("target/out_Table_Test_7.docx");
    }

    // 合并单元格-合并指定列具有相同值的行，并返回合并后的行信息
    private List<String> mergeCellsV2(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        if (rowCount <= 1) {
            return new ArrayList<>(); // 如果只有一行，不需要合并
        }

        String previousValue = null;
        boolean isMergeStarted = false;
        List<String> mergedRows = new ArrayList<>();
        int startRow = 0;

        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(columnIndex);

            String currentValue = cell.getText();

            if (i == 0 || !currentValue.equals(previousValue)) {
                // 新的值，开始新的合并
                if (isMergeStarted) {
                    // 结束上一次的合并
                    mergedRows.add((startRow + 1) + "-" + i);
                    isMergeStarted = false;
                }
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                previousValue = currentValue;
                startRow = i;
                isMergeStarted = true;
            } else {
                // 相同的值，继续合并
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }

        // 处理最后一组合并
        if (isMergeStarted) {
            mergedRows.add((startRow + 1) + "-" + rowCount);
        }

        return mergedRows;
    }

    // 根据合并行信息合并指定列，并清空值
    /*private void mergeColumnsBasedOnMergedRows(XWPFTable table, List<String> mergedRows, int columnIndex) {
        for (String mergedRow : mergedRows) {
            // [1-1, 2-2, 3-4, 5-5]
            String[] range = mergedRow.split("-");
            int startRow = Integer.parseInt(range[0]);
            int endRow = Integer.parseInt(range[1]);
            System.out.println("startRow:" + startRow + ",endRow:" + endRow);

            if (startRow != endRow) {
                startRow = startRow - 1;
                endRow = endRow - 1;
                for (int i = startRow; i <= endRow; i++) {
                    XWPFTableRow row1 = table.getRow(i);
                    XWPFTableCell cell = row1.getCell(columnIndex);
                    if (cell == null) {
                        cell = row1.createCell();
                    }
                    // 设置合并标记
                    cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);

                    // 清理单元格文本内容
                    clearCellText(cell);
                }
            }
        }
    }*/

    // 根据合并行信息合并指定列，并设置新值
    private void mergeColumnsBasedOnMergedRowsSetNewValue(XWPFTable table, List<String> mergedRows, int columnIndex) {
        int lastMergedEndRow = -1;
        for (String mergedRow : mergedRows) {
            // [1-1, 2-2, 3-5, 6-6]
            String[] range = mergedRow.split("-");
            int startRow = Integer.parseInt(range[0]) - 1;
            int endRow = Integer.parseInt(range[1]) - 1;

            if (startRow != endRow) {
                // 如果当前合并区域与上一个合并区域相邻，则合并
                if (startRow <= lastMergedEndRow + 1) {
                    startRow = lastMergedEndRow + 1;
                }
                for (int i = startRow; i <= endRow; i++) {
                    XWPFTableRow row1 = table.getRow(i);
                    XWPFTableCell cell = row1.getCell(columnIndex);
                    if (cell == null) {
                        cell = row1.createCell();
                    }
                    // 设置合并标记
                    if (i > startRow) {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
                    } else {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                    }

                    // 清理单元格文本内容
                    clearCellText(cell);

                    // 设置前一列的值
                    int prevColumnIndex = columnIndex - 1;
                    cell.setText(getCellValue(table, prevColumnIndex, startRow, endRow));
                }
                lastMergedEndRow = endRow;
            }
        }
    }


    // 获取指定列在指定行范围内的文本内容, 并返回合并后的文本内容
 /*   private String getCellValue(XWPFTable table, int columnIndex, int startRow, int endRow) {
        StringBuilder textValue = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            XWPFTableRow row = table.getRow(i);
            if (row != null) {
                XWPFTableCell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell();
                }
                textValue.append(cell.getText());
            }
        }
        return textValue.toString();
    }*/

    // 获取指定列在指定行范围内的文本内容，并将数字相加
    private String getCellValue(XWPFTable table, int columnIndex, int startRow, int endRow) {
        double sum = 0.0;
        boolean hasValidValue = false;

        for (int i = startRow; i <= endRow; i++) {
            XWPFTableRow row = table.getRow(i);
            if (row != null) {
                XWPFTableCell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell();
                }
                String text = cell.getText();
                if (StringUtils.isNotEmpty(text)) {
                    try {
                        sum += Double.parseDouble(text);
                        hasValidValue = true;
                    } catch (NumberFormatException e) {
                        // 忽略无法解析的数字
                        System.err.println("无法解析数字: " + text);
                    }
                }
            }
        }

        return hasValidValue ? String.valueOf(sum) : "";
    }

    // 设置指定单元格的新值
    private void setCellNewValue(XWPFTable table, int rowIndex, int columnIndex, String newValue) {
        XWPFTableRow row = table.getRow(rowIndex);
        if (row != null) {
            XWPFTableCell cell = row.getCell(columnIndex);
            if (cell == null) {
                cell = row.createCell();
            }
            // 清空单元格文本内容
            clearCellText(cell);
            cell.setText(newValue);
        }
    }

    // 清空单元格文本内容
    private void clearCellText(XWPFTableCell cell) {
        // 清空所有段落的文本内容
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            // 删除段落中的所有运行（runs）
            for (XWPFRun run : paragraph.getRuns()) {
                run.setText("", 0);
            }
        }
    }

    // 动态单个表格(合并相同值的行)
    @Test
    public void test8() throws IOException {

        // 构建报告数据
        JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("data/json/合规性检测报告-内审模版.json");
        Map<String, Object> data = jsonObject.getInnerMap();

        String templatePath = "templates/word/table/合规性检测报告-内审模版.docx";
        String wordPath = ContextPathUtil.getTempFilePath("合规性检测报告-内审.docx");

        // 加载模板文件
        Configure config = Configure.builder()
                .useSpringEL(true)
                .bind("theStatusQuoList", new LoopRowTableRenderPolicy()).build();
        XWPFTemplate template = XWPFTemplate
                .compile(WordPoiTLUtil.getInputStream(templatePath), config)
                .render(data);

        // 获取模板中的第一个表格
        XWPFTable xwpfTable = template.getXWPFDocument().getTables().get(0);

        // 处理表格，合并列，计算数据
        List<String> mergeCells = WordPoiUtil.mergeCells(xwpfTable, 0); // 合并第1列
        WordPoiUtil.mergeColumnsBasedOnMergedRowsSetNewValue(xwpfTable, mergeCells, 4); // 合并第5列，并且读取计算第4列值，填入第5列
        WordPoiUtil.mergeColumnsBasedOnMergedRows(xwpfTable, mergeCells, 5); // 合并第6列

        // 处理表格，合并列，计算数据
        List<String> mergeCells2 = WordPoiUtil.mergeCells(xwpfTable, 1); // 合并第2列
        List<String> mergeCells3 = WordPoiUtil.mergeCells(xwpfTable, 2); // 合并第3列
        WordPoiUtil.mergeColumnsBasedOnMergedRowsSetNewValueV2(xwpfTable, mergeCells2, 3); // 合并第4列，并且读取计算第4列值，填入第4列

        // 输出生成的文档
        template.writeToFile(wordPath);
    }
}
