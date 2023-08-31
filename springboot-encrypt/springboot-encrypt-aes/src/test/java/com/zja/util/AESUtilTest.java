/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 14:17
 * @Since:
 */
package com.zja.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/08/31 14:17
 */
public class AESUtilTest {

    //自定义秘钥(key),一定是16位字节长度
    private static final String AES_KEY = "zjaq2h8g3y22bh==";

    @Test
    public void encryptText_test() throws Exception {
        String text = "这是一段明文句子。";

        System.out.println("加密前：" + text);

        String encryptText = AESUtil.encryptText(text, AES_KEY);
        System.out.println("加密后：" + encryptText);

        String decryptText = AESUtil.decryptText(encryptText, AES_KEY);
        System.out.println("解密后：" + decryptText);
    }

    @Test
    public void decryptText_test() throws Exception {
        String encryptText = "MpvkxOtUtsuI6QhOjENQ1ySLobHmBSWcZy5JdPBVaTY=";
        String decryptText = AESUtil.decryptText(encryptText, AES_KEY);
        System.out.println(decryptText);
    }

    @Test
    public void encryptFile_test() throws Exception {
        //源文件
        File inputFile = new File("D:\\temp\\txt\\txt.txt");

        File encryptFile = new File("D:\\temp\\txt\\aes\\encryptFile_txt.txt");
        //加密文件
        AESUtil.encryptFile(inputFile, encryptFile, AES_KEY);

        File decryptFile = new File("D:\\temp\\txt\\aes\\decryptFile_txt.txt");
        //解密文件
        AESUtil.decryptFile(encryptFile, decryptFile, AES_KEY);
    }

    @Test
    public void decryptFile_test() throws Exception {
        File inputFile = new File("D:\\temp\\txt\\aes\\encryptFile_txt.txt");
        File outputFile = new File("D:\\temp\\txt\\aes\\decryptFile_txt.txt");

        AESUtil.decryptFile(inputFile, outputFile, AES_KEY);
    }

    //使用 hutool aes 工具类示例
    @Test
    public void aes_file_test_v1() throws IOException {
        //源文件
        String inputFile = "D:\\temp\\pdf\\800M.pdf";

        //解密后文件
        String encryptedFile = "D:\\temp\\pdf\\aes\\output_encrypted_pdf.pdf";
        //加密后文件
        String decryptedFile = "D:\\temp\\pdf\\aes\\output_decrypted_pdf.pdf";

        AES aes = SecureUtil.aes();
        //加密文件
        aes.encrypt(FileUtil.getInputStream(inputFile), Files.newOutputStream(Paths.get(encryptedFile)), true);
        //解密文件
        aes.decrypt(FileUtil.getInputStream(encryptedFile), Files.newOutputStream(Paths.get(decryptedFile)), true);
    }
}
