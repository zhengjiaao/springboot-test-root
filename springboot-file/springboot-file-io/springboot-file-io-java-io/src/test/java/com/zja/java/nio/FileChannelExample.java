package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:00
 */
public class FileChannelExample {

    @Test
    public void test() {
        try (FileChannel sourceChannel = new FileInputStream("source.txt").getChannel();
             FileChannel destinationChannel = new FileOutputStream("destination.txt").getChannel()) {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
