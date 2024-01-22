package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:51
 */
public class BufferedReaderExample {

    // 使用BufferedReader和BufferedWriter进行缓冲读写
    @Test
    public void test() {
        try (BufferedReader reader = new BufferedReader(new FileReader("source.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("destination.txt"))) {
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
