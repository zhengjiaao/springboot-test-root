package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:55
 */
public class RandomAccessFileExample {

    // 读取文件中的数据
    @Test
    public void test() {
        try (RandomAccessFile file = new RandomAccessFile("source.txt", "r")) {
            byte[] buffer = new byte[1024];
            int bytesRead = file.read(buffer);
            while (bytesRead != -1) {
                System.out.write(buffer, 0, bytesRead);
                bytesRead = file.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向文件中写入数据
    @Test
    public void test2() {
        try (RandomAccessFile file = new RandomAccessFile("source.txt", "rw")) {
            file.seek(file.length()); // 将文件指针移动到文件末尾
            file.writeBytes("Hello, World!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在文件中定位并读取特定位置的数据
    @Test
    public void test3() {
        try (RandomAccessFile file = new RandomAccessFile("source.txt", "r")) {
            file.seek(10); // 将文件指针移动到第10个字节的位置
            byte[] buffer = new byte[1024];
            int bytesRead = file.read(buffer);
            while (bytesRead != -1) {
                System.out.write(buffer, 0, bytesRead);
                bytesRead = file.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
