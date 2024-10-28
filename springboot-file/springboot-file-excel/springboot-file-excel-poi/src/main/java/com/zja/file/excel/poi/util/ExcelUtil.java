package com.zja.file.excel.poi.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 15:24
 */
public class ExcelUtil {

    // 创建折线图
    public static void createLineChart(File file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            // 创建数据单元格
            for (int i = 1; i <= 10; i++) {
                Row dataRow = sheet.createRow(i - 1);
                Cell dataCell = dataRow.createCell(0);
                dataCell.setCellValue(i);
                Cell dataCell2 = dataRow.createCell(1);
                dataCell2.setCellValue(i * 2);
            }

            // 创建图表
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 3, 0, 10, 15);
            XSSFChart chart = drawing.createChart(anchor);

            // 设置图表标题
            chart.setTitleText("Sample Line Chart");
            chart.setTitleOverlay(false);

            // 创建数据源
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 9, 0, 0));
            XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 9, 1, 1));

            // 创建数据系列
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(xs, ys1);
            series1.setTitle("Data Series 1", null);

            // 绘制图表
            chart.plot(data);

            // 保存工作簿
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    // 创建饼图
    public static void createPieChart(File file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            // 创建数据单元格
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Value");

            String[] categories = {"A", "B", "C", "D"};
            double[] values = {30, 70, 50, 100};

            for (int i = 0; i < categories.length; i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(categories[i]);
                dataRow.createCell(1).setCellValue(values[i]);
            }

            // 创建图表
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 3, 0, 10, 15);
            XSSFChart chart = drawing.createChart(anchor);

            // 设置图表标题
            chart.setTitleText("Sample Pie Chart");
            chart.setTitleOverlay(false);

            // 创建数据源
            XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 4, 0, 0));
            XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 1, 1));

            // 创建数据系列
            XDDFPieChartData pieChartData = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);
            XDDFPieChartData.Series series = (XDDFPieChartData.Series) pieChartData.addSeries(categoriesData, valuesData);
            series.setTitle("Data Series 1", null);

            // 绘制图表
            chart.plot(pieChartData);

            // 保存工作簿
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    // 创建柱状图
    public static void createBarChart(File file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            // 创建数据单元格
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Value");

            String[] categories = {"A", "B", "C", "D"};
            double[] values = {30, 70, 50, 100};

            for (int i = 0; i < categories.length; i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(categories[i]);
                dataRow.createCell(1).setCellValue(values[i]);
            }

            // 创建图表
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 3, 0, 10, 15);
            XSSFChart chart = drawing.createChart(anchor);

            // 设置图表标题
            chart.setTitleText("Sample Bar Chart");
            chart.setTitleOverlay(false);

            // 创建数据源
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 4, 0, 0));
            XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 1, 1));

            // 创建数据系列
            XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFBarChartData.Series series = (XDDFBarChartData.Series) barChartData.addSeries(categoriesData, valuesData);
            series.setTitle("Data Series 1", null);

            // 绘制图表
            chart.plot(barChartData);

            // 保存工作簿
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    // 创建动态下拉列表
    public static void createDynamicList(File file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            // 创建数据单元格
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Selection");

            String[] categories = {"A", "B", "C", "D"};

            for (int i = 0; i < categories.length; i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(categories[i]);
            }

            // 创建数据验证
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(categories);
            CellRangeAddressList addressList = new CellRangeAddressList(1, 10, 1, 1);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);

            // 设置数据验证显示提示
            validation.setSuppressDropDownArrow(false);  // 确保下拉箭头不被隐藏

            // 添加数据验证到工作表
            sheet.addValidationData(validation);

            // 保存工作簿
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }
}
