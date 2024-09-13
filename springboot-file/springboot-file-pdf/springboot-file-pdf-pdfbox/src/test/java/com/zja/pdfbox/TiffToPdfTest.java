package com.zja.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * @Author: zhengja
 * @Date: 2024-09-12 11:30
 */
public class TiffToPdfTest {

    // 单页
    static final String tif_filePath = Paths.get("D:\\temp\\tif\\test", "区位小图.tif").toString();

    //多页
    static final String tif_multiPage_filePath = Paths.get("D:\\temp\\tif\\multipage", "multipage_image.tif").toString();

    static final String pdf_outputPath = Paths.get("target", "output.pdf").toString();

    /**
     * 将多个 TIFF 图片转换为 JPEG 并输出到一个 PDF 文件中
     */
    @Test
    public void tiff_to_pdf_test() {
        try {
            // 输入TIFF文件路径
            File tiffFile = new File(tif_filePath);
//            File tiffFile = new File(tif_multiPage_filePath);

            // 创建 PDF 文件
            PDDocument document = new PDDocument();

            // 获取TIFF ImageReader
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("tiff");
            if (!readers.hasNext()) {
                throw new RuntimeException("No TIFF ImageReader available.");
            }
            ImageReader reader = readers.next();

            // 打开输入流
            try (FileInputStream fis = new FileInputStream(tiffFile);
                 ImageInputStream iis = ImageIO.createImageInputStream(fis)) {
                reader.setInput(iis);

                // 获取总页数
                int pageCount = reader.getNumImages(true);
                System.out.println("Total Pages: " + pageCount);

                // 循环读取每一页并保存为 JPEG
                for (int i = 0; i < pageCount; i++) {
                    System.out.println("Processing Page: " + i);
                    BufferedImage page = reader.read(i);

                    // 检查图像是否有效
                    if (page == null || page.getWidth() <= 0 || page.getHeight() <= 0) {
                        throw new IOException("Invalid image data on page " + i);
                    }

                    // 保存为临时 JPEG 文件
                    File tempFile = new File("temp_" + i + ".jpg");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        boolean success = ImageIO.write(page, "jpg", fos);
                        if (!success) {
                            throw new IOException("Failed to write image as JPEG.");
                        }
                    }

                    // 将 JPEG 图片插入到 PDF 中
                    PDImageXObject pdImage = PDImageXObject.createFromFile(tempFile.getAbsolutePath(), document);
                    PDPage pagePdf = new PDPage(PDRectangle.A4);
                    document.addPage(pagePdf);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, pagePdf)) {
                        contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
                    }

                    // 删除临时文件
                    tempFile.delete();
                }
            }

            // 保存 PDF 文件
            document.save(pdf_outputPath);
            document.close();

            System.out.println("PDF 文件生成成功！");
        } catch (IOException e) {
            System.err.println("图像转换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
