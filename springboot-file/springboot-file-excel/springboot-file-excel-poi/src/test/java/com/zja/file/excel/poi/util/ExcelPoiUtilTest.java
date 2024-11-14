package com.zja.file.excel.poi.util;

import org.apache.poi.ss.usermodel.*;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-11-14 22:07
 */
public class ExcelPoiUtilTest {

    public Path tempDir = Paths.get("target");
    private String templatePath = "D:\\temp\\excel\\template.xlsx";
    private String outputPath = Paths.get("target","output.xlsx").toString();
    private String outputUpdatedPath = Paths.get("target","updated.xlsx").toString();

    // 写入Excel文件
    @Test
    public void test_1() {
        List<Map<String, Object>> data = Lists.newArrayList(
                Maps.newHashMap("name", "张三"),
                Maps.newHashMap("name", "李四")
        );

        try {
            ExcelPoiUtil.writeExcel(templatePath, outputPath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取Excel文件
    @Test
    public void test_2() {
        try {
            List<Map<String, Object>> data = ExcelPoiUtil.readExcel(outputPath);
            // 处理数据
            for (Map<String, Object> row : data) {
                System.out.println(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取工作表并设置单元格值
    @Test
    public void test_3() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.setCellValue(sheet, 0, 0, "Hello");
            ExcelPoiUtil.setCellValue(sheet, 0, 1, 123.45);
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取单元格值
    @Test
    public void test_4() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            Object value = ExcelPoiUtil.getCellValue(sheet, 0, 0);
            System.out.println("Cell value: " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除工作表
    @Test
    public void test_5() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            ExcelPoiUtil.removeSheet(workbook, "Sheet1");
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 复制工作表
    @Test
    public void test_6() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            ExcelPoiUtil.copySheet(workbook, "Sheet1", "CopiedSheet");
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 合并单元格
    @Test
    public void test_7() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.mergeCells(sheet, 0, 1, 0, 1);
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 插入图片
    @Test
    public void test_8() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            byte[] pictureData = Files.readAllBytes(Paths.get("path/to/image.png"));
            ExcelPoiUtil.insertPicture(sheet, pictureData, 0, 0, 1, 1);
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置单元格边框
    @Test
    public void test_9() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.setCellBorder(sheet, 0, 0, BorderStyle.THIN, IndexedColors.BLACK.getIndex());
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置单元格对齐方式
    @Test
    public void test_10() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.setCellAlignment(sheet, 0, 0, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置单元格背景色
    @Test
    public void test_11() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.setCellBackgroundColor(sheet, 0, 0, IndexedColors.YELLOW.getIndex());
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 插入超链接
    @Test
    public void test_12() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.insertHyperlink(sheet, 0, 0, "https://www.example.com", "点击这里");
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置单元格公式
    @Test
    public void test_13() {
        try {
            Workbook workbook = ExcelPoiUtil.getWorkbook(outputUpdatedPath);
            Sheet sheet = ExcelPoiUtil.getSheet(workbook, "Sheet1");
            ExcelPoiUtil.setCellFormula(sheet, 0, 0, "A1+B1");
            ExcelPoiUtil.saveWorkbook(workbook, outputUpdatedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
