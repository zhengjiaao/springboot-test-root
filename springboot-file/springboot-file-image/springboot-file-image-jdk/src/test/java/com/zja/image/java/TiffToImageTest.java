package com.zja.image.java;

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
 * @Date: 2024-09-12 13:51
 */
public class TiffToImageTest {

    static final String tif_filePath = Paths.get("D:\\temp\\tif\\test", "区位小图.tif").toString();

    static final String tif_multiPage_filePath = Paths.get("D:\\temp\\tif\\multipage", "multipage_image.tif").toString();

    static final String jpeg_outputDir = Paths.get("target").toString();

    /**
     * 转换图像到 png
     */
    @Test
    public void tiff_to_image_test() throws Exception {
        try {
            // 输入TIFF文件路径
//            File tiffFile = new File(tif_filePath);
            File tiffFile = new File(tif_multiPage_filePath);

            // 输出目录
            File outputDir = new File(jpeg_outputDir);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

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

                // 循环读取每一页并保存为JPEG
                for (int i = 0; i < pageCount; i++) {
                    System.out.println("Processing Page: " + i);
                    BufferedImage page = reader.read(i);

                    // 检查图像是否有效
                    if (page == null || page.getWidth() <= 0 || page.getHeight() <= 0) {
                        throw new IOException("Invalid image data on page " + i);
                    }

                    // 保存为JPEG
                    File outputFile = new File(outputDir, "output_" + i + ".png");
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        boolean success = ImageIO.write(page, "png", fos);
                        if (!success) {
                            throw new IOException("Failed to write image as png.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("图像转换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
