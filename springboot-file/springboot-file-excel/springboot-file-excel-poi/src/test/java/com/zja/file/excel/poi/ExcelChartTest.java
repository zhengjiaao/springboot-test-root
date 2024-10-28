package com.zja.file.excel.poi;

import com.zja.file.excel.poi.util.ExcelUtil;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 图表：折线图，饼图，柱状图，动态列表
 *
 * @Author: zhengja
 * @Date: 2024-10-28 15:19
 */
public class ExcelChartTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateLineChart() throws IOException {
        File tempFile = tempDir.resolve("line_chart_example.xlsx").toFile();

        // 调用 createLineChart 方法生成 Excel 文件
        ExcelUtil.createLineChart(tempFile);

        // 读取生成的 Excel 文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
            XSSFChart chart = (XSSFChart) drawing.getCharts().get(0);
            assertNotNull(chart, "应有一个图表");

            // 检查图表是否存在
            /*boolean chartFound = false;
            for (POIXMLDocumentPart part : sheet.getRelations()) {
                if (part instanceof XSSFChart) {
                    XSSFChart chart = (XSSFChart) part;
                    chartFound = true;

                  *//*  // 验证图表类型
                    XDDFChartData chartData = chart.getChartData();
                    assertTrue(chartData instanceof XDDFLineChartData, "图表类型应为 LINE");

                    // 验证图表标题
                    XDDFTitle title = chart.getCTChart().getTitle();
                    assertNotNull(title, "图表标题应存在");
                    assertEquals("Sample Line Chart", title.getTx().getRich().getPArray(0).getRArray(0).getT(), "图表标题应为 'Sample Line Chart'");

                    // 验证数据系列
                    XDDFChartData.Series series = chartData.getSeriesList().get(0);
                    assertNotNull(series, "数据系列应存在");
                    assertEquals("Data Series 1", series.getTitle(), "数据系列标题应为 'Data Series 1'");*//*

                    break; // 找到图表后退出循环
                }
            }

            assertTrue(chartFound, "图表未找到");*/
        }
    }

    @Test
    public void testCreatePieChart() throws IOException {
        File tempFile = tempDir.resolve("pie_chart_example.xlsx").toFile();

        // 调用 createPieChart 方法生成 Excel 文件
        ExcelUtil.createPieChart(tempFile);

        // 读取生成的 Excel 文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
            XSSFChart chart = (XSSFChart) drawing.getCharts().get(0);
            assertNotNull(chart, "应有一个图表");

            /*// 检查图表是否存在
            boolean chartFound = false;
            for (POIXMLDocumentPart part : sheet.getRelations()) {
                if (part instanceof XSSFChart) {
                    XSSFChart chart = (XSSFChart) part;
                    chartFound = true;

                   *//* // 验证图表类型
                    XDDFChartData chartData = chart.getChartData();
                    assertTrue(chartData instanceof XDDFPieChartData, "图表类型应为 PIE");

                    // 验证图表标题
                    XDDFTitle title = chart.getCTChart().getTitle();
                    assertNotNull(title, "图表标题应存在");
                    assertEquals("Sample Pie Chart", title.getTx().getRich().getPArray(0).getRArray(0).getT(), "图表标题应为 'Sample Pie Chart'");

                    // 验证数据系列
                    XDDFChartData.Series series = chartData.getSeriesList().get(0);
                    assertNotNull(series, "数据系列应存在");
                    assertEquals("Data Series 1", series.getTitle(), "数据系列标题应为 'Data Series 1'");*//*

                    break; // 找到图表后退出循环
                }
            }

            assertTrue(chartFound, "图表未找到");*/
        }
    }

    @Test
    public void testCreateBarChart() throws IOException {
        File tempFile = tempDir.resolve("bar_chart_example.xlsx").toFile();

        // 调用 createBarChart 方法生成 Excel 文件
        ExcelUtil.createBarChart(tempFile);

        // 读取生成的 Excel 文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
            XSSFChart chart = (XSSFChart) drawing.getCharts().get(0);
            assertNotNull(chart, "应有一个图表");

            /*// 检查图表是否存在
            boolean chartFound = false;
            for (POIXMLDocumentPart part : sheet.getRelations()) {
                if (part instanceof XSSFChart) {
                    XSSFChart chart = (XSSFChart) part;
                    chartFound = true;

                   *//* // 验证图表类型
                    XDDFChartData chartData = chart.getChartData();
                    assertTrue(chartData instanceof XDDFBarChartData, "图表类型应为 BAR");

                    // 验证图表标题
                    XDDFTitle title = chart.getCTChart().getTitle();
                    assertNotNull(title, "图表标题应存在");
                    assertEquals("Sample Bar Chart", title.getTx().getRich().getPArray(0).getRArray(0).getT(), "图表标题应为 'Sample Bar Chart'");

                    // 验证数据系列
                    XDDFChartData.Series series = chartData.getSeriesList().get(0);
                    assertNotNull(series, "数据系列应存在");
                    assertEquals("Data Series 1", series.getTitle(), "数据系列标题应为 'Data Series 1'");*//*

                    break; // 找到图表后退出循环
                }
            }

            assertTrue(chartFound, "图表未找到");*/
        }
    }

    @Test
    @Deprecated
    public void testCreateDynamicList() throws IOException {
        File tempFile = tempDir.resolve("dynamic_list_example.xlsx").toFile();

        // 调用 createDynamicList 方法生成 Excel 文件
        ExcelUtil.createDynamicList(tempFile);

        // 读取生成的 Excel 文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = workbook.getSheetAt(0);

           /* // 检查数据验证是否存在
            DataValidation dataValidation = sheet.getDataValidations().getDataValidation(0);
            assertNotNull(dataValidation, "数据验证应存在");

            // 检查数据验证的约束条件
            DataValidationConstraint constraint = dataValidation.getValidationConstraint();
            assertNotNull(constraint, "数据验证约束条件应存在");
            assertTrue(constraint.isExplicitList(), "数据验证应为显式列表");

            // 检查数据验证的范围
            CellRangeAddressList addressList = (CellRangeAddressList) dataValidation.getRegions();
            assertNotNull(addressList, "数据验证范围应存在");
            assertEquals(1, addressList.getNumberOfRanges(), "数据验证范围应只有一个");
            assertEquals(new CellRangeAddress(1, 10, 1, 1), addressList.getCellRangeAddress(0), "数据验证范围应为 B2:B11");

            // 检查数据验证的选项
            assertFalse(dataValidation.isSuppressDropDownArrow(), "数据验证不应隐藏下拉箭头");*/
        }
    }
}
