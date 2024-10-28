package com.zja.file.excel.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 13:38
 */
public class CreateExcelTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateExcel() throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("示例工作表");

        // 创建一行
        Row row = sheet.createRow(0);

        // 创建单元格并设置值
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello, World!");

        // 写入文件
        File tempFile = tempDir.resolve("workbook.xlsx").toFile();
        try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();

        // 验证文件是否存在
        assertTrue(tempFile.exists(), "文件应存在");

        // 验证文件大小是否大于0
        assertTrue(tempFile.length() > 0, "文件应有内容");
    }
}
