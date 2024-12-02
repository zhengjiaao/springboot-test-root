package com.zja.poitl.pictures;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.List;

/**
 * 合并单元格：合并列，存储图片，并且设置单元格内容居中
 *
 * @Author: zhengja
 * @Date: 2024-11-19 20:33
 */
public class PicturesDynamicTableRenderPolicyV2 extends DynamicTableRenderPolicy {

    private static final int TABLE_START_ROW = 1; // 假设数据从第1行开始

    public void render(XWPFTable table, Object data) throws Exception {
        if (!verifyDataLegality(data)) return;

        List<RowRenderData> tableData = (List<RowRenderData>) data;
        // 表数据
        renderData(table, tableData, TABLE_START_ROW, 3); // 不需要合并单元格
        // renderData(table, tableData, TABLE_START_ROW, 0, 1); // 合并单元格

        // 表图片数据
        renderImagesData(table, tableData, TABLE_START_ROW, 0, 3);

        // 合并单元格
        // mergeAndCenterCells(table, 0);
    }

    private boolean verifyDataLegality(Object data) {
        if (data == null) {
            return false;
        }
        if (!(data instanceof List)) {
            return false;
        }
        List<?> dataList = (List<?>) data;
        if (dataList.isEmpty()) {
            return false;
        }

        return dataList.get(0) instanceof RowRenderData;
    }

    // 渲染数据
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

    // todo 待测试
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
}
