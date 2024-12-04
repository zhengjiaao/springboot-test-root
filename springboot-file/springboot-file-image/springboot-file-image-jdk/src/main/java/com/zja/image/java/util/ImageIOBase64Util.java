package com.zja.image.java.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-12-04 10:00
 */
public class ImageIOBase64Util {

    // 图片转为 Base64
    public static String imageToBase64(String imagePath, String format) throws IOException {
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
            return Base64.encodeBase64String(imageBytes);
        }
    }

    // Base64转为 图片
    public static void base64ToImage(String base64, String imagePath, String format) throws IOException {
        if (base64 == null || base64.isEmpty() || imagePath == null || imagePath.isEmpty() || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("Base64 string, image path, and format must not be null or empty");
        }

        BufferedImage bufferedImage = base64ToBufferedImage(base64);
        if (!ImageIO.write(bufferedImage, format, new File(imagePath))) {
            throw new IOException("Failed to write image to file: " + imagePath);
        }
    }

    // BufferedImage 转为 base64
    public static String bufferedImageToBase64(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (!ImageIO.write(image, format, baos)) {
                throw new IOException("Failed to write image to ByteArrayOutputStream");
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeBase64String(imageBytes);
        }
    }

    // base64 转为 BufferedImage
    public static BufferedImage base64ToBufferedImage(String base64) throws IOException {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Base64 string is null or empty");
        }

        // Convert the Base64 string to a byte array
        byte[] base64ByteArray = getBase64ByteArray(base64);
        if (base64ByteArray == null) {
            throw new IOException("Failed to decode Base64 string");
        }

        // Decode the Base64 string to a BufferedImage
        try (ByteArrayInputStream bais = new ByteArrayInputStream(base64ByteArray)) {
            BufferedImage image = ImageIO.read(bais);
            if (image == null) {
                throw new IOException("Failed to read image from ByteArrayInputStream");
            }
            return image;
        }
    }

    public static byte[] getBase64ByteArray(String base64) {
        String encodingPrefix = "base64,";
        if (base64.contains(encodingPrefix)) {
            // Remove any data URI scheme prefix (e.g., "data:image/png;base64,")
            int contentStartIndex = base64.indexOf(encodingPrefix) + encodingPrefix.length();
            base64 = base64.substring(contentStartIndex);
        }

        boolean isBase64 = Base64.isBase64(base64);
        return isBase64 ? Base64.decodeBase64(base64) : null;
    }
}
