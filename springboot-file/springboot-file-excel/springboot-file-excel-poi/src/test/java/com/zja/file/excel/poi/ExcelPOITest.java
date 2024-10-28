package com.zja.file.excel.poi;

import javafx.scene.chart.Chart;
import javafx.scene.chart.ValueAxis;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Excel poi 常用操作
 * <p>
 * 创建 Excel 文件 (createExcel): 创建一个简单的 Excel 文件并写入 "Hello, World!"。
 * 读取 Excel 文件 (readExcel): 读取创建的 Excel 文件并验证内容。
 * 更新 Excel 文件 (updateExcel): 更新 Excel 文件中的内容。
 * 删除 Excel 内容 (deleteExcelContent): 删除 Excel 文件中的内容。
 * 合并单元格 (mergeCells): 合并多个单元格。
 * 设置单元格样式 (setCellStyle): 设置单元格的样式，包括对齐方式和背景色。
 * 设置日期格式 (setDateFormat): 设置单元格的日期格式。
 * 插入图片 (insertImage): 插入图片到 Excel 文件中。
 * 设置公式 (setFormula): 设置单元格的公式。
 * 设置超链接 (setHyperlink): 设置单元格的超链接。
 * 设置下拉列表 (setDropdownList): 设置单元格的下拉列表。
 * 创建图表 (createChart): 创建一个简单的折线图。
 * 设置条件格式 (setConditionalFormatting): 设置单元格的条件格式。
 * 设置批注 (setComment): 设置单元格的批注。
 * 设置数据验证 (setDataValidation): 设置单元格的数据验证。
 *
 * @Author: zhengja
 * @Date: 2024-10-28 13:19
 */
public class ExcelPOITest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateExcel() throws IOException {
        File tempFile = tempDir.resolve("create_excel.xlsx").toFile();
        createExcel(tempFile);

        assertTrue(tempFile.exists(), "文件应存在");
        assertTrue(tempFile.length() > 0, "文件应有内容");

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            assertEquals("Hello, World!", cell.getStringCellValue(), "单元格内容应为'Hello, World!'");
        }
    }

    @Test
    public void testReadExcel() throws IOException {
        File tempFile = tempDir.resolve("read_excel.xlsx").toFile();
        createExcel(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            assertEquals("Hello, World!", cell.getStringCellValue(), "单元格内容应为'Hello, World!'");
        }
    }

    @Test
    public void testUpdateExcel() throws IOException {
        File tempFile = tempDir.resolve("update_excel.xlsx").toFile();
        createExcel(tempFile);

        updateExcel(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            assertEquals("Updated Value", cell.getStringCellValue(), "单元格内容应为'Updated Value'");
        }
    }

    @Test
    public void testDeleteExcelContent() throws IOException {
        File tempFile = tempDir.resolve("delete_excel_content.xlsx").toFile();
        createExcel(tempFile);

        deleteExcelContent(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            assertNull(row, "行应被删除");
        }
    }

    @Test
    public void testMergeCells() throws IOException {
        File tempFile = tempDir.resolve("merge_cells.xlsx").toFile();
        createMergeExcel(tempFile);

        mergeCells(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            CellRangeAddress mergedRegion = sheet.getMergedRegion(0);
            assertEquals(0, mergedRegion.getFirstRow(), "合并区域的第一行应为0");
            assertEquals(0, mergedRegion.getFirstColumn(), "合并区域的第一列应为0");
            assertEquals(1, mergedRegion.getLastRow(), "合并区域的最后一行应为1");
            assertEquals(1, mergedRegion.getLastColumn(), "合并区域的最后一列应为1");
        }
    }

    @Test
    public void testSetCellStyle() throws IOException {
        File tempFile = tempDir.resolve("set_cell_style.xlsx").toFile();
        createExcel(tempFile);

        setCellStyle(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            CellStyle style = cell.getCellStyle();
            // 检查水平对齐方式
            assertEquals(HorizontalAlignment.CENTER, style.getAlignment(), "水平对齐应为居中");
            // 检查垂直对齐方式
            assertEquals(VerticalAlignment.CENTER, style.getVerticalAlignment(), "垂直对齐应为居中");
            assertEquals(IndexedColors.YELLOW.getIndex(), style.getFillForegroundColor(), "背景色应为黄色");
        }
    }

    @Test
    public void testSetDateFormat() throws IOException {
        File tempFile = tempDir.resolve("set_date_format.xlsx").toFile();
        createExcel(tempFile);

        setDateFormat(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            assertEquals("yyyy-MM-dd", cell.getCellStyle().getDataFormatString(), "日期格式应为'yyyy-MM-dd'");
        }
    }

    @Test
    public void testInsertImage() throws IOException {
        File tempFile = tempDir.resolve("insert_image.xlsx").toFile();
        createExcel(tempFile);

        File imageFile = new File("D:\\temp\\images\\test.png");
        insertImage(tempFile, imageFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
            long pictureCount = drawing.getShapes().stream().filter(shape -> shape instanceof XSSFPicture).count();
            assertEquals(1, pictureCount, "应有一张图片");
        }
    }

    @Test
    public void testSetFormula() throws IOException {
        File tempFile = tempDir.resolve("set_formula.xlsx").toFile();
        // createExcel(tempFile);

        setFormula(tempFile);

        // 读取生成的 Excel 文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 检查数据单元格的值
            for (int i = 1; i <= 10; i++) {
                Row dataRow = sheet.getRow(i - 1);
                Cell dataCell = dataRow.getCell(0);
                assertEquals((double) i, dataCell.getNumericCellValue(), "数据单元格 " + i + " 的值应为 " + i);
            }

            // 检查公式单元格的值
            Row formulaRow = sheet.getRow(10); // 11 行
            Cell formulaCell = formulaRow.getCell(0); // A11
            System.out.println(formulaCell); // SUM(A1:A10)，在 Apache POI 中，公式单元格的值在读取时不会自动计算。你需要显式地调用 evaluateFormulaCell 方法来计算公式的结果。
            // assertEquals(55.0, formulaCell.getNumericCellValue(), "公式单元格的值应为 55");

            // 创建公式评估器
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // 计算公式单元格的值
            CellValue cellValue = evaluator.evaluate(formulaCell);
            double formulaResult = cellValue.getNumberValue();

            assertEquals(55.0, formulaResult, "公式单元格的值应为 55");
        }
    }

    @Test
    public void testSetHyperlink() throws IOException {
        File tempFile = tempDir.resolve("set_hyperlink.xlsx").toFile();
        createExcel(tempFile);

        setHyperlink(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            XSSFHyperlink hyperlink = (XSSFHyperlink) cell.getHyperlink();
            assertNotNull(hyperlink, "单元格应有超链接");
            assertEquals("https://www.example.com", hyperlink.getAddress(), "超链接地址应为'https://www.example.com'");
        }
    }

    @Test
    public void testSetDropdownList() throws IOException {
        File tempFile = tempDir.resolve("set_dropdown_list.xlsx").toFile();
        createExcel(tempFile);

        setDropdownList(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 获取所有数据验证对象
            DataValidation[] validations = sheet.getDataValidations().toArray(new DataValidation[0]);

            // 确保数组不为空，并获取第一个数据验证对象。然后检查数据验证的约束条件，确保下拉列表的值正确。
            if (validations.length > 0) {
                DataValidation validation = validations[0];
                XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) validation.getValidationConstraint();
                String[] values = constraint.getExplicitListValues();
                assertArrayEquals(new String[]{"Option1", "Option2", "Option3"}, values, "下拉列表值应为'Option1', 'Option2', 'Option3'");
            } else {
                throw new AssertionError("未找到数据验证对象");
            }
        }
    }

    // 创建图表
    @Test
    @Deprecated
    public void testCreateChart() throws IOException {
        // File tempFile = tempDir.resolve("create_chart.xlsx").toFile();
        File tempFile = tempDir.resolve("line_chart_example.xlsx").toFile();
        // createChart(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
            XSSFChart chart = (XSSFChart) drawing.getCharts().get(0);
            assertNotNull(chart, "应有一个图表");
        }
    }

    @Test
    @Deprecated
    public void testSetConditionalFormatting() throws IOException {
        File tempFile = tempDir.resolve("set_conditional_formatting.xlsx").toFile();
        createExcel(tempFile);

        setConditionalFormatting(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFConditionalFormatting cf = (XSSFConditionalFormatting) xssfSheet.getCTWorksheet().getConditionalFormattingArray(0);
            assertNotNull(cf, "应有条件格式");
        }
    }

    @Test
    public void testSetComment() throws IOException {
        File tempFile = tempDir.resolve("set_comment.xlsx").toFile();
        createExcel(tempFile);

        setComment(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            Comment comment = cell.getCellComment();
            assertNotNull(comment, "单元格应有批注");
            assertEquals("This is a comment", comment.getString().getString(), "批注内容应为'This is a comment'");
        }
    }

    @Test
    public void testSetDataValidation() throws IOException {
        File tempFile = tempDir.resolve("set_data_validation.xlsx").toFile();
        createExcel(tempFile);

        setDataValidation(tempFile);

        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 获取所有数据验证对象
            DataValidation[] validations = sheet.getDataValidations().toArray(new DataValidation[0]);

            if (validations.length > 0) {
                DataValidation validation = validations[0];
                XSSFDataValidation xssfValidation = (XSSFDataValidation) validation;

                // 检查数据验证类型
                XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) xssfValidation.getValidationConstraint();
                assertEquals(DataValidationConstraint.ValidationType.DECIMAL, constraint.getValidationType(), "数据验证类型应为小数");

                // 检查数据验证范围
                CellRangeAddressList addressList = xssfValidation.getRegions();
                assertEquals(1, addressList.countRanges(), "数据验证区域数量应为1");
                CellRangeAddress area = addressList.getCellRangeAddress(0);
                assertEquals(0, area.getFirstRow(), "起始行应为0");
                assertEquals(0, area.getLastRow(), "结束行应为0");
                assertEquals(0, area.getFirstColumn(), "起始列应为0");
                assertEquals(0, area.getLastColumn(), "结束列应为0");

                // 检查数据验证条件
                assertEquals(DataValidationConstraint.OperatorType.BETWEEN, constraint.getOperator(), "数据验证条件应为介于");
                assertEquals("0", constraint.getFormula1(), "最小值应为0");
                assertEquals("100", constraint.getFormula2(), "最大值应为100");
            } else {
                throw new AssertionError("未找到数据验证对象");
            }
        }
    }

    private void createExcel(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello, World!");

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
    }

    private void updateExcel(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            cell.setCellValue("Updated Value");

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void deleteExcelContent(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.removeRow(sheet.getRow(0));

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    public void createMergeExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("MergedCells");

            // 创建一些单元格
            Row row1 = sheet.createRow(0);
            Row row2 = sheet.createRow(1);
            Cell cell1 = row1.createCell(0);
            Cell cell2 = row2.createCell(0);
            cell1.setCellValue("Cell 1");
            cell2.setCellValue("Cell 2");

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    public void mergeCells(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void setCellStyle(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);

            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(style);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void setDateFormat(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(new Date());

            CellStyle style = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
            cell.setCellStyle(style);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void insertImage(File file, File imageFile) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            byte[] bytes = IOUtils.toByteArray(Files.newInputStream(imageFile.toPath()));
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    public void setFormula(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // 创建数据单元格
            for (int i = 1; i <= 10; i++) {
                Row dataRow = sheet.createRow(i - 1);
                Cell dataCell = dataRow.createCell(0);
                dataCell.setCellValue(i);
            }

            // 创建公式单元格
            Row formulaRow = sheet.createRow(10);
            Cell formulaCell = formulaRow.createCell(0);
            formulaCell.setCellFormula("SUM(A1:A10)");

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void setHyperlink(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Example Link");

            // 创建超链接
            Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
            link.setAddress("https://www.example.com");
            cell.setHyperlink(link);

            // 创建超链接样式
            CellStyle hyperlinkStyle = workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
            hyperlinkStyle.setFont(hyperlinkFont);

            // 应用超链接样式
            cell.setCellStyle(hyperlinkStyle);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    // 设置下拉列表
    private void setDropdownList(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(new String[]{"Option1", "Option2", "Option3"});
            CellRangeAddressList addressList = new CellRangeAddressList(0, 0, 0, 0);
            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            sheet.addValidationData(validation);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

   /* private void createChart(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.createSheet("ChartSheet");

            // 创建数据
            for (int rownum = 0; rownum < 5; rownum++) {
                Row row = sheet.createRow(rownum);
                Cell cell = row.createCell(0);
                cell.setCellValue(rownum * 100);
                cell = row.createCell(1);
                cell.setCellValue((rownum + 1) * 100);
            }

            // 创建绘图区域
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 2, 2, (short) 7, 15);
            Chart chart = drawing.createChart(anchor);

            // 设置图表标题
            ChartTitle title = chart.getOrCreateTitle();
            title.setText("Sample Chart");

            // 设置图表类型
            LineChartData data = chart.getChartDataFactory().createLineChartData();

            // 创建数据集
            ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(0, 4, 0, 0));
            ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(0, 4, 1, 1));

            // 添加数据集到图表
            data.addSeries(xs, ys1);

            // 设置图表轴
            ValueAxis bottomAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.BOTTOM);
            ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            // 将数据和轴添加到图表
            chart.plot(data, bottomAxis, leftAxis);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }*/

    private void setConditionalFormatting(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 创建条件格式规则
            XSSFConditionalFormattingRule rule = ((XSSFSheet) sheet).getSheetConditionalFormatting().createConditionalFormattingColorScaleRule();

            // 应用条件格式
            CellRangeAddress[] regions = {new CellRangeAddress(0, 4, 0, 0)};
            ((XSSFSheet) sheet).getSheetConditionalFormatting().addConditionalFormatting(regions, rule);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void setComment(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Commented Cell");

            // 创建批注
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1);
            Comment comment = drawing.createCellComment(anchor);
            RichTextString str = workbook.getCreationHelper().createRichTextString("This is a comment");
            comment.setString(str);

            // 将批注添加到单元格
            cell.setCellComment(comment);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    private void setDataValidation(File file) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint constraint = validationHelper.createNumericConstraint(
                    DataValidationConstraint.ValidationType.DECIMAL,
                    DataValidationConstraint.OperatorType.BETWEEN,
                    "0",
                    "100"
            );

            CellRangeAddressList addressList = new CellRangeAddressList(0, 0, 0, 0);
            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            sheet.addValidationData(validation);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

}
