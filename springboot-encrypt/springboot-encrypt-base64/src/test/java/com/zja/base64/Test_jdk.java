/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 11:09
 * @Since:
 */
package com.zja.base64;

import org.junit.Test;

import java.io.IOException;
import java.util.Base64;

/**
 * util 包下的 Base64 (jdk8)
 * Java 8 提供的 Base64 效率最高. 实际测试编码与解码速度, Java 8 的 Base64 要比 sun包下的要快大约 11 倍，比 Apache 的快大约 3 倍.
 */
public class Test_jdk {
    @Test
    public void test() throws IOException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
    }
}
