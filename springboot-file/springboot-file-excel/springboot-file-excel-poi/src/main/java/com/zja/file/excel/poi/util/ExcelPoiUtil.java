package com.zja.file.excel.poi.util;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-11-14 22:00
 */

public class ExcelPoiUtil {

    /**
     * 从Excel文件中读取数据到List<Map<String, Object>>中
     *
     * @param filePath Excel文件路径
     * @return 数据列表
     * @throws IOException 如果读取文件时发生错误
     */
    public static List<Map<String, Object>> readExcel(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        for (Row row : sheet) {
            Map<String, Object> rowData = new java.util.HashMap<>();
            for (Cell cell : row) {
                String columnName = "Column" + cell.getColumnIndex();
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.put(columnName, cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.put(columnName, cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowData.put(columnName, cell.getBooleanCellValue());
                        break;
                    default:
                        rowData.put(columnName, cell.toString());
                }
            }
            data.add(rowData);
        }
        workbook.close();
        fis.close();
        return data;
    }

    /**
     * 将数据写入到Excel文件中
     *
     * @param templatePath 模板文件路径
     * @param outputPath   输出文件路径
     * @param data         要写入的数据
     * @throws IOException 如果写入文件时发生错误
     */
    public static void writeExcel(String templatePath, String outputPath, List<Map<String, Object>> data) throws IOException {
        try (FileInputStream fis = new FileInputStream(templatePath);
             FileOutputStream fos = new FileOutputStream(outputPath)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum() + 1;
            for (Map<String, Object> rowMap : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (Map.Entry<String, Object> entry : rowMap.entrySet()) {
                    Cell cell = row.createCell(colNum++);
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
            workbook.write(fos);
        }
    }

    /**
     * 获取工作簿对象
     *
     * @param filePath Excel文件路径
     * @return 工作簿对象
     * @throws IOException 如果读取文件时发生错误
     */
    public static Workbook getWorkbook(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        fis.close();
        return workbook;
    }

    /**
     * 获取工作表对象
     *
     * @param workbook 工作簿对象
     * @param sheetName 工作表名称
     * @return 工作表对象
     */
    public static Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    /**
     * 设置单元格值
     *
     * @param sheet 工作表对象
     * @param row   行号（从0开始）
     * @param col   列号（从0开始）
     * @param value 单元格值
     */
    public static void setCellValue(Sheet sheet, int row, int col, Object value) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 获取单元格值
     *
     * @param sheet 工作表对象
     * @param row   行号（从0开始）
     * @param col   列号（从0开始）
     * @return 单元格值
     */
    public static Object getCellValue(Sheet sheet, int row, int col) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            return null;
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return cell.toString();
        }
    }

    /**
     * 添加工作表
     *
     * @param workbook 工作簿对象
     * @param sheetName 工作表名称
     * @return 新添加的工作表对象
     */
    public static Sheet addSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * 删除工作表
     *
     * @param workbook 工作簿对象
     * @param sheetName 工作表名称
     */
    public static void removeSheet(Workbook workbook, String sheetName) {
        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex != -1) {
            workbook.removeSheetAt(sheetIndex);
        }
    }

    /**
     * 复制工作表
     *
     * @param workbook 工作簿对象
     * @param sourceSheetName 源工作表名称
     * @param targetSheetName 目标工作表名称
     * @return 新复制的工作表对象
     */
    public static Sheet copySheet(Workbook workbook, String sourceSheetName, String targetSheetName) {
        Sheet sourceSheet = workbook.getSheet(sourceSheetName);
        if (sourceSheet == null) {
            throw new IllegalArgumentException("Source sheet not found: " + sourceSheetName);
        }
        Sheet targetSheet = workbook.createSheet(targetSheetName);
        for (Row sourceRow : sourceSheet) {
            Row targetRow = targetSheet.createRow(sourceRow.getRowNum());
            for (Cell sourceCell : sourceRow) {
                Cell targetCell = targetRow.createCell(sourceCell.getColumnIndex());
                targetCell.setCellStyle(sourceCell.getCellStyle());
                switch (sourceCell.getCellType()) {
                    case STRING:
                        targetCell.setCellValue(sourceCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    default:
                        targetCell.setCellValue(sourceCell.toString());
                }
            }
        }
        return targetSheet;
    }

    /**
     * 合并单元格
     *
     * @param sheet 工作表对象
     * @param firstRow 第一行号（从0开始）
     * @param lastRow 最后一行号（从0开始）
     * @param firstCol 第一列号（从0开始）
     * @param lastCol 最后一列号（从0开始）
     */
    public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 设置单元格样式
     *
     * @param workbook 工作簿对象
     * @param fontColor 字体颜色
     * @param fontSize 字体大小
     * @param bold 是否加粗
     * @param backgroundColor 背景颜色
     * @return 单元格样式
     */
    public static CellStyle createCellStyle(Workbook workbook, short fontColor, short fontSize, boolean bold, short backgroundColor) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(fontColor);
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        style.setFont(font);
        style.setFillForegroundColor(backgroundColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置单元格样式
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param style 单元格样式
     */
    public static void setCellStyle(Sheet sheet, int row, int col, CellStyle style) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        cell.setCellStyle(style);
    }

    /**
     * 插入图片
     *
     * @param sheet 工作表对象
     * @param pictureData 图片数据
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param width 图片宽度
     * @param height 图片高度
     */
    public static void insertPicture(Sheet sheet, byte[] pictureData, int row, int col, int width, int height) {
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col, row, col + width, row + height);
        Picture pict = drawing.createPicture(anchor, sheet.getWorkbook().addPicture(pictureData, Workbook.PICTURE_TYPE_PNG));
        pict.resize();
    }

    /**
     * 设置单元格边框
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param borderStyle 边框样式
     * @param borderColor 边框颜色
     */
    public static void setCellBorder(Sheet sheet, int row, int col, BorderStyle borderStyle, short borderColor) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        CellStyle style = cell.getCellStyle();
        if (style == null) {
            style = sheet.getWorkbook().createCellStyle();
        }
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        style.setTopBorderColor(borderColor);
        style.setBottomBorderColor(borderColor);
        style.setLeftBorderColor(borderColor);
        style.setRightBorderColor(borderColor);
        cell.setCellStyle(style);
    }

    /**
     * 设置单元格对齐方式
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param horizontalAlign 水平对齐方式
     * @param verticalAlign 垂直对齐方式
     */
    public static void setCellAlignment(Sheet sheet, int row, int col, HorizontalAlignment horizontalAlign, VerticalAlignment verticalAlign) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        CellStyle style = cell.getCellStyle();
        if (style == null) {
            style = sheet.getWorkbook().createCellStyle();
        }
        style.setAlignment(horizontalAlign);
        style.setVerticalAlignment(verticalAlign);
        cell.setCellStyle(style);
    }

    /**
     * 设置单元格背景色
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param color 背景色
     */
    public static void setCellBackgroundColor(Sheet sheet, int row, int col, short color) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        CellStyle style = cell.getCellStyle();
        if (style == null) {
            style = sheet.getWorkbook().createCellStyle();
        }
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    /**
     * 插入超链接
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param url 超链接地址
     * @param displayText 显示文本
     */
    public static void insertHyperlink(Sheet sheet, int row, int col, String url, String displayText) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        Hyperlink link = sheet.getWorkbook().getCreationHelper().createHyperlink(HyperlinkType.URL);
        link.setAddress(url);
        cell.setHyperlink(link);
        cell.setCellValue(displayText);
    }

    /**
     * 设置单元格公式
     *
     * @param sheet 工作表对象
     * @param row 行号（从0开始）
     * @param col 列号（从0开始）
     * @param formula 公式
     */
    public static void setCellFormula(Sheet sheet, int row, int col, String formula) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(formula);
    }

    /**
     * 保存工作簿到文件
     *
     * @param workbook 工作簿对象
     * @param filePath 文件路径
     * @throws IOException 如果写入文件时发生错误
     */
    public static void saveWorkbook(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            workbook.write(fos);
        }
        workbook.close();
    }
}

