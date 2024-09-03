package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:51
 */
public class FileInputStreamExample {

    static final String filePath_source = Paths.get("target", "example_io.txt").toString();

    static final String filePath_destination = Paths.get("target", "example_io_destination.txt").toString();


    // 使用FileInputStream和FileOutputStream进行文件读写
    @Test
    public void test() {
        try (FileInputStream fis = new FileInputStream(filePath_source);
             FileOutputStream fos = new FileOutputStream(filePath_destination)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
