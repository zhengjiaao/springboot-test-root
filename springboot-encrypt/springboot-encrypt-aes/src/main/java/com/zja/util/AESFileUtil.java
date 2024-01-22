package com.zja.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * @author: zhengja
 * @since: 2024/01/22 15:34
 */
public class AESFileUtil {

    private static final String ALGORITHM = "AES";
    // private static final String KEY = "YourSecretKey"; // YourSecretKey 自定义秘钥(key),一定是16位字节长度
    private static final String KEY = "zja26h8g3y22bh==";  // 这是个参考key

    /**
     * 加密文件
     */
    public static void encryptFile(String inputFile, String outputFile) throws Exception {
        encryptFile(inputFile, outputFile, KEY);
    }

    /**
     * 加密文件
     */
    public static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        performCipherOperation(inputFile, outputFile, cipher);
    }

    /**
     * 解密文件
     */
    public static void decryptFile(String inputFile, String outputFile) throws Exception {
        decryptFile(inputFile, outputFile, KEY);
    }

    /**
     * 解密文件
     */
    public static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        performCipherOperation(inputFile, outputFile, cipher);
    }

    private static void performCipherOperation(String inputFile, String outputFile, Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
            outputStream.write(decryptedBytes);
        }
        byte[] finalBytes = cipher.doFinal();
        outputStream.write(finalBytes);

        inputStream.close();
        outputStream.close();
    }

    public static void main(String[] args) {
        String inputFile = "D:\\temp\\pdf\\100M.pdf";
        String encryptedFile = "encrypted.pdf";
        String decryptedFile = "decrypted.pdf";

        try {
            encryptFile(inputFile, encryptedFile);
            decryptFile(encryptedFile, decryptedFile);
            System.out.println("File encryption and decryption completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
