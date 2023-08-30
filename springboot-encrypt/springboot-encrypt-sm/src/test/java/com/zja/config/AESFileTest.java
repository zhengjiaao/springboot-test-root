/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-30 9:50
 * @Since:
 */
package com.zja.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/08/30 9:50
 */
public class AESFileTest {

    //AES 适合加解密文件，效率比 SMFileTests.sm4 高
    @Test
    public void aes_file_test_v1() throws IOException {
        //源文件
        String inputFile = "D:\\temp\\pdf\\800M.pdf";

        //解密后文件
        String encryptedFile = "D:\\temp\\pdf\\aes\\output_encrypted_pdf.pdf";
        //加密后文件
        String decryptedFile = "D:\\temp\\pdf\\aes\\output_decrypted_pdf.pdf";

        AES aes = SecureUtil.aes();
        aes.encrypt(FileUtil.getInputStream(inputFile), Files.newOutputStream(Paths.get(encryptedFile)), true);

        aes.decrypt(FileUtil.getInputStream(encryptedFile), Files.newOutputStream(Paths.get(decryptedFile)), true);

    }

}
