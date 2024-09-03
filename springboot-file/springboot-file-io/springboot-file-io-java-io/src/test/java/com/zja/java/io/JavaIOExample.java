package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;

/**
 * Java IO 示例
 * <p>
 * 主要类：
 * FileInputStream
 * FileOutputStream
 * FileReader
 * FileWriter
 * BufferedInputStream
 * BufferedOutputStream
 * PrintWriter
 * </p>
 * @Author: zhengja
 * @Date: 2024-09-03 9:26
 */
public class JavaIOExample {

    static final String filePath = Paths.get("target", "example_io.txt").toString();

    @Test
    public void test_io() {

        // 写入文件
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            printWriter.println("Hello, Java IO!");
            printWriter.println("This is a sample file.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            int data;
            while ((data = bufferedInputStream.read()) != -1) {
                System.out.print((char) data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("filePath："+ filePath);
    }

}
