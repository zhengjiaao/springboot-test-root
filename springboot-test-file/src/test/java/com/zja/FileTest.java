package com.zja;


import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Date: 2020-01-03 11:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class FileTest {

    private static String filePath="D:\\FileTest\\Maven\\aa.txt"; //要操作的文件的路径

    @Test
    public void test(){

        File file = new File(filePath);

        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileCreationTime = LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime fileLastModifiedTime = LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String creationTime = df.format(fileCreationTime);
            String lastModifiedTime = df.format(fileLastModifiedTime);

            System.out.println("文件创建时间: " + creationTime);
            System.out.println("文件修改时间: " + lastModifiedTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
