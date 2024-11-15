package com.zja.util;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author: zhengja
 * @Date: 2024-11-14 22:32
 */

public class ExcelJxlsUtils {

    /**
     * 根据模板生成Excel文件
     *
     * @param templatePath 模板文件路径（支持资源文件路径或系统文件路径）
     * @param outputPath   输出文件路径 (包括文件名) ，示例: D:\\test.xlsx
     * @param context      上下文
     * @throws IOException 文件读取或写入异常
     */
    public static void generateExcel(String templatePath, String outputPath, Context context) throws IOException {
        try (InputStream is = getInputStream(templatePath)) {
            try (OutputStream os = Files.newOutputStream(Paths.get(outputPath))) {
                processTemplate(is, os, context);
            }
        }
    }

    /**
     * 根据模板生成Excel文件
     *
     * @param templatePath 模板文件路径（支持资源文件路径或系统文件路径）
     * @param outputPath   输出文件路径 (包括文件名) ，示例: D:\\test.xlsx
     * @param data         数据
     * @throws IOException 如果读写文件时发生错误
     */
    public static void generateExcel(String templatePath, String outputPath, Map<String, Object> data) throws IOException {
        try (InputStream is = getInputStream(templatePath)) {
            try (OutputStream os = Files.newOutputStream(Paths.get(outputPath))) {
                Context context = new Context(data);
                processTemplate(is, os, context);
            }
        }
    }

    /**
     * 根据模板生成Excel文件 带自定义工具类
     *
     * @param templatePath 模板文件路径
     * @param outputPath   输出文件路径 (包括文件名) ，示例: D:\\test.xlsx
     * @param context      上下文
     * @throws IOException 文件写入异常
     */
    public static void generateExcelWithUtils(String templatePath, String outputPath, Context context) throws IOException {
        try (InputStream is = getInputStream(templatePath)) {
            try (OutputStream os = Files.newOutputStream(Paths.get(outputPath))) {
                processTemplateWithUtils(is, os, context);
            }
        }
    }

    /**
     * 导出Excel - 通用方法
     *
     * @param response     响应对象
     * @param templateName 模板文件名
     * @param context      上下文
     * @param fileName     下载文件名
     * @throws IOException 文件写入异常
     */
    public static void exportExcel(HttpServletResponse response, String templateName, Context context, String fileName) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.ms-excel");

        try (InputStream is = getInputStream(templateName)) {
            try (OutputStream os = response.getOutputStream()) {
                processTemplate(is, os, context);
            }
        }
    }

    /**
     * 导出Excel - 通用方法，带自定义工具类
     *
     * @param response     响应对象
     * @param templateName 模板文件名
     * @param context      上下文
     * @param fileName     下载文件名
     * @throws IOException 文件写入异常
     */
    public static void exportExcelWithUtils(HttpServletResponse response, String templateName, Context context, String fileName) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.ms-excel");

        try (InputStream is = getInputStream(templateName)) {
            try (OutputStream os = response.getOutputStream()) {
                processTemplateWithUtils(is, os, context);
            }
        }
    }

    /**
     * 处理模板
     *
     * @param is      输入流
     * @param os      输出流
     * @param context 上下文
     * @throws IOException 文件读取或写入异常
     */
    private static void processTemplate(InputStream is, OutputStream os, Context context) throws IOException {
        JxlsHelper.getInstance().processTemplate(is, os, context);
    }

    /**
     * 处理模板，带自定义工具类
     *
     * @param is      输入流
     * @param os      输出流
     * @param context 上下文
     * @throws IOException 文件读取或写入异常
     */
    private static void processTemplateWithUtils(InputStream is, OutputStream os, Context context) throws IOException {
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> functions = new HashMap<>();
        functions.put("utils", new JxlsUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(functions);
        // jb.silent(true); //设置静默模式，不报警告
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        JxlsHelper.getInstance().processTemplate(context, transformer);
    }

    /**
     * 读取文件流，优先读取classpath下的文件，如未找到，则读取本地文件
     */
    public static InputStream getInputStream(String fileName) throws IOException {
        try {
            return getResourcesFileInputStream(fileName);
        } catch (IOException e) {
            try {
                return getFileInputStream(fileName);
            } catch (IOException e1) {
                e.addSuppressed(e1);
                throw new IOException("file not found：" + fileName, e);
            }
        }
    }

    // 读取本地文件流
    private static InputStream getFileInputStream(String filePath) throws IOException {
        return Files.newInputStream(Paths.get(filePath));
    }

    // 读取资源文件流（支持读取jar下面的资源文件）
    private static InputStream getResourcesFileInputStream(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return resource.getInputStream();
    }

    // -----------------------

    /**
     * 从 Excel 文件导入数据
     *
     * @param excelPath Excel 文件路径
     * @param clazz     数据对象的类
     * @param <T>       数据对象的类型
     * @return 导入的数据列表
     * @throws IOException 如果读写文件时发生错误
     */
    public static <T> List<T> importFromExcel(String excelPath, Class<T> clazz) throws IOException {
        try (FileInputStream fis = new FileInputStream(excelPath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            return convertWorkbookToList(workbook, clazz);
        }
    }

    /**
     * 将工作簿转换为数据对象列表
     *
     * @param workbook 工作簿
     * @param clazz    数据对象的类
     * @param <T>      数据对象的类型
     * @return 数据对象列表
     */
    private static <T> List<T> convertWorkbookToList(Workbook workbook, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            T obj = convertRowToObject(row, clazz);
            dataList.add(obj);
        }
        return dataList;
    }

    /**
     * 将行转换为数据对象
     *
     * @param row   行
     * @param clazz 数据对象的类
     * @param <T>   数据对象的类型
     * @return 数据对象
     */
    private static <T> T convertRowToObject(Row row, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                int columnIndex = Arrays.asList(fields).indexOf(field);
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    String value = getCellValue(cell);
                    Method setter = clazz.getMethod("set" + capitalize(field.getName()), String.class);
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取单元格的值
     *
     * @param cell 单元格
     * @return 单元格的值
     */
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 合并单元格
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param firstRow  第一行索引
     * @param lastRow   最后一行索引
     * @param firstCol  第一列索引
     * @param lastCol   最后一列索引
     */
    public static void mergeCells(Workbook workbook, String sheetName, int firstRow, int lastRow, int firstCol, int lastCol) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 设置单元格样式
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param row       行索引
     * @param col       列索引
     * @param style     单元格样式
     */
    public static void setCellStyle(Workbook workbook, String sheetName, int row, int col, CellStyle style) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        Cell c = r.getCell(col);
        if (c == null) {
            c = r.createCell(col);
        }
        c.setCellStyle(style);
    }

    /**
     * 创建单元格样式
     *
     * @param workbook 工作簿
     * @return 单元格样式
     */
    public static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 保存工作簿到文件
     *
     * @param workbook 工作簿
     * @param filePath 文件路径
     * @throws IOException 如果读写文件时发生错误
     */
    public static void saveWorkbookToFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * 读取工作簿
     *
     * @param filePath 文件路径
     * @return 工作簿
     * @throws IOException 如果读写文件时发生错误
     */
    public static Workbook readWorkbook(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return WorkbookFactory.create(fis);
        }
    }

    /**
     * 读取所有工作表
     *
     * @param workbook 工作簿
     * @return 所有工作表的列表
     */
    public static List<Sheet> getAllSheets(Workbook workbook) {
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.add(workbook.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * 复制工作表
     *
     * @param workbook        工作簿
     * @param sourceSheetName 源工作表名称
     * @param targetSheetName 目标工作表名称
     */
    public static void copySheet(Workbook workbook, String sourceSheetName, String targetSheetName) {
        Sheet sourceSheet = workbook.getSheet(sourceSheetName);
        if (sourceSheet == null) {
            throw new IllegalArgumentException("Source sheet not found: " + sourceSheetName);
        }
        Sheet targetSheet = workbook.cloneSheet(workbook.getSheetIndex(sourceSheetName));
        workbook.setSheetName(workbook.getSheetIndex(targetSheet), targetSheetName);
    }

    /**
     * 插入行
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param rowIndex  行索引
     */
    public static void insertRow(Workbook workbook, String sheetName, int rowIndex) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        sheet.shiftRows(rowIndex, sheet.getLastRowNum(), 1);
        Row newRow = sheet.createRow(rowIndex);
    }

    /**
     * 删除行
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param rowIndex  行索引
     */
    public static void deleteRow(Workbook workbook, String sheetName, int rowIndex) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
        sheet.removeRow(sheet.getRow(rowIndex));
    }

    /**
     * 插入列
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param colIndex  列索引
     */
    public static void insertColumn(Workbook workbook, String sheetName, int colIndex) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        for (Row row : sheet) {
            for (int i = row.getLastCellNum() - 1; i >= colIndex; i--) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    String cellValue = getCellValue(cell);
                    row.createCell(i + 1).setCellValue(cellValue);
                    row.removeCell(cell);
                }
            }
            row.createCell(colIndex);
        }
    }

    /**
     * 删除列
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param colIndex  列索引
     */
    public static void deleteColumn(Workbook workbook, String sheetName, int colIndex) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        for (Row row : sheet) {
            for (int i = colIndex; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    String cellValue = getCellValue(cell);
                    row.createCell(i - 1).setCellValue(cellValue);
                    row.removeCell(cell);
                }
            }
        }
    }

    /**
     * 读取特定工作表
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @return 工作表
     */
    public static Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    /**
     * 创建工作表
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @return 新创建的工作表
     */
    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * 删除工作表
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     */
    public static void deleteSheet(Workbook workbook, String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index != -1) {
            workbook.removeSheetAt(index);
        }
    }

    /**
     * 设置单元格值
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param row       行索引
     * @param col       列索引
     * @param value     单元格值
     */
    public static void setCellValue(Workbook workbook, String sheetName, int row, int col, Object value) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        Cell c = r.getCell(col);
        if (c == null) {
            c = r.createCell(col);
        }
        if (value instanceof String) {
            c.setCellValue((String) value);
        } else if (value instanceof Number) {
            c.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            c.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            c.setCellValue((Date) value);
        } else {
            c.setCellValue(value.toString());
        }
    }

    /**
     * 获取单元格值
     *
     * @param workbook  工作簿
     * @param sheetName 工作表名称
     * @param row       行索引
     * @param col       列索引
     * @return 单元格值
     */
    public static String getCellValue(Workbook workbook, String sheetName, int row, int col) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return "";
        }
        Row r = sheet.getRow(row);
        if (r == null) {
            return "";
        }
        Cell c = r.getCell(col);
        if (c == null) {
            return "";
        }
        return getCellValue(c);
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写的字符串
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
