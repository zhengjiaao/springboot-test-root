package com.zja.file.excel.poi;

import javafx.scene.chart.Chart;
import javafx.scene.chart.ValueAxis;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @Author: zhengja
 * @Date: 2024-10-28 13:39
 */
public class ReadExcelTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testReadExcel() throws IOException {
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

        // 读取文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook readWorkbook = new XSSFWorkbook(fileIn);
            Sheet readSheet = readWorkbook.getSheetAt(0);
            Row readRow = readSheet.getRow(0);
            Cell readCell = readRow.getCell(0);

            // 验证单元格内容
            assertEquals("Hello, World!", readCell.getStringCellValue(), "单元格内容应为 'Hello, World!'");

            // 关闭工作簿
            readWorkbook.close();
        }
    }

}
