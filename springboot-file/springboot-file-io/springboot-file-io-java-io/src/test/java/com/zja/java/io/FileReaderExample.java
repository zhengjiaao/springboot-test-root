package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:52
 */
public class FileReaderExample {

    // 使用FileReader和FileWriter进行字符文件的读写
    @Test
    public void test() {
        try (FileReader reader = new FileReader("source.txt");
             FileWriter writer = new FileWriter("destination.txt")) {
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, charsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
