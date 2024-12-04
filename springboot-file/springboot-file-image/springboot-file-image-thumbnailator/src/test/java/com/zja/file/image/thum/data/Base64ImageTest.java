package com.zja.file.image.thum.data;

import com.zja.file.image.thum.util.ImageIOUtil;
import com.zja.file.image.thum.util.ResourcesFileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-12-04 9:57
 */
public class Base64ImageTest {
    private void printlnWidthAndHeight(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        System.out.println("Width: " + image.getWidth());
        System.out.println("Height: " + image.getHeight());
    }

    private void printlnWidthAndHeight(BufferedImage image) {
        System.out.println("Width: " + image.getWidth());
        System.out.println("Height: " + image.getHeight());
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_1() throws IOException {
        String base64Image = ResourcesFileUtil.readFileByUTF8("data/base64_image_1.txt");
        // System.out.println(base64Image);

        BufferedImage bufferedImage = ImageIOUtil.base64ToBufferedImage(base64Image);
        printlnWidthAndHeight(bufferedImage);

        // 生成缩略图（按等比缩放）
        int targetWidth = 500;
        BufferedImage resizedImage = Thumbnails.of(bufferedImage)
                .width(targetWidth)
                .keepAspectRatio(true) // 保持纵横比
                .asBufferedImage();

        printlnWidthAndHeight(resizedImage);

        // 输出为文件
        ImageIO.write(resizedImage, "png", new File("target\\base64_image_1.png"));

        printlnWidthAndHeight("target\\base64_image_1.png");
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_2() throws IOException {
        String base64Image = ResourcesFileUtil.readFileByUTF8("data/base64_image_1.txt");
        // System.out.println(base64Image);

        BufferedImage bufferedImage = ImageIOUtil.base64ToBufferedImage(base64Image);
        printlnWidthAndHeight(bufferedImage);

        ImageIOUtil.base64ToImage(base64Image, "target\\base64_image_2.png", "png");

        printlnWidthAndHeight("target\\base64_image_2.png");
    }

    /**
     * 生成缩略图（按等比缩放）
     */
    @Test
    public void test_3() throws IOException {
        String base64 = ImageIOUtil.imageToBase64("target\\base64_image_2.png", "png");
        System.out.println(base64);

        BufferedImage bufferedImage = ImageIOUtil.base64ToBufferedImage(base64);
        printlnWidthAndHeight(bufferedImage);

        ImageIO.write(bufferedImage, "png", new File("target\\base64_image_3.png"));
    }
}
