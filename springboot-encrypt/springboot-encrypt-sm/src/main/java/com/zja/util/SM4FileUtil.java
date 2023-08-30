/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-30 9:24
 * @Since:
 */
package com.zja.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2023/08/30 9:24
 */
public class SM4FileUtil {
    private static final int BLOCK_SIZE = 16; // 数据块大小为16字节
    private static final byte[] DEFAULT_IV = new byte[BLOCK_SIZE]; // 默认的初始向量

    /**
     * 使用SM4算法对文件进行加密
     *
     * @param inputFile  待加密的文件路径
     * @param outputFile 加密后的文件路径
     * @param key        密钥
     */
    public static void encryptFile(String inputFile, String outputFile, String key) throws IOException, InvalidCipherTextException {
        try (FileInputStream fis = new FileInputStream(inputFile); FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] inputBuffer = new byte[BLOCK_SIZE];
            byte[] outputBuffer = new byte[BLOCK_SIZE];
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            SM4Engine engine = new SM4Engine();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
            cipher.init(true, new ParametersWithIV(new KeyParameter(keyBytes), DEFAULT_IV));

            int bytesRead;
            while ((bytesRead = fis.read(inputBuffer)) != -1) {
                int outputLength = cipher.processBytes(inputBuffer, 0, bytesRead, outputBuffer, 0);
                fos.write(outputBuffer, 0, outputLength);
            }
            int outputLength = cipher.doFinal(outputBuffer, 0);
            fos.write(outputBuffer, 0, outputLength);
        }
    }

    /**
     * 使用SM4算法对文件进行解密
     *
     * @param inputFile  待解密的文件路径
     * @param outputFile 解密后的文件路径
     * @param key        密钥
     */
    public static void decryptFile(String inputFile, String outputFile, String key) throws IOException, InvalidCipherTextException {
        try (FileInputStream fis = new FileInputStream(inputFile); FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] inputBuffer = new byte[BLOCK_SIZE];
            byte[] outputBuffer = new byte[BLOCK_SIZE];
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            SM4Engine engine = new SM4Engine();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
            cipher.init(false, new ParametersWithIV(new KeyParameter(keyBytes), DEFAULT_IV));

            int bytesRead;
            while ((bytesRead = fis.read(inputBuffer)) != -1) {
                int outputLength = cipher.processBytes(inputBuffer, 0, bytesRead, outputBuffer, 0);
                fos.write(outputBuffer, 0, outputLength);
            }
            int outputLength = cipher.doFinal(outputBuffer, 0);
            fos.write(outputBuffer, 0, outputLength);
        }
    }

    /**
     * 将字节数组转换为Base64字符串
     *
     * @param data 字节数组
     * @return Base64编码后的字符串
     */
    private static String encodeBase64(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    /**
     * 将Base64字符串转换为字节数组
     *
     * @param base64String Base64编码的字符串
     * @return 解码后的字节数组
     */
    private static byte[] decodeBase64(String base64String) {
        return Base64.decodeBase64(base64String);
    }

}
