package com.zja.poitl.base;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.TableRenderData;
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
 * @Date: 2024-11-26 20:27
 */
@Deprecated
public class XWPFTableTest {

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    // 动态生成表格，处理表格操作
    @Test
    public void test_1() throws Exception {
        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row 3 Col 1", "Row 3 Col 2", "Row 3 Col 3").create());

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
        // Map<String, Object> data = new HashMap<>();
        // data.put("detail_table_1", table1);
        // data.put("detail_table_2", table2);

        // 加载模板文件
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/XWPFTable_1.docx"));//.render(data);

        // 处理表格数据
        List<XWPFTable> tables = template.getXWPFDocument().getTables();
        System.out.println("表格数量：" + tables.size());

        // 渲染前，未写入前的行数进行打印(模版中的表格行数)
        System.out.println("表格1行数：" + tables.get(0).getRows().size());
        System.out.println("表格2行数：" + tables.get(1).getRows().size());

        // 写入数据
        renderData(tables.get(0), rows1, 1, -1, -1);
        renderData(tables.get(1), rows2, 1, -1, -1);

        // 渲染后，写入后的行数进行打印(非模版)
        System.out.println("表格1行数：" + tables.get(0).getRows().size());
        System.out.println("表格2行数：" + tables.get(1).getRows().size());

        // 输出生成的文档
        template.writeToFile("target/out_XWPFTable_1.docx");
    }

    /**
     * 渲染数据
     *
     * @param table         表格对象
     * @param data          渲染数据
     * @param startRow      插入行数
     * @param mergeStartCol 合并开始列
     * @param mergeEndCol   合并结束列
     */
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


    @Test
    public void test_2() throws IOException {

    }

    private void renderData(XWPFTable table, List<RowRenderData> data, int startRow, int createRowCell) throws Exception {
        if (table == null || data == null) {
            throw new IllegalArgumentException("table or data cannot be null");
        }

        table.removeRow(startRow);
        for (int i = 0; i < data.size(); i++) {
            XWPFTableRow newRow = table.insertNewTableRow(startRow);
            if (newRow == null) {
                throw new IllegalStateException("Failed to create new row in table");
            }

            // 创建3个单元格
            for (int j = 0; j < createRowCell; j++) newRow.createCell();

            // 获取行数据
            RowRenderData rowRenderData = data.get(i);

            // 渲染数据
            TableRenderPolicy.Helper.renderRow(newRow, rowRenderData);

            // 设置单元格内容居中
            for (int j = 0; j < newRow.getTableCells().size(); j++) {
                XWPFTableCell cell = newRow.getCell(j);
                cell.getParagraphs().forEach(p -> p.setAlignment(ParagraphAlignment.CENTER));
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }
        }

       /* // XWPFTable table 最后一行创建一个单元格
        int lastRowPos = startRow + data.size();
        XWPFTableRow lastRow = table.getRow(lastRowPos);
        if (lastRow != null) {
            table.removeRow(lastRowPos);
        }
        XWPFTableRow newRow = table.insertNewTableRow(lastRowPos);
        for (int j = 0; j < 3; j++) newRow.createCell();
        TableTools.mergeCellsHorizonal(table, lastRowPos, 0, 3);
        int size = data.size();
        RowRenderData rowRenderData = data.get(size - 1);
        TableRenderPolicy.Helper.renderRow(newRow, rowRenderData);*/
    }

    @Test
    public void test_3() throws IOException {

    }

    private void mergeRowAndCenterCells(XWPFTable table, int rowIndex) {
        int rowCount = table.getRows().size();
        if (rowCount <= 1) {
            return; // 如果只有一行，不需要合并
        }
        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(0);
            if (i == rowIndex) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else if (i > rowIndex) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }
        }
    }


    // 合并列单元格
    private void mergeColumnAndCenterCells(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        if (rowCount <= 1) {
            return; // 如果只有一列，不需要合并
        }
        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(columnIndex);
            if (i == 0) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            }
        }
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }
        }
        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(columnIndex);
            cell.getParagraphs().forEach(p -> p.setAlignment(ParagraphAlignment.CENTER));
        }
    }

    private void renderImagesData(XWPFTable table, List<RowRenderData> data, int startRow, int mergeStartCol, int mergeEndCol) throws Exception {
        if (table == null || data == null) {
            throw new IllegalArgumentException("table or data cannot be null");
        }

        table.removeRow(startRow);
        for (int i = 0; i < data.size(); i++) {
            XWPFTableRow newRow = table.insertNewTableRow(startRow);
            if (newRow == null) {
                throw new IllegalStateException("Failed to create new row in table");
            }

            // 创建3个单元格
            for (int j = 0; j < 3; j++) newRow.createCell();

            // 获取行数据
            RowRenderData rowRenderData = data.get(i);

            // 渲染数据
            TableRenderPolicy.Helper.renderRow(newRow, rowRenderData);

            // 合并单元格
            if (mergeStartCol >= 0 && mergeEndCol >= 0) {
                TableTools.mergeCellsHorizonal(table, startRow, mergeStartCol, mergeEndCol);
            }

            // 设置单元格内容居中
            for (int j = 0; j < newRow.getTableCells().size(); j++) {
                XWPFTableCell cell = newRow.getCell(j);
                cell.getParagraphs().forEach(p -> p.setAlignment(ParagraphAlignment.CENTER));
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }
        }
    }

    // 合并单元格

    // 合并行单元格

    // 合并列单元格

    // 合并整个表格的单元格

    // 设置某个单元格的内容居中(水平居中+垂直居中)
    private void setCellCenter(XWPFTableCell cell) {
        // 设置单元格内容居中
        cell.getParagraphs().forEach(p -> {
            p.setAlignment(ParagraphAlignment.CENTER); // 内容水平居中
            p.setVerticalAlignment(TextAlignment.CENTER); // 内容垂直居中
        });
        // 设置单元格垂直居中(图片等)
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    // 设置某行的内容居中(含垂直居中)
    private void setRowCenter(XWPFTableRow row) {
        for (XWPFTableCell cell : row.getTableCells()) {
            setCellCenter(cell);
        }
    }

    // 设置某列的内容居中(含垂直居中)
    private void setColumnCenter(XWPFTable table, int columnIndex) {
        for (XWPFTableRow row : table.getRows()) {
            XWPFTableCell cell = row.getCell(columnIndex);
            setCellCenter(cell);
        }
    }

    // 设置整个表格的内容居中(含垂直居中)
    private void setTableCenter(XWPFTable table) {
        for (XWPFTableRow row : table.getRows()) {
            setRowCenter(row);
        }
    }

}
