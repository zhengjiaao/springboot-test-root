package com.zja.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zja.easyexcel.model.UserDTO;
import com.zja.easyexcel.model.UserDTOMockData;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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

    // 第一行合并单元格写入"说明"，然后从第二行开始追加用户数据
    @Test
    public void writeWithMergedHeaderAndAppendData() throws Exception {
        String outputPath = getTargetPath("merged_header_and_data.xlsx");

        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList();

        ExcelWriter excelWriter = null;
        try {
            // 创建ExcelWriter，并添加自定义的合并单元格写入处理器
            excelWriter = EasyExcel.write(outputPath, UserDTO.class)
                    .registerWriteHandler(new MergeAndWriteDescriptionHandler()) // 注册自定义的合并单元格写入处理器
                    .build();

            // 创建sheet，从第二行开始写入表头和数据
            WriteSheet writeSheet = EasyExcel.writerSheet("用户数据")
                    .relativeHeadRowIndex(1) // 从第二行开始写入表头和数据
                    .build();

            // 写入用户数据
            excelWriter.write(userDTOList, writeSheet);

            System.out.println("数据已写入，第一行包含合并单元格的说明文字: " + outputPath);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 在写入开始前就创建合并单元格
     * 自定义写入处理器，用于在第一行合并单元格并写入说明文字
     */
    public static class MergeAndWriteDescriptionHandler implements SheetWriteHandler {

        private final int columnCount;

        public MergeAndWriteDescriptionHandler() {
            this.columnCount = 0; // 默认值
        }

        public MergeAndWriteDescriptionHandler(int columnCount) {
            this.columnCount = columnCount;
        }

        @Override
        public void afterSheetCreate(SheetWriteHandlerContext context) {
            // 在sheet创建后立即处理，此时还没有写入任何数据行
            Sheet sheet = context.getWriteSheetHolder().getSheet();

            // 动态：计算UserDTO的列数，如果构造函数没有传入，则动态计算
            int columns = columnCount > 0 ? columnCount : calculateColumnCount(context);
            System.out.println("列数: " + columns);
            int lastCol = columns > 0 ? columns - 1 : 0; // 最后一列索引，默认为0
            System.out.println("最后一列索引: " + lastCol);

            // 动态：合并第一行的多个单元格（根据实际列数进行合并）
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, lastCol);
            sheet.addMergedRegion(cellRangeAddress);

            // 固定：合并第一行的多个单元格（假设UserDTO有3列，索引0-2）
            // CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 2);
            // sheet.addMergedRegion(cellRangeAddress);

            // 在合并的单元格中写入"说明"文字
            Row row = sheet.getRow(0);
            if (row == null) {
                row = sheet.createRow(0);
            }

            // 设置行高为25
            row.setHeightInPoints(25);

            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }

            // 创建红色字体样式
            Workbook workbook = sheet.getWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(Font.COLOR_RED); // 设置字体为红色
            font.setBold(true); // 可选：设置为粗体
            cellStyle.setFont(font);

            // 设置自动换行
            cellStyle.setWrapText(true);

            // 设置垂直居中对齐
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 可选：设置水平居中对齐
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 应用样式到单元格
            cell.setCellStyle(cellStyle);
            cell.setCellValue("说明：仅支持修改评估值");
        }
    }

    /**
     * 动态计算UserDTO的列数
     * 通过context获取head信息来计算列数
     */
    private static int calculateColumnCount(SheetWriteHandlerContext context) {
        // 尝试从head中获取列数信息
        if (context.getWriteSheetHolder().getExcelWriteHeadProperty() != null) {
            return context.getWriteSheetHolder().getExcelWriteHeadProperty().getHeadMap().size();
        }
        // 默认返回0列
        return 0;
    }

    /**
     * 没生效，原因是：RowWriteHandler 只在写入数据行时触发
     * 自定义写入处理器，用于在第一行合并单元格并写入说明文字
     */
    @Deprecated
    public static class MergeAndWriteDescriptionHandlerV2 implements RowWriteHandler {

        @Override
        public void afterRowDispose(RowWriteHandlerContext context) {
            // 只在第一行处理
            if (context.getRowIndex() == 0) {
                // 获取工作簿和工作表
                Sheet sheet = context.getWriteSheetHolder().getSheet();

                // 合并第一行的多个单元格（假设UserDTO有5列）
                CellRangeAddress cellRangeAddress =
                        new CellRangeAddress(0, 0, 0, 4);
                sheet.addMergedRegion(cellRangeAddress);

                // 在合并的单元格中写入"说明"文字
                Row row = sheet.getRow(0);
                if (row == null) {
                    row = sheet.createRow(0);
                }
                Cell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue("说明");
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
