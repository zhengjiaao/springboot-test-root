package com.zja.poitl.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-01-22 9:33
 */
@Slf4j
public class WordPoiUtil {

    private WordPoiUtil() {

    }

    // 合并单元格-合并指定列具有临近相同值的行，并返回合并后的行信息
    public static List<String> mergeCells(XWPFTable table, int columnIndex) {
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

            // 检查单元格是否为 null
            if (cell == null) {
                log.warn("警告: 第 {} 行, columnIndex {} 的单元格为空", i, columnIndex);
                continue;
            }
            // System.out.println("当前行数" + i + " ,columnIndex" + columnIndex);

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

    // 合并列
    public static void mergeColumnsBasedOnMergedRows(XWPFTable table, List<String> mergedRows, int columnIndex) {
        for (String mergedRow : mergedRows) {
            // [1-1, 2-2, 3-4, 5-5]
            String[] range = mergedRow.split("-");
            int startRow = Integer.parseInt(range[0]);
            int endRow = Integer.parseInt(range[1]);

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
    }

    // 合并指定列，并设置前一列计算的新值
    public static void mergeColumnsBasedOnMergedRowsSetNewValue(XWPFTable table, List<String> mergedRows, int columnIndex) {
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

                    // 设置前一列的值
                    int prevColumnIndex = columnIndex - 1;
                    String cellValue = getCellValue(table, prevColumnIndex, startRow, endRow);

                    // 清理单元格文本内容
                    clearCellText(cell);

                    cell.setText(cellValue);
                }
                lastMergedEndRow = endRow;
            } else {
                // 若是同一行，则直接设置前一列的值，需要排除掉首行，最后两行
                if (startRow == 0 || startRow >= table.getRows().size() - 2) {
                    continue;
                }
                XWPFTableRow row1 = table.getRow(startRow);
                XWPFTableCell cell = row1.getCell(columnIndex);
                if (cell == null) {
                    cell = row1.createCell();
                }

                // 设置前一列的值
                String cellValue = getCellValue(table, columnIndex - 1, startRow, endRow);

                // 清理单元格文本内容
                clearCellText(cell);

                cell.setText(cellValue);
            }
        }
    }

    // 合并指定列，并设置当前列计算的新值
    public static void mergeColumnsBasedOnMergedRowsSetNewValueV2(XWPFTable table, List<String> mergedRows, int columnIndex) {
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
                        continue;
                        // cell = row1.createCell();
                    }
                    // 设置合并标记
                    if (i > startRow) {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
                        // row1.setHeight((short) -1);     // 设置行高为-1，表示自动，适配office
                        row1.setHeight((short) 1);     // 设置行高为1，表示自动，适配wps
                    } else {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                    }

                    // 设置当前列的值
                    String cellValue = getCellValue(table, columnIndex, startRow, endRow);

                    // 清理单元格文本内容
                    clearCellText(cell);

                    // 设置当前列的值
                    cell.setText(cellValue);
                }
                lastMergedEndRow = endRow;
            } else {
                // 若是同一行，则直接设置当前列的值，需要排除掉首行，最后两行
                if (startRow == 0 || startRow >= table.getRows().size() - 2) {
                    continue;
                }

                XWPFTableRow row1 = table.getRow(startRow);
                XWPFTableCell cell = row1.getCell(columnIndex);
                if (cell == null) {
                    cell = row1.createCell();
                }

                // 设置当前列的值
                String cellValue = getCellValue(table, columnIndex, startRow, endRow);

                // 清理单元格文本内容
                clearCellText(cell);

                cell.setText(cellValue);
            }
        }
    }

    // 获取指定列在指定行范围内的文本内容，并将数字相加
    private static String getCellValue(XWPFTable table, int columnIndex, int startRow, int endRow) {
        BigDecimal sum = BigDecimal.ZERO;
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
                        BigDecimal value = new BigDecimal(text);
                        sum = sum.add(value);
                        hasValidValue = true;
                    } catch (NumberFormatException e) {
                        // 忽略无法解析的数字
                        System.err.println("无法解析数字: " + text);
                    }
                }
            }
        }

        // 格式化输出，保留小数点后四位，并且逢五进一
        return hasValidValue ? sum.setScale(4, RoundingMode.HALF_UP).toString() : "";
    }

    // 清空单元格文本内容
    private static void clearCellText(XWPFTableCell cell) {
        // 清空所有段落的文本内容
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            // 删除段落中的所有运行（runs）
            for (XWPFRun run : paragraph.getRuns()) {
                run.setText("", 0);
            }
        }
    }
}
