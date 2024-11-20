package com.zja.poitl.table;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-19 20:33
 */
public class DetailTablePolicy3 extends DynamicTableRenderPolicy {

    private static final int TABLE_START_ROW = 1; // 假设数据从第1行开始

    public void render(XWPFTable table, Object data) throws Exception {
        if (!verifyDataLegality(data)) return;

        List<RowRenderData> tableData = (List<RowRenderData>) data;
        // 表数据
        renderData(table, tableData, TABLE_START_ROW, -1, -1); // 不需要合并单元格
        // renderData(table, tableData, TABLE_START_ROW, 0, 1); // 合并单元格

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

            // 渲染数据
            TableRenderPolicy.Helper.renderRow(newRow, data.get(i));

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
}
