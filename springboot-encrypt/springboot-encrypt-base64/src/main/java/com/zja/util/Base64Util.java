/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 16:06
 * @Since:
 */
package com.zja.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 编码工具类
 * Java 8 提供的 Base64 效率最高
 *
 * @author: zhengja
 * @since: 2023/08/31 16:06
 */
public class Base64Util {

    private static final Logger log = LoggerFactory.getLogger(Base64Util.class);

    private Base64Util() {

    }

    /**
     * Base64 编码
     */
    public static String encoderTextUTF8(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码
     */
    public static String decoderTextUTF8(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }

    // 文本编码
    public static String encodeText(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    // 文本解码
    public static String decodeText(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
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
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("Error encoding image: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 图像解码
    public static void decodeImage(String encodedImage, File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
            fos.write(imageBytes);
        } catch (IOException e) {
            log.error("Error decoding image: {}", e.getMessage(), e);
            throw e;
        }
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
