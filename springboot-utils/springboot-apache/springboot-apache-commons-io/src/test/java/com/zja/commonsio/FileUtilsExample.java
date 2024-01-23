package com.zja.commonsio;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:36
 */
public class FileUtilsExample {

    @Test
    public void test() {
        try {
            // 复制文件
            File srcFile = new File("source.txt");
            File destFile = new File("destination.txt");
            FileUtils.copyFile(srcFile, destFile);

            // 读取文件内容
            String fileContent = FileUtils.readFileToString(destFile, StandardCharsets.UTF_8);
            System.out.println("文件内容：" + fileContent);

            // 删除文件
            FileUtils.deleteQuietly(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
