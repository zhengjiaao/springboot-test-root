package com.zja.util;

import com.zja.model.Company;
import com.zja.model.Employee;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.jxls.common.Context;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: zhengja
 * @Date: 2024-11-14 22:54
 */
public class ExcelJxlsUtilsTest {

    private List<Employee> generateSampleEmployeeData() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("李四", new Date(), BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
        employeeList.add(new Employee("李四", new Date(), BigDecimal.valueOf(200), BigDecimal.valueOf(200)));
        employeeList.add(new Employee("小芳", new Date(), BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
        employeeList.add(new Employee("小芳", new Date(), BigDecimal.valueOf(200), BigDecimal.valueOf(200)));
        return employeeList;
    }

    // Excel 普通数据填充
    @Test
    public void test_1() throws IOException {
        // 创建上下文
        Context context = new Context();
        context.putVar("money1", 80);
        context.putVar("money2", 20);

        // 指定模板路径和输出路径
        String templatePath = "templates/template_data.xlsx";
        String outputPath = "target/output1.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcel(templatePath, outputPath, context);
        // ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);
    }

    // Excel 列表数据填充
    @Test
    public void test_2() throws IOException {
        // 生成示例数据
        List<Employee> employees = generateSampleEmployeeData();

        // 创建上下文
        Context context = new Context();
        context.putVar("employees", employees);

        // 指定模板路径和输出路径
        String templatePath = "templates/template_data_list.xlsx";
        String outputPath = "target/output2.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcel(templatePath, outputPath, context);
        // ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);
    }

    // Excel 设置公式(函数、求和等)
    @Test
    public void test_3() throws IOException {
        // 生成示例数据
        List<Employee> employees = generateSampleEmployeeData();

        // 创建上下文
        Context context = new Context();
        context.putVar("employees", employees);
        context.putVar("bonus", 0.1);

        // 指定模板路径和输出路径
        String templatePath = "templates/template_formulas_设置公式.xls";
        String outputPath = "target/output3.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcel(templatePath, outputPath, context);
        // ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);

    }

    // Excel 设置公式(自定义工具、函数、求和等)
    @Test
    public void test_4() throws IOException {

        Context context = new Context();
        // context.putVar("dateFormatter", "yyyy-MM-dd");
        context.putVar("dateFormatter", "yyyy/MM/dd");
        context.putVar("dateTimeFormatter", "yyyy-MM-dd HH:mm:ss");

        // 测试工具类处理时间
        context.putVar("newDate", new Date());
        context.putVar("newLocalDate", LocalDateTime.now());
        context.putVar("stringDate", "2023-01-05");
        context.putVar("timeStamp", 1672800485279L);
        context.putVar("currentTimeMillis", System.currentTimeMillis());

        // 指定模板路径和输出路径
        String templatePath = "templates/template_date_自定义工具.xlsx";
        String outputPath = "target/output4.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);
    }

    // Excel 设置函数（设置公式函数、求和等）
    @Test
    public void test_5() throws IOException {

        Map<String, Object> data = new HashMap<>();
        data.put("money1", 80);
        data.put("money2", 20);

        // 创建上下文
        Context context = new Context(data);
        // context.putVar("money1", 80);
        // context.putVar("money2", 20);

        // 指定模板路径和输出路径
        String templatePath = "templates/template_function_设置函数.xlsx";
        String outputPath = "target/output5.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcel(templatePath, outputPath, context);
        // ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);
    }

    // Excel 根据条件合并单元格
    @Test
    public void test_6() throws IOException {
        // 生成示例数据
        List<Employee> employees = generateSampleEmployeeData();
        Company company1 = new Company("Company Name1", "Department Name1", employees);
        Company company2 = new Company("Company Name2", "Department Name2", employees);

        List<Company> companyList = new ArrayList<>();
        companyList.add(company1);
        companyList.add(company2);

        // 创建上下文
        Context context = new Context();
        context.putVar("companyList", companyList);

        // 指定模板路径和输出路径
        String templatePath = "templates/template_data_合并单元格.xlsx";
        String outputPath = "target/output6.xlsx";

        // 生成Excel文件
        ExcelJxlsUtils.generateExcel(templatePath, outputPath, context);
        // ExcelJxlsUtils.generateExcelWithUtils(templatePath, outputPath, context);
    }

    // Excel 设置样式
    @Test
    public void test_7() {

    }

    @Test
    public void test_8() {

    }

    @Test
    public void test_9() {

    }

    // -------------以下是 poi 功能-------------

    @Test
    public void test_10() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        List<Sheet> sheets = ExcelJxlsUtils.getAllSheets(workbook);
        for (Sheet sheet : sheets) {
            System.out.println("Sheet Name: " + sheet.getSheetName());
        }

    }

    @Test
    public void test_11() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        ExcelJxlsUtils.copySheet(workbook, "Sheet1", "CopyOfSheet1");
        ExcelJxlsUtils.saveWorkbookToFile(workbook, "output.xlsx");

    }

    @Test
    public void test_12() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        ExcelJxlsUtils.insertRow(workbook, "Sheet1", 1);
        ExcelJxlsUtils.saveWorkbookToFile(workbook, "output.xlsx");

    }

    @Test
    public void test_13() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        ExcelJxlsUtils.deleteRow(workbook, "Sheet1", 1);
        ExcelJxlsUtils.saveWorkbookToFile(workbook, "output.xlsx");

    }

    @Test
    public void test_14() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        ExcelJxlsUtils.insertColumn(workbook, "Sheet1", 1);
        ExcelJxlsUtils.saveWorkbookToFile(workbook, "output.xlsx");

    }

    @Test
    public void test_15() throws IOException {
        Workbook workbook = ExcelJxlsUtils.readWorkbook("input.xlsx");
        ExcelJxlsUtils.deleteColumn(workbook, "Sheet1", 1);
        ExcelJxlsUtils.saveWorkbookToFile(workbook, "output.xlsx");

    }
}
