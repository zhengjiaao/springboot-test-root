package com.zja.file.io.google;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:11
 */
public class FilesExample {

    @Test
    public void test() {
        // 获取字符编码
        String charsetName = Charsets.UTF_8.name();
        System.out.println("字符编码：" + charsetName);

        // 使用指定字符编码将字符串转换为字节数组
        String text = "Hello, Guava!";
        byte[] bytes = text.getBytes(Charsets.UTF_8);

        // 使用指定字符编码将字节数组转换为字符串
        String decodedText = new String(bytes, Charsets.UTF_8);
        System.out.println("解码后的字符串：" + decodedText);
    }
}
