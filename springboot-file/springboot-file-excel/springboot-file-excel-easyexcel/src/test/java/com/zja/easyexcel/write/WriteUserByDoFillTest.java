package com.zja.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.zja.easyexcel.model.DepartmentSummary;
import com.zja.easyexcel.model.UserDTO;
import com.zja.easyexcel.model.UserDTOMockData;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 写方法：doFill、fill 用来填充模版，按占位符，必须要使用模版
 *
 * <p>
 * 预先设计好的 Excel 模板文件（包含特定的格式、公式、占位符等）
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-09-16 19:10
 */
public class WriteUserByDoFillTest {

    // 填充单个对象数据(注意，@ExcelProperty 注解方式不在生效)
    @Test
    public void writeSingleData() throws Exception {
        // 模板文件路径
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_占位符.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doFill/write_user_template_占位符.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 获取模拟数据
        UserDTO userDTO = UserDTOMockData.getUserDTO();

        // 使用 doFill 填充模板
        EasyExcel.write(outputPath)
                // .withTemplate(templatePath) // 指定模板
                .withTemplate(templateInputStream) // 指定模板
                .sheet() // 默认使用模板的第一个 sheet
                .doFill(userDTO); // 执行填充
    }

    // 复杂方式-使用fill方法, 必须依赖模板
    @Test
    public void writeDataByTemplateWithAnnotations2() throws Exception {
        // 模板文件路径
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_占位符.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doFill/write_user_template_占位符.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 获取模拟数据
        UserDTO userDTO = UserDTOMockData.getUserDTO();

        // 验证数据不为空
        if (userDTO == null) {
            throw new IllegalStateException("用户数据不能为空");
        }

        // 使用复杂填充方式
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputPath)
                    .withTemplate(templateInputStream)
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
            // 使用fill方法而不是write方法。
            excelWriter.fill(userDTO, writeSheet); // fill方法 必须使用模版

            System.out.println("文件已输出到: " + outputPath);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            templateInputStream.close();
        }
    }


    // 填充单个对象数据-Map
    @Test
    public void writeSingleDataByMap() throws Exception {
        // 模板文件路径
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_占位符.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doFill/write_user_template_占位符.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 准备填充数据 (可以是 Map 或 JavaBean)
        Map<String, Object> data = new HashMap<>();
        data.put("userId", "001");
        data.put("userName", "张三66");
        data.put("userPhone", "15838888992");
        data.put("age", 30);
        data.put("email", "zhangsan@example.com");
        data.put("index", 1);

        // 使用 doFill 填充模板
        EasyExcel.write(outputPath)
                // .withTemplate(templatePath) // 指定模板
                .withTemplate(templateInputStream) // 指定模板
                .sheet() // 默认使用模板的第一个 sheet
                .doFill(data); // 执行填充
    }

    @Test
    public void writeListData() throws Exception {
        // 模板文件路径
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_占位符_列表.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doFill/write_user_template_占位符_列表.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList(); // 获取用户列表数据

        // 先填充单个数据
        EasyExcel.write(outputPath)
                // .withTemplate(templatePath)
                .withTemplate(templateInputStream)
                .sheet()
                .doFill(userDTOList); // 直接填充列表可能不适用于所有模板结构，注意：对于复杂填充，官方推荐使用 ExcelWriter 来更精确控制

        // 再填充列表数据 (需要使用 ExcelWriter)
        // 注意：对于复杂填充，官方推荐使用 ExcelWriter 来更精确控制
        // 这里是简化示例，实际可能需要更复杂的逻辑来处理多数据源
        // EasyExcel.write(outputPath).withTemplate(templatePath).sheet().doFill(userDataList); // 直接填充列表可能不适用于所有模板结构
        // 更推荐的方式是参考官方文档的 FillWrapper 用法
    }

    // 复杂方式-使用ExcelWriter fill方法, 必须依赖模板
    @Test
    public void writeDataByTemplateWithExcelWriter() throws Exception {
        // 模板文件路径
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_占位符_多列表.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doFill/write_user_template_占位符_多列表.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 1. 准备数据
        // 单个数据
        Map<String, Object> singleData = new HashMap<>();
        singleData.put("reportDate", "2025-09-16");

        // 部门A员工列表
        List<Map<String, Object>> deptAList = new ArrayList<>();
        deptAList.add(new HashMap<String, Object>() {{
            put("userId", 1);
            put("userName", "张三");
            put("userPhone", "1583888736");
        }});
        deptAList.add(new HashMap<String, Object>() {{
            put("userId", 2);
            put("userName", "李四");
            put("userPhone", "1583888732");
        }});
        // 或者使用 List<Employee> 并在模板中用 {deptA.字段名}

        // 部门B员工列表
        List<UserDTO> deptBList = new ArrayList<>();
        deptBList.add(UserDTOMockData.getUserDTO());
        deptBList.add(UserDTOMockData.getUserDTO());

        // 部门汇总列表
        List<DepartmentSummary> summaryList = Arrays.asList(
                new DepartmentSummary("部门A", 2),
                new DepartmentSummary("部门B", 2)
        );

        // 2. 使用 ExcelWriter 进行复杂填充
        ExcelWriter excelWriter = null;
        try {
            // 创建 ExcelWriter，指定模板
            excelWriter = EasyExcel.write(outputPath)
                    // .withTemplate(templatePath)
                    .withTemplate(templateInputStream)
                    .build();

            // 获取第一个 Sheet (模板中的 Sheet)
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            // 创建 FillConfig，设置 forceNewRow 为 true，确保列表填充时即使模板只有一行也能正确填充多行
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 填充单个数据
            excelWriter.fill(singleData, writeSheet);

            // 填充部门A列表
            // 使用 FillWrapper 包装数据，指定前缀 "deptA"
            excelWriter.fill(new FillWrapper("deptA", deptAList), fillConfig, writeSheet);

            // 填充部门B列表
            // 使用 FillWrapper 包装数据，指定前缀 "deptB"
            excelWriter.fill(new FillWrapper("deptB", deptBList), fillConfig, writeSheet);

            // 填充汇总列表
            // 使用 FillWrapper 包装数据，指定前缀 "summary"
            excelWriter.fill(new FillWrapper("summary", summaryList), fillConfig, writeSheet);

        } finally {
            // 关闭 writer，非常重要，会刷新数据到文件
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        System.out.println("复杂模板填充完成，文件已生成: " + outputPath);
    }

    // 从resources目录下读取模板文件
    private InputStream getTemplateInputStream(String templatePath) throws IOException {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        return templateResource.getInputStream();
    }

    /**
     * 获取target目录下的文件路径
     *
     * @param fileName 文件名
     * @return target目录下的完整路径
     */
    private String getTargetPath(String fileName) throws Exception {
        // 获取target目录
        File targetDir = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile();
        // 确保target目录存在
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 返回文件路径
        return new File(targetDir, fileName).getAbsolutePath();
    }
}
