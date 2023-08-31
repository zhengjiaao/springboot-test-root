/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 14:58
 * @Since:
 */
package com.zja.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author: zhengja
 * @since: 2023/08/31 14:58
 */
public class DESUtil {

    /**
     * 文本加密
     */
    public static String encryptText(String text, String key) throws EncryptionException {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
            byte[] inputBytes = text.getBytes(StandardCharsets.UTF_8);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            byte[] encryptedBytes = Base64.getEncoder().encode(outputBytes);
            return new String(encryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt text", e);
        }
    }

    /**
     * 文本解密
     */
    public static String decryptText(String encryptedText, String key) throws EncryptionException {
        try {
            byte[] encryptedBytes = encryptedText.getBytes(StandardCharsets.UTF_8);
            byte[] inputBytes = Base64.getDecoder().decode(encryptedBytes);
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            return new String(outputBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt text", e);
        }
    }

    /**
     * 加密文件
     */
    public static void encryptFile(File inputFile, File outputFile, String key) throws EncryptionException {
        processFile(inputFile, outputFile, key, Cipher.ENCRYPT_MODE);

    }

    /**
     * 解密文件
     */
    public static void decryptFile(File inputFile, File outputFile, String key) throws EncryptionException {
        processFile(inputFile, outputFile, key, Cipher.DECRYPT_MODE);
    }

    private static Cipher getCipher(int cipherMode, String key) throws Exception {
        KeySpec keySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(cipherMode, secretKey);
        return cipher;
    }

    private static void processFile(File inputFile, File outputFile, String key, int cipherMode) throws EncryptionException {
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            Cipher cipher = getCipher(cipherMode, key);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            outputStream.write(outputBytes);
        } catch (Exception e) {
            throw new EncryptionException("Failed to process file", e);
        }
    }

    public static class EncryptionException extends Exception {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
