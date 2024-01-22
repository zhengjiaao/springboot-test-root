package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:01
 */
public class FilesExample {

    @Test
    public void test() {
        try {
            Path sourcePath = Paths.get("source.txt");
            Path destinationPath = Paths.get("destination.txt");

            // 复制文件
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // 移动文件
            Path newLocation = Paths.get("newLocation.txt");
            Files.move(destinationPath, newLocation, StandardCopyOption.REPLACE_EXISTING);

            // 读取文件内容
            List<String> fileContent = Files.readAllLines(newLocation);
            System.out.println("文件内容：" + fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
