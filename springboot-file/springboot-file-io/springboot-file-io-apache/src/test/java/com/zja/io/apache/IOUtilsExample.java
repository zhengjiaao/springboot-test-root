package com.zja.io.apache;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:39
 */
public class IOUtilsExample {

    @Test
    public void test() {
        try {
            // 复制流
            InputStream inputStream = Files.newInputStream(Paths.get("source.txt"));
            OutputStream outputStream = Files.newOutputStream(Paths.get("destination.txt"));
            IOUtils.copy(inputStream, outputStream);

            // 关闭流
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
