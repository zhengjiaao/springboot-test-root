package com.zja.file.image.thum;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 生成缩略图
 *
 * <p>
 * 参考：https://www.cnblogs.com/seve/p/14559250.html
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-09-12 17:05
 */
public class ThumbnailatorTest {

    // 待处理图片
    static final String image_Dir = Paths.get("D:\\temp\\images\\风景").toString();
    static final String image_outputDir = Paths.get("D:\\temp\\images\\output").toString();
    static final String image_filePath = Paths.get("D:\\temp\\images\\风景", "1.jpeg").toString();
    static final String image_filePath2 = Paths.get("D:\\temp\\images\\风景", "2.jpeg").toString();

    private void printlnWidthAndHeight(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        System.out.println("Width: " + image.getWidth());
        System.out.println("Height: " + image.getHeight());
    }

    /**
     * 批量处理缩略图-生成缩略图（按等比缩放）
     * <p>
     * 示例：1.jpeg --> thumbnail.1.jpeg
     * </p>
     */
    @Test
    public void test_1() throws IOException {
        // 生成缩略图（按等比缩放）
        Thumbnails.of(new File(image_Dir).listFiles()).size(640, 480) // 生成缩略图（按等比缩放）
                .outputFormat("jpg").toFiles(Rename.PREFIX_DOT_THUMBNAIL);

        // 处理后的缩略图输出到指定文件夹，使用原来的名称
        // Thumbnails.of(image_filePath, image_filePath2)
        //         .size(200, 200)
        //         .toFiles(new File("target"), Rename.NO_CHANGE);
    }

    /**
     * 批量处理缩略图-生成缩略图（按等比缩放）
     * <p>
     * 示例：1.jpeg --> thumbnail.1.jpeg
     * </p>
     */
    @Test
    public void test_1_1() throws IOException {
        printlnWidthAndHeight(image_filePath);
        printlnWidthAndHeight(image_filePath2);

        // 处理后的缩略图输出到指定文件夹，使用原来的名称
        Thumbnails.of(image_filePath, image_filePath2).size(200, 200) // 生成缩略图（按等比缩放）
                .toFiles(new File("target"), Rename.NO_CHANGE);

        printlnWidthAndHeight("target\\1.jpeg");
        printlnWidthAndHeight("target\\2.jpeg");
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_2() throws IOException {
        printlnWidthAndHeight(image_filePath);

        // 生成缩略图（按等比缩放）
        Thumbnails.of(new File(image_filePath))
                // 设置缩略图大小，按等比缩放
                .size(200, 200)
                // 将生成的缩略图写入文件
                .toFile(new File("target\\thumbnail_2.jpg"));


        int targetWidth = 520;
        // Resize the image using Thumbnailator
        BufferedImage resizedImage = Thumbnails.of(image_filePath).width(targetWidth).keepAspectRatio(true) // Maintain aspect ratio
                .asBufferedImage();

        printlnWidthAndHeight("target\\thumbnail_2.jpg");
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_2_1() throws IOException {
        printlnWidthAndHeight(image_filePath);

        // 生成缩略图（按等比缩放）
        int targetWidth = 500;
        BufferedImage resizedImage = Thumbnails.of(image_filePath).width(targetWidth).keepAspectRatio(true) // 保持纵横比
                .asBufferedImage();

        // 输出为文件
        ImageIO.write(resizedImage, "jpeg", new File("target\\thumbnail_2_1.jpeg"));

        printlnWidthAndHeight("target\\thumbnail_2_1.jpeg");
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_2_2() throws IOException {
        printlnWidthAndHeight(image_filePath);

        // imageToBase64
        String base64Image = imageToBase64(image_filePath, "jpeg");

        // 生成缩略图（按等比缩放）
        int targetWidth = 500;
        String base64Image2 = resizeImage(base64Image, targetWidth);

        // base64ToImage
        base64ToImage(base64Image2, "target\\thumbnail_2_2.jpeg","jpeg");

        printlnWidthAndHeight("target\\thumbnail_2_2.jpeg");
    }

    private String resizeImage(String base64Image, int targetWidth) throws IOException {
        BufferedImage originalImage = base64ToBufferedImage(base64Image);

        // Resize the image using Thumbnailator
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .width(targetWidth)
                .keepAspectRatio(true) // Maintain aspect ratio
                .asBufferedImage();

        return bufferedImageToBase64(resizedImage, "jpeg");
    }

    // 图片转为 Base64
    private String imageToBase64(String imagePath, String format) throws IOException {
        if (imagePath == null || imagePath.isEmpty() || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("Image path and format must not be null or empty");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 10)) {
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image == null) {
                throw new IOException("Failed to read image from file: " + imagePath);
            }
            if (!ImageIO.write(image, format, baos)) {
                throw new IOException("Failed to write image to ByteArrayOutputStream");
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    // Base64转为 图片
    private void base64ToImage(String base64, String imagePath, String format) throws IOException {
        if (base64 == null || base64.isEmpty() || imagePath == null || imagePath.isEmpty() || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("Base64 string, image path, and format must not be null or empty");
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(base64))) {
            BufferedImage bufferedImage = ImageIO.read(bais);
            if (bufferedImage == null) {
                throw new IOException("Failed to read image from ByteArrayInputStream");
            }
            if (!ImageIO.write(bufferedImage, format, new File(imagePath))) {
                throw new IOException("Failed to write image to file: " + imagePath);
            }
        }
    }

    // BufferedImage 转为 base64
    private String bufferedImageToBase64(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (!ImageIO.write(image, format, baos)) {
                throw new IOException("Failed to write image to ByteArrayOutputStream");
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    // base64 转为 BufferedImage
    private BufferedImage base64ToBufferedImage(String base64) throws IOException {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Base64 string is null or empty");
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(base64))) {
            BufferedImage image = ImageIO.read(bais);
            if (image == null) {
                throw new IOException("Failed to read image from ByteArrayInputStream");
            }
            return image;
        }
    }

    /**
     * 按比例缩放图片
     *
     * @throws IOException
     */
    @Test
    public void test_3() throws IOException {
        printlnWidthAndHeight(image_filePath);

        // 按比例缩放图片
        Thumbnails.of(new File(image_filePath))
                // 缩小50%
                .scale(0.5)
                // 将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_3.jpg"));

        printlnWidthAndHeight("target\\thumbnail_3.jpg");
    }


    @Test
    public void test_4() throws IOException {
        // 缩放并旋转图片
        Thumbnails.of(new File(image_filePath)).size(300, 300)
                // 旋转180度
                .rotate(180)
                // 将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_4.jpg"));
    }

    // 缩放图片并添加水印
    @Test
    public void test_5() throws IOException {
        // 水印图片
        BufferedImage watermarkImage = ImageIO.read(new File("f:\\watermark.jpg"));

        Thumbnails.of(new File(image_filePath)).size(500, 500)
                // 添加水印
                // watermark参数1：表示水印位置，Positions枚举类中预定义了一些常用的位置
                // watermark参数2：水印图片
                // watermark参数3：水印的不透明度
                .watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.8f)
                // 将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_5.jpg"));
    }

    // 图片裁剪
    @Test
    public void test_6() throws IOException {
        Thumbnails.of(new File(image_filePath))
                // 裁剪大小
                .size(200, 200)
                // 裁剪位置
                .crop(Positions.CENTER).toFile(new File("target/thumbnail_6.jpg"));
    }

    @Test
    public void test_10() throws IOException {
        try {
            resizeImage(image_filePath, "target\\thumbnail_10.jpg", 10000, 16667);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resizeImage(String inputFile, String outputFile, int targetWidth, int targetHeight) throws IOException {
        File input = new File(inputFile);
        BufferedImage image = ImageIO.read(input);

        // 计算缩放比例
        double scaleX = (double) targetWidth / image.getWidth();
        double scaleY = (double) targetHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        // 计算新的宽度和高度
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);

        // 创建一个新的缩放后的图片
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
        resizedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);

        // 写入新的图片到文件
        ImageIO.write(resizedImage, "jpg", new File(outputFile));
    }

    // 输出图片的宽高
    @Test
    public void test_11() throws IOException {
        BufferedImage image = ImageIO.read(new File(image_filePath));
        System.out.println("Width: " + image.getWidth());
        System.out.println("Height: " + image.getHeight());
    }

    // 加载两张图像，将第二张图像缩放到第一张图像的十分之一大小，然后将其叠加到第一张图像的右下角，并保存为新的图像文件。
    @Test
    public void test_12() throws IOException {
        // 加载第一张图像
        BufferedImage image1 = ImageIO.read(new File(image_filePath));
        // 加载第二张图像
        BufferedImage image2 = ImageIO.read(new File(image_filePath2));

        // 计算第二张图像缩放到第一张图像的十分之一大小
        int newWidth = image1.getWidth() / 5;
        // int newHeight = image2.getHeight() / 10;
        BufferedImage resizedImage2 = Thumbnails.of(image2).width(newWidth).keepAspectRatio(true).asBufferedImage();

        int newHeight = resizedImage2.getHeight();

        // 创建一个新的图像，大小与第一张图像相同
        BufferedImage combinedImage = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());
        Graphics2D g2d = combinedImage.createGraphics();

        // 绘制第一张图像
        g2d.drawImage(image1, 0, 0, null);
        // 绘制缩放后的第二张图像到右下角
        // g2d.drawImage(resizedImage2, image1.getWidth() - newWidth, image1.getHeight() - newHeight, null);
        g2d.drawImage(resizedImage2, image1.getWidth() - newWidth, image1.getHeight() - newHeight, null);

        // 释放资源
        g2d.dispose();

        // 保存合并后的图像到新的文件
        ImageIO.write(combinedImage, "jpg", new File("target/thumbnail_12.jpg"));
    }

}
