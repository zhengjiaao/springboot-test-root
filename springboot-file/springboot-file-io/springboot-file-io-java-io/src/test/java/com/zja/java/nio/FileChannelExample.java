package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:00
 */
public class FileChannelExample {

    static final String filePath = Paths.get("target", "example_nio.txt").toString();

    static final String filePath_destination = Paths.get("target", "example_nio_destination.txt").toString();

    @Test
    public void test_FileChannel_Copy() {
        try (FileChannel sourceChannel = new FileInputStream(filePath).getChannel();
             FileChannel destinationChannel = new FileOutputStream(filePath_destination).getChannel()) {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
