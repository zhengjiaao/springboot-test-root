package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:52
 */
public class FileReaderExample {

    static final String filePath_source = Paths.get("target", "example_io.txt").toString();

    static final String filePath_destination = Paths.get("target", "example_io_destination.txt").toString();


    // 使用FileReader和FileWriter进行字符文件的读写
    @Test
    public void test_Reader_and_Writer() {
        try (FileReader reader = new FileReader(filePath_source);
             FileWriter writer = new FileWriter(filePath_destination)) {
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
