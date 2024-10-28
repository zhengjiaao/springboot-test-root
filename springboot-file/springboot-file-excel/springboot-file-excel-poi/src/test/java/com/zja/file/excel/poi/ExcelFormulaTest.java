package com.zja.file.excel.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 处理公式
 *
 * @Author: zhengja
 * @Date: 2024-10-28 13:47
 */
public class ExcelFormulaTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateFormulaExcel() throws IOException {
        // 创建临时文件
        File tempFile = tempDir.resolve("formula_workbook.xlsx").toFile();

        // 调用方法创建带公式的 Excel 文件
        createFormulaExcel(tempFile);

        // 验证文件是否存在
        assertTrue(tempFile.exists(), "文件应存在");

        // 验证文件大小是否大于0
        assertTrue(tempFile.length() > 0, "文件应有内容");

        // 读取文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row1 = sheet.getRow(0);
            Row row2 = sheet.getRow(1);

            // 验证单元格内容
            assertEquals(10, row1.getCell(0).getNumericCellValue(), "A1 单元格内容应为 10");
            assertEquals(20, row1.getCell(1).getNumericCellValue(), "B1 单元格内容应为 20");

            // 验证公式单元格
            Cell cellC1 = row2.getCell(2);
            assertEquals("A1+B1", cellC1.getCellFormula(), "C1 单元格公式应为 A1+B1");
            assertEquals(30, cellC1.getNumericCellValue(), "C1 单元格计算结果应为 30");

            // 关闭工作簿
            workbook.close();
        }
    }

    public static void createFormulaExcel(File file) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("公式示例");

        // 创建一行
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);

        // 创建单元格并设置值
        Cell cellA1 = row1.createCell(0);
        cellA1.setCellValue(10);
        Cell cellB1 = row1.createCell(1);
        cellB1.setCellValue(20);

        // 创建公式单元格
        Cell cellC1 = row2.createCell(2);
        cellC1.setCellFormula("A1+B1");

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();
    }
}
