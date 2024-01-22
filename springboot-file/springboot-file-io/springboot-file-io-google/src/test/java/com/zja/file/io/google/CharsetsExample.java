package com.zja.file.io.google;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:11
 */
public class CharsetsExample {

    @Test
    public void test() {
        try {
            // 读取文件内容
            File file = new File("source.txt");
            String fileContent = Files.asCharSource(file, StandardCharsets.UTF_8).read();
            System.out.println("文件内容：" + fileContent);

            // 写入文件内容
            String contentToWrite = "Hello, Guava!";
            Files.asCharSink(file, StandardCharsets.UTF_8).write(contentToWrite);

            // 复制文件
            File destFile = new File("destination.txt");
            Files.copy(file, destFile);

            // 移动文件
            File newLocation = new File("newLocation.txt");
            Files.move(destFile, newLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
