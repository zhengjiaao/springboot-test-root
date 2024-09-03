package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:51
 */
public class BufferedReaderExample {

    static final String filePath_source = Paths.get("target", "example_io.txt").toString();

    static final String filePath_destination = Paths.get("target", "example_io_destination.txt").toString();

    // 使用BufferedReader和BufferedWriter进行缓冲读写
    @Test
    public void test() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath_source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath_destination))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
