/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 10:16
 * @Since:
 */
package com.zja.io;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author: zhengja
 * @since: 2023/11/07 10:16
 */
public class ResourcesTest {

    @Test
    public void testReadResourceAsString() throws IOException {
        // 读取资源文件为字符串
        URL resourceUrl = Resources.getResource("example.txt");
        String content = Resources.toString(resourceUrl, StandardCharsets.UTF_8);

        // 验证资源文件内容
        String expectedContent = "Hello, Guava Resources!";
        assertEquals(expectedContent, content);
    }

    @Test
    public void testReadResourceAsBytes() throws IOException {
        // 读取资源文件为字节数组
        URL resourceUrl = Resources.getResource("example.jpg");
        byte[] bytes = Resources.toByteArray(resourceUrl);

        // 验证资源文件的字节内容
        assertEquals(123456, bytes.length); // 假设例子图片的大小为123456字节
    }

    @Test
    public void testGetResourceUrl() {
        // 获取资源文件的URL
        URL resourceUrl = Resources.getResource("example.txt");

        // 验证资源文件的URL不为null
        assertNotNull(resourceUrl);
    }

    @Test
    public void testOpenResourceStream() throws IOException {
        // 获取资源文件的输入流
        URL resourceUrl = Resources.getResource("example.txt");
        try (InputStream inputStream = Resources.asByteSource(resourceUrl).openStream()) {
            // 在此处处理资源文件的输入流
            // 例如，使用流进行进一步的操作或读取数据
            // 这里只是一个示例，您可以根据需要进行自定义处理
        }

        // 在try-with-resources块结束后，资源流会自动关闭
    }

}
