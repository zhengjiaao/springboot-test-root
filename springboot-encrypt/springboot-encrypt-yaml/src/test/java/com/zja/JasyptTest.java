/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-05 10:35
 * @Since:
 */
package com.zja;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * yaml 配置文件内容加解密
 *
 * @author: zhengja
 * @since: 2023/09/05 10:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EncryptYamlApplication.class)
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;

    @Value("${after_encryption}")
    private String afterEncryption;

    @Test
    public void test() {
        //jasypt 自动解密
        System.out.println(afterEncryption);
    }

    @Test
    public void encrypt_and_decrypt() {
        //加密
        String hello = encryptor.encrypt("hello");
        System.out.println(hello);

        //解密
        String helloTxt = encryptor.decrypt(hello);
        System.out.println(helloTxt);
    }

}

