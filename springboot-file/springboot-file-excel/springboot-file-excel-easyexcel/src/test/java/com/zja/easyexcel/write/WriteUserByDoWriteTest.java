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
import java.util.List;

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
                .doWrite(userDTOList); //  填充数据，doWrite 模版是可选地
        // .doFill(userDTOList); //  填充数据，doFill 必须使用模版

        templateInputStream.close();
        System.out.println("文件已输出到: " + outputPath);
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
