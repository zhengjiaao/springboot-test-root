/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 14:14
 * @Since:
 */
package com.zja.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * AES 加解密工具类
 *
 * @author: zhengja
 * @since: 2023/08/31 14:14
 * @dasc: 请先尝试在以下工具包中查找封装的方法，若找不到，在此 工具类 添加自定文件方法
 * @see cn.hutool.crypto.SecureUtil#aes()
 */
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 生成密钥
     */
    private static Key generateKey(String key) {
        return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    /**
     * 执行加密或解密操作
     */
    private static byte[] performCipherOperation(int cipherMode, Key secretKey, byte[] inputBytes) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        return cipher.doFinal(inputBytes);
    }

    /**
     * 加密文本
     */
    public static String encryptText(String text, String key) throws Exception {
        Key secretKey = generateKey(key);
        byte[] encryptedBytes = performCipherOperation(Cipher.ENCRYPT_MODE, secretKey, text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密文本
     */
    public static String decryptText(String encryptedText, String key) throws Exception {
        Key secretKey = generateKey(key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = performCipherOperation(Cipher.DECRYPT_MODE, secretKey, encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 加密文件
     */
    public static void encryptFile(File inputFile, File outputFile, String key) throws Exception {
        Key secretKey = generateKey(key);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            inputStream.read(inputBytes);
            byte[] encryptedBytes = performCipherOperation(Cipher.ENCRYPT_MODE, secretKey, inputBytes);
            outputStream.write(encryptedBytes);
        }
    }

    /**
     * 解密文件
     */
    public static void decryptFile(File inputFile, File outputFile, String key) throws Exception {
        Key secretKey = generateKey(key);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            inputStream.read(inputBytes);
            byte[] decryptedBytes = performCipherOperation(Cipher.DECRYPT_MODE, secretKey, inputBytes);
            outputStream.write(decryptedBytes);
        }
    }

    public static void main(String[] args) throws Exception {
        //自定义秘钥(key),一定是16位字节长度。
        //这仅是一个参考
        String AES_KEY = "zjaq2h8g3y22bh==";

        String text = "这是一段明文句子。";

        System.out.println("加密前：" + text);

        String encryptText = AESUtil.encryptText(text, AES_KEY);
        System.out.println("加密后：" + encryptText);

        String decryptText = AESUtil.decryptText(encryptText, AES_KEY);
        System.out.println("解密后：" + decryptText);
    }
}
