package com.zja.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-09-12 11:19
 */
public class ImageToPDFTest {
    public static void main(String[] args) {
        String imagePath = "D:\\temp\\tif\\test\\output\\output.jpeg"; // 图像文件路径
        String outputPdfPath = "D:\\temp\\tif\\test\\output\\output2.pdf"; // 输出PDF文件路径

        try (PDDocument document = new PDDocument()) {
            // 创建一个A4大小的新页面
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // 创建一个内容流
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // 加载图像
                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

                // 将图像绘制到页面上
                contentStream.drawImage(pdImage, 50, 50, 200, 200); // 绘制位置和大小
            }

            // 保存并关闭PDF文档
            document.save(outputPdfPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
