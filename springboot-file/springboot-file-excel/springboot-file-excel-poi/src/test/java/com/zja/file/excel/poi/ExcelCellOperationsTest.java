package com.zja.file.excel.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 单元格操作
 *
 * @Author: zhengja
 * @Date: 2024-10-28 14:00
 */
public class ExcelCellOperationsTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");


    /**
     * 合并单元格：
     * 使用 sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1)); 方法合并从第0行到第1行，第0列到第1列的单元格。
     * 添加单元格：
     * 使用 row2.createCell(0).setCellValue("New Cell"); 方法在第2行第0列创建一个新的单元格，并设置其值为 "New Cell"。
     * 修改单元格内容：
     * 使用 row2.createCell(1).setCellValue("Modified Cell"); 方法在第2行第1列创建一个新的单元格，并设置其值为 "Modified Cell"。
     * 删除单元格：
     * 使用 row3.removeCell(row3.getCell(0)); 方法删除第3行第0列的单元格。
     */
    @Test
    public void testCellOperations() throws IOException {
        // 创建临时文件
        File tempFile = tempDir.resolve("cell_operations.xlsx").toFile();

        // 调用方法创建并操作 Excel 文件
        createAndOperateExcel(tempFile);

        // 验证文件是否存在
        assertTrue(tempFile.exists(), "文件应存在");

        // 验证文件大小是否大于0
        assertTrue(tempFile.length() > 0, "文件应有内容");

        // 读取文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 验证合并单元格
            CellRangeAddress mergedRegion = sheet.getMergedRegion(0);
            assertEquals(0, mergedRegion.getFirstRow(), "合并区域的第一行应为0");
            assertEquals(0, mergedRegion.getFirstColumn(), "合并区域的第一列应为0");
            assertEquals(1, mergedRegion.getLastRow(), "合并区域的最后一行应为1");
            assertEquals(1, mergedRegion.getLastColumn(), "合并区域的最后一列应为1");

            // 验证添加的单元格内容
            Row row1 = sheet.getRow(2);
            Cell cell1 = row1.getCell(0);
            assertEquals("New Cell", cell1.getStringCellValue(), "单元格内容应为'New Cell'");

            // 验证修改的单元格内容
            Cell cell2 = row1.getCell(1);
            assertEquals("Modified Cell", cell2.getStringCellValue(), "单元格内容应为'Modified Cell'");

            // 验证删除的单元格
            Row row2 = sheet.getRow(3);
            assertNull(row2.getCell(0), "单元格应为空");

            // 关闭工作簿
            workbook.close();
        }
    }

    public static void createAndOperateExcel(File file) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("单元格操作示例");

        // 创建一些初始数据
        Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("A1");
        row0.createCell(1).setCellValue("B1");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("A2");
        row1.createCell(1).setCellValue("B2");

        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));

        // 添加单元格
        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("New Cell");

        // 修改单元格内容
        row2.createCell(1).setCellValue("Modified Cell");

        // 删除单元格
        Row row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue("To Be Deleted");
        row3.removeCell(row3.getCell(0));

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();
    }
}
