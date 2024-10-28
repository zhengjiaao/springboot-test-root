package com.zja.file.excel.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 设置单元格样式
 *
 * @Author: zhengja
 * @Date: 2024-10-28 13:40
 */
public class ExcelStyleTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateStyledExcel() throws IOException {
        // 创建临时文件
        File tempFile = tempDir.resolve("styled_workbook.xlsx").toFile();

        // 调用方法创建带样式的 Excel 文件
        createStyledExcel(tempFile);

        // 验证文件是否存在
        assertTrue(tempFile.exists(), "文件应存在");

        // 验证文件大小是否大于0
        assertTrue(tempFile.length() > 0, "文件应有内容");

        // 读取文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);

            // 验证单元格内容
            assertEquals("Hello, World!", cell.getStringCellValue(), "单元格内容应为 'Hello, World!'");

            // 验证单元格样式
            CellStyle style = cell.getCellStyle();
            assertEquals(IndexedColors.YELLOW.getIndex(), style.getFillForegroundColor(), "背景色索引应为黄色");
            assertEquals(FillPatternType.SOLID_FOREGROUND, style.getFillPattern(), "填充模式应为实心");
            assertEquals(HorizontalAlignment.CENTER, style.getAlignment(), "水平对齐方式应为中心");
            assertEquals(VerticalAlignment.CENTER, style.getVerticalAlignment(), "垂直对齐方式应为中心");

            // 关闭工作簿
            workbook.close();
        }
    }

    public static void createStyledExcel(File file) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("样式示例");

        // 创建一行
        Row row = sheet.createRow(0);

        // 创建单元格并设置值
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello, World!");

        // 创建单元格样式
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 应用样式
        cell.setCellStyle(style);

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();
    }
}
