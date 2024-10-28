package com.zja.file.excel.poi;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 13:50
 */
public class ExcelImageTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateImageExcel() throws IOException {
        // 创建临时文件
        File tempFile = tempDir.resolve("image_workbook.xlsx").toFile();
        File imageFile = new File("D:\\temp\\images\\test.png"); // 替换为实际图片路径

        // 调用方法创建带图片的 Excel 文件
        createImageExcel(tempFile, imageFile);

        // 验证文件是否存在
        assertTrue(tempFile.exists(), "文件应存在");

        // 验证文件大小是否大于0
        assertTrue(tempFile.length() > 0, "文件应有内容");

        // 读取文件
        try (FileInputStream fileIn = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            // 获取绘图对象
            XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();

            // 过滤出图片
            long pictureCount = drawing.getShapes().stream().filter(shape -> shape instanceof org.apache.poi.xssf.usermodel.XSSFPicture).count();

            // 验证图片数量
            assertEquals(1, pictureCount, "应有一张图片");

            // 关闭工作簿
            workbook.close();
        }
    }

    public static void createImageExcel(File file, File imageFile) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个工作表
        Sheet sheet = workbook.createSheet("图片示例");

        // 读取图片
        byte[] bytes = IOUtils.toByteArray(Files.newInputStream(imageFile.toPath()));
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

        // 创建绘图对象
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 创建锚点
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 1, 1);

        // 插入图片
        drawing.createPicture(anchor, pictureIdx);

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();
    }
}
