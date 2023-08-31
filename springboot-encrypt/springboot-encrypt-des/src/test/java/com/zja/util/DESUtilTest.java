/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 15:18
 * @Since:
 */
package com.zja.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/08/31 15:18
 */
public class DESUtilTest {

    //自定义秘钥(key),限定8位 或 24位
    private static final String DES_KEY = "D076D35C";

    @Test
    public void encryptText_test() throws Exception {
        String text = "这是一段明文句子。";

        System.out.println("加密前：" + text);

        String encryptText = DESUtil.encryptText(text, DES_KEY);
        System.out.println("加密后：" + encryptText);

        String decryptText = DESUtil.decryptText(encryptText, DES_KEY);
        System.out.println("解密后：" + decryptText);
    }

    @Test
    public void decryptText_test() throws Exception {
        String encryptText = "4G3a01H42omGub09MGt2XJlvAAwKxXMi+Gd20AL6ZFI=";
        String decryptText = DESUtil.decryptText(encryptText, DES_KEY);
        System.out.println(decryptText);
    }

    @Test
    public void encryptFile_test() throws Exception {
        //源文件
        File inputFile = new File("D:\\temp\\txt\\txt.txt");

        File encryptFile = new File("D:\\temp\\txt\\des\\encryptFile_txt.txt");
        //加密文件
        DESUtil.encryptFile(inputFile, encryptFile, DES_KEY);

        File decryptFile = new File("D:\\temp\\txt\\des\\decryptFile_txt.txt");
        //解密文件
        DESUtil.decryptFile(encryptFile, decryptFile, DES_KEY);
    }

    @Test
    public void decryptFile_test() throws Exception {
        File inputFile = new File("D:\\temp\\txt\\des\\encryptFile_txt.txt");
        File outputFile = new File("D:\\temp\\txt\\des\\decryptFile_txt.txt");

        DESUtil.decryptFile(inputFile, outputFile, DES_KEY);
    }

    //使用 hutool des 工具类示例
    @Test
    public void des_file_test_v1() throws IOException {
        //源文件
        String inputFile = "D:\\temp\\pdf\\800M.pdf";

        //解密后文件
        String encryptedFile = "D:\\temp\\pdf\\des\\output_encrypted_pdf.pdf";
        //加密后文件
        String decryptedFile = "D:\\temp\\pdf\\des\\output_decrypted_pdf.pdf";

        DES des = SecureUtil.des();
        //加密文件
        des.encrypt(FileUtil.getInputStream(inputFile), Files.newOutputStream(Paths.get(encryptedFile)), true);
        //解密文件
        des.decrypt(FileUtil.getInputStream(encryptedFile), Files.newOutputStream(Paths.get(decryptedFile)), true);
    }

}
