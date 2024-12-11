package com.zja.apache.codec.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Base64 编码工具类
 * commons-codec 提供的 Base64 功能全面，支持URL安全的Base64编码和解码功能
 *
 * @Author: zhengja
 * @Date: 2024-12-11 16:32
 */
public class Base64Util {

    private static final Logger log = LoggerFactory.getLogger(Base64Util.class);

    private Base64Util() {

    }

    /**
     * Base64 编码
     */
    public static String encoderTextUTF8(String text) {
        return Base64.encodeBase64String(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码
     */
    public static String decoderTextUTF8(String text) {
        return new String(Base64.decodeBase64(text), StandardCharsets.UTF_8);
    }

    // 文本编码
    public static String encodeText(String text) {
        return Base64.encodeBase64String(text.getBytes());
    }

    // 文本解码
    public static String decodeText(String encodedText) {
        byte[] decodedBytes = Base64.decodeBase64(encodedText);
        return new String(decodedBytes);
    }

    // 图像编码
    public static String encodeImage(File imageFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(imageFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = bos.toByteArray();
            return Base64.encodeBase64String(imageBytes);
        } catch (IOException e) {
            log.error("Error encoding image: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 图像解码
    public static void decodeImage(String encodedImage, File outputFile) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(encodedImage));
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            BufferedImage bufferedImage = ImageIO.read(bais);
            if (bufferedImage == null) {
                throw new IOException("Failed to read image from Base64 string.");
            }
            ImageIO.write(bufferedImage, "jpg", fos);
        } catch (IOException e) {
            log.error("Error decoding image: {}", e.getMessage(), e);
            throw e;
        }
    }

    // URL安全的Base64编码(URL安全的Base64编码会将+替换为-，并将/替换为_，并且不会在末尾添加填充字符=。这使得编码后的字符串可以在URL中安全地传输。)
    public static String encodeUrlSafe(String text) {
        return Base64.encodeBase64URLSafeString(text.getBytes(StandardCharsets.UTF_8));
    }

    // URL安全的Base64解码
    public static String decodeUrlSafe(String encodedText) {
        return new String(Base64.decodeBase64(encodedText), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            // 示例：图像编码和解码
            File imageFile = new File("D:\\temp\\images\\test.jpg");
            String encodedImage = encodeImage(imageFile);
            System.out.println("Encoded Image: " + encodedImage);
            File outputFile = new File("image.jpg");
            decodeImage(encodedImage, outputFile);
            System.out.println("Image decoded and saved to: " + outputFile.getAbsolutePath());

            // 示例：文本编码和解码
            String text = "Hello, World! 你好，世界！";

            String encoder = encoderTextUTF8(text);
            System.out.println("Encoded Text: " + encoder);
            String decoder = decoderTextUTF8(encoder);
            System.out.println("Decoded Text: " + decoder);

            String encodedText = encodeText(text);
            System.out.println("Encoded Text: " + encodedText);
            String decodedText = decodeText(encodedText);
            System.out.println("Decoded Text: " + decodedText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
