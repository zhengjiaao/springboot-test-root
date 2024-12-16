package com.zja.file.image.thum.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-12-16 14:26
 */
public class BufferedImageUtil {

    /**
     * 图片叠加
     *
     * @param baseMapImage
     * @param topImage
     * @param width
     * @return
     * @throws IOException
     */
    public static BufferedImage combinedImage(BufferedImage baseMapImage, BufferedImage topImage, int width) throws IOException {
        // 计算第二张图像缩放到第一张图像的十分之一大小
        int newWidth = baseMapImage.getWidth() / width;
        BufferedImage resizedImage2 = Thumbnails.of(topImage)
                .width(newWidth)
                .keepAspectRatio(true)
                .asBufferedImage();

        int newHeight = resizedImage2.getHeight();

        // 创建一个新的图像，大小与第一张图像相同
        BufferedImage combinedImage = new BufferedImage(baseMapImage.getWidth(), baseMapImage.getHeight(), baseMapImage.getType());
        Graphics2D g2d = combinedImage.createGraphics();

        // 绘制第一张图像
        g2d.drawImage(baseMapImage, 0, 0, null);
        // 绘制缩放后的第二张图像到右下角
        g2d.drawImage(resizedImage2, baseMapImage.getWidth() - newWidth, baseMapImage.getHeight() - newHeight, null);

        // 释放资源
        g2d.dispose();

        return combinedImage;
    }
}
