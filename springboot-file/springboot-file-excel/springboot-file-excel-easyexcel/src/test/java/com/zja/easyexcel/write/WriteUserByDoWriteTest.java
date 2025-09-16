package com.zja.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zja.easyexcel.model.UserDTO;
import com.zja.easyexcel.model.UserDTOMockData;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 写方法：doWrite、write 用来写数据，不需要使用模版，也可以使用模版，但不能用来按占位符等填充数据
 *
 * <p>
 * write 方法，不依赖模版，写入方式就是在最后一行进行追加数据
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-09-15 17:54
 */
public class WriteUserByDoWriteTest {

    // 采用注解方式：写入列表数据-可选模板
    @Test
    public void writeListData() throws Exception {
        // 从自定义目录下读取模板文件
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_doWrite.xlsx";
        // String templatePath = "D:\\temp\\excel\\template\\write_user_template_空的.xlsx";
        // String templatePath = "D:\\temp\\excel\\template\\write_user_template_带数据.xlsx";

        // InputStream templateInputStream = getTemplateInputStream("template/doWrite/write_user_template_空的.xlsx");
        InputStream templateInputStream = getTemplateInputStream("template/doWrite/write_user_template_带数据.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList();

        // 方式一：简单填充（适用于列表数据）
        EasyExcel.write(outputPath, UserDTO.class)
                // .withTemplate(templateInputStream) // 模版，是可选地
                // .withTemplate(templatePath) // 模版，可选地
                .sheet()
                // .sheet("Sheet1")  // 指定sheet名称
                // .sheet(0)        // 指定sheet索引（从0开始）
                // .excludeColumnFieldNames(getAllFieldNames()) // 排除字段，这样就不会写入此列数据
                // .head(Collections.emptyList()) // 设置空的表头，这样就不会有表头
                // .relativeHeadRowIndex(5) // 从第5行开始写入表头和数据，在指定位置追加数据
                .doWrite(userDTOList); //  追加数据，doWrite 模版是可选地
        // .doFill(userDTOList); //  填充数据，doFill 必须使用模版

        templateInputStream.close();
        System.out.println("文件已输出到: " + outputPath);
    }

    // 获取UserDTO类的所有字段名
    private Set<String> getAllFieldNames() {
        Set<String> fieldNames = new HashSet<>();
        // fieldNames.add("userId");
        fieldNames.add("userName");
        fieldNames.add("userPhone");
        fieldNames.add("userEmail");
        fieldNames.add("userAge");
        fieldNames.add("userAddress");
        // 添加UserDTO类中的所有字段名
        return fieldNames;
    }

    // 复杂方式 - 使用write方法，可不依赖模板
    @Test
    public void writeDataWithoutTemplate() throws Exception {
        String templatePath = "D:\\temp\\excel\\template\\write_user_template_空的.xlsx";
        InputStream templateInputStream = getTemplateInputStream("template/doWrite/write_user_template_空的.xlsx");

        // 输出文件到target目录
        String outputPath = getTargetPath("output_" + FilenameUtils.getName(templatePath));

        // 获取模拟数据
        UserDTO userDTO = UserDTOMockData.getUserDTO();

        // 验证数据不为空
        if (userDTO == null) {
            throw new IllegalStateException("用户数据不能为空");
        }

        // 使用write方式不依赖模板
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputPath, UserDTO.class)
                    // .withTemplate(templatePath) // 可选地
                    // .withTemplate(templateInputStream) // 可选地
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet("用户数据")
                    .build();

            // 使用write方法而不是fill方法
            excelWriter.write(Collections.singletonList(userDTO), writeSheet); // write方法，可不依赖模板

            System.out.println("文件已输出到: " + outputPath);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    // 使用 ExcelWriter 和 WriteSheet 精确控制写入位置
    @Test
    public void writeDataAtExactPosition() throws Exception {
        String outputPath = getTargetPath("output_exact_position.xlsx");

        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList();

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputPath, UserDTO.class).build();

            // 创建sheet并指定起始行
            WriteSheet writeSheet = EasyExcel.writerSheet("用户数据")
                    .relativeHeadRowIndex(3) // 从第3行开始写入
                    .build();

            // 写入数据
            excelWriter.write(userDTOList, writeSheet);

            System.out.println("数据已精确写入指定位置: " + outputPath);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    // 在已有数据的Excel文件中追加数据
    @Test
    public void appendDataToExistingFile() throws Exception {
        // 首先创建一个包含已有数据的文件
        String existingFilePath = getTargetPath("existing_data.xlsx");
        List<UserDTO> existingData = UserDTOMockData.getUserDTOList().subList(0, 3);

        // 写入初始数据
        EasyExcel.write(existingFilePath, UserDTO.class)
                .sheet("用户数据")
                .doWrite(existingData);

        // 然后在该文件中追加新数据
        String appendFilePath = existingFilePath;
        List<UserDTO> newData = UserDTOMockData.getUserDTOList().subList(1, 3);

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(appendFilePath, UserDTO.class).build();

            // 指定从已有数据之后的行开始写入
            WriteSheet writeSheet = EasyExcel.writerSheet("用户数据")
                    .relativeHeadRowIndex(existingData.size() + 2) // 跳过已有数据和空行
                    .build();

            // 追加新数据
            excelWriter.write(newData, writeSheet);

            System.out.println("数据已追加到现有文件: " + appendFilePath);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
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
