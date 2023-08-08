/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 9:43
 * @Since:
 */
package com.zja;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.jupiter.api.Test;

/**
 * druid 数据库密码加密
 *
 * @author: zhengja
 * @since: 2023/08/08 9:43
 */
public class ConfigToolsTest {
    @Test
    public void test_password() throws Exception {
        String password = "pass";
        //采用 RSA秘钥算法，私钥加密，公钥解密
        String[] arr = ConfigTools.genKeyPair(512);
        //以下都要保存好，避免丢失
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(arr[0], password));
    }
}
