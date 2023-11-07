/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 10:16
 * @Since:
 */
package com.zja.io;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: zhengja
 * @since: 2023/11/07 10:16
 */
public class FilesTest {

    @Test
    public void testReadFile() throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("test", ".txt");
        String content = "Hello, Guava Files!";
        Files.write(content.getBytes(StandardCharsets.UTF_8), tempFile);

        // 使用Guava Files读取文件内容
        List<String> lines = Files.readLines(tempFile, StandardCharsets.UTF_8);

        // 验证文件内容
        assertEquals(1, lines.size());
        assertEquals(content, lines.get(0));

        // 清理临时文件
        assertTrue(tempFile.delete());
    }

    @Test
    public void testCopyFile() throws IOException {
        // 创建临时文件和目标文件
        File sourceFile = File.createTempFile("source", ".txt");
        File destFile = File.createTempFile("dest", ".txt");

        // 写入源文件内容
        String content = "Hello, Guava Files!";
        Files.write(content.getBytes(StandardCharsets.UTF_8), sourceFile);

        // 使用Guava Files复制文件
        Files.copy(sourceFile, destFile);

        // 验证目标文件是否存在并且内容相同
        assertTrue(destFile.exists());
        assertEquals(content, Files.asCharSource(destFile, StandardCharsets.UTF_8).read());

        // 清理临时文件
        assertTrue(sourceFile.delete());
        assertTrue(destFile.delete());
    }

    @Test
    public void testMoveFile() throws IOException {
        // 创建临时文件和目标文件夹
        File sourceFile = File.createTempFile("source", ".txt");
        Files.createParentDirs(sourceFile);
        Path destPath = Paths.get(sourceFile.getParentFile().getAbsolutePath(), sourceFile.getName());

        // 写入源文件内容
        String content = "Hello, Guava Files!";
        Files.write(content.getBytes(StandardCharsets.UTF_8), sourceFile);

        // 使用Guava Files移动文件
        Files.move(sourceFile, destPath.toFile());

        // 验证源文件不存在且目标文件存在并且内容相同
        assertTrue(!sourceFile.exists());
        assertTrue(destPath.toFile().exists());
        assertEquals(content, Files.asCharSource(destPath.toFile(), StandardCharsets.UTF_8).read());

        // 清理临时文件和目标文件夹
        assertTrue(destPath.toFile().delete());
        assertTrue(sourceFile.getParentFile().delete());
    }
}
