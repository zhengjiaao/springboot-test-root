package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:01
 */
public class ByteBufferExample {

    static final String filePath = Paths.get("target", "example_nio.txt").toString();

    @Test
    public void test_FileChannel_Read() {
        try (FileChannel channel = new FileInputStream(filePath).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
