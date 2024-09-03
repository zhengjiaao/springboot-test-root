package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Java NIO 示例
 * <p>
 * 主要类：
 * FileChannel
 * ByteBuffer
 * SocketChannel
 * ServerSocketChannel
 * Selector
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-09-03 9:25
 */
public class JavaNIOExample {

    static final String filePath = Paths.get("target", "example_nio.txt").toString();

    /**
     * 总结
     * 在 Java IO 示例中，我们使用了 FileWriter、BufferedWriter 和 PrintWriter 来写入文件，然后使用 FileInputStream 和 BufferedInputStream 来读取文件内容。
     * 在 Java NIO 示例中，我们使用了 FileChannel 和 ByteBuffer 来写入和读取文件内容。
     */
    @Test
    public void test_nio() {

        // 写入文件
        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            String data = "Hello, Java NIO!\nThis is a sample file.";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(data.getBytes());
            buffer.flip(); // 切换到读模式
            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件
        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip(); // 切换到读模式
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear(); // 清空缓冲区
                bytesRead = fileChannel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("filePath："+ filePath);
    }
}
