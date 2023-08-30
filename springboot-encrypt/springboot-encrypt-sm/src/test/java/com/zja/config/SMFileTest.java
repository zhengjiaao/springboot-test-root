/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-29 17:43
 * @Since:
 */
package com.zja.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 测试国密算法，针对文件进行加解密效率情况
 *
 * @author: zhengja
 * @since: 2023/08/29 17:43
 */
public class SMFileTest {

    //SM2不适合大文件加解密 比如：超过2G大小的文件
    @Test
    public void sm2_file_test() {
        SM2 sm2 = SmUtil.sm2();
    }

    //SM3签名算法，不可逆，不适合文件加解密
    @Test
    public void sm3_file_test() {
        String inputFile = "D:\\temp\\pdf\\800M.pdf";

        String s = SmUtil.sm3(new File(inputFile));
        System.out.println(s);
    }

    //SM4适合文件加解密
    //整个方法耗时大概26秒
    @Test
    public void sm4_file_test_v1() throws IOException {
        //源文件
        String inputFile = "D:\\temp\\pdf\\800M.pdf";

        //解密后文件
        String encryptedFile = "D:\\temp\\pdf\\sm4\\output_encrypted_pdf.pdf";
        //加密后文件
        String decryptedFile = "D:\\temp\\pdf\\sm4\\output_decrypted_pdf.pdf";

        SM4 sm4 = SmUtil.sm4();
        sm4.encrypt(FileUtil.getInputStream(inputFile), Files.newOutputStream(Paths.get(encryptedFile)), true);

        sm4.decrypt(FileUtil.getInputStream(encryptedFile), Files.newOutputStream(Paths.get(decryptedFile)), true);
    }
}
