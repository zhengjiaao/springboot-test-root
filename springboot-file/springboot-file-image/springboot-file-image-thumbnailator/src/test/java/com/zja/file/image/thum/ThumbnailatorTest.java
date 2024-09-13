package com.zja.file.image.thum;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

    /**
     * 批量处理缩略图-生成缩略图（按等比缩放）
     * <p>
     * 示例：1.jpeg --> thumbnail.1.jpeg
     * </p>
     */
    @Test
    public void test_1() throws IOException {
        // 生成缩略图（按等比缩放）
        Thumbnails.of(new File(image_Dir).listFiles())
                .size(640, 480)
                .outputFormat("jpg")
                .toFiles(Rename.PREFIX_DOT_THUMBNAIL);

        // 处理后的缩略图输出到指定文件夹，使用原来的名称
        Thumbnails.of(image_filePath, image_filePath2)
                .size(200, 200)
                .toFiles(new File("target"), Rename.NO_CHANGE);
    }

    /**
     * 生成缩略图（不按等比缩放）
     */
    @Test
    public void test_2() throws IOException {
        // 生成缩略图（不按等比缩放）
        Thumbnails.of(new File(image_filePath))
                //设置缩略图大小，按等比缩放
                .size(200, 200)
                //将生成的缩略图写入文件
                .toFile(new File("target\\thumbnail_2.jpg"));
    }

    /**
     * 按比例缩放图片
     *
     * @throws IOException
     */
    @Test
    public void test_3() throws IOException {
        // 按比例缩放图片
        Thumbnails.of(new File(image_filePath))
                //缩小50%
                .scale(0.5)
                //将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_3.jpg"));
    }


    @Test
    public void test_4() throws IOException {
        // 缩放并旋转图片
        Thumbnails.of(new File(image_filePath))
                .size(300, 300)
                //旋转180度
                .rotate(180)
                //将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_4.jpg"));
    }

    //缩放图片并添加水印
    @Test
    public void test_5() throws IOException {
        //水印图片
        BufferedImage watermarkImage = ImageIO.read(new File("f:\\watermark.jpg"));

        Thumbnails.of(new File(image_filePath))
                .size(500, 500)
                //添加水印
                //watermark参数1：表示水印位置，Positions枚举类中预定义了一些常用的位置
                //watermark参数2：水印图片
                //watermark参数3：水印的不透明度
                .watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.8f)
                //将生成的缩略图写入文件
                .toFile(new File("target/thumbnail_5.jpg"));
    }

    // 图片裁剪
    @Test
    public void test_6() throws IOException {
        Thumbnails.of(new File(image_filePath))
                //裁剪大小
                .size(200, 200)
                //裁剪位置
                .crop(Positions.CENTER)
                .toFile(new File("target/thumbnail_6.jpg"));
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
}
