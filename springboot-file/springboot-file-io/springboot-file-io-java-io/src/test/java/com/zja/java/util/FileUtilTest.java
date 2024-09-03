package com.zja.java.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @Author: zhengja
 * @Date: 2024-09-03 14:58
 */
public class FileUtilTest {

    static final String filePath = Paths.get("target", "example_file.txt").toString();

    @Test
    public void test_file_write_And_read() throws IOException {
        Path sourcePath = Paths.get(filePath);

        // 创建父目录
        FileUtil.createParentDirectories(sourcePath);

        // 写入文件内容, 如果文件不存在，则创建，如果文件已存在，（APPEND）则在文件末尾追加，（CREATE） 如果文件不存在，则创建,存在则不覆盖,(TRUNCATE_EXISTING) 清空文件，覆盖
        try (OutputStream outputStream = FileUtil.newOutputStream(sourcePath, StandardOpenOption.CREATE)) {
            outputStream.write("Hello, world!".getBytes());
        }

        // 读取文件内容
        try (InputStream inputStream = FileUtil.newInputStream(sourcePath, StandardOpenOption.READ)) {
            // 读取文件内容(按字节读取)
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 处理读取到的字节数据
                System.out.println("文件内容：" + new String(buffer, 0, bytesRead));
            }
        }
    }

}
