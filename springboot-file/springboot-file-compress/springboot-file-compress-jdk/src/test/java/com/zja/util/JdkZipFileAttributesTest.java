package com.zja.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

/**
 * @author: zhengja
 * @since: 2024/03/18 14:43
 */
public class JdkZipFileAttributesTest {

    @Test
    public void test() {
        Instant zipFileCreationTime = getZipFileCreationTime("D:\\temp\\zip\\测试目录.zip");
        System.out.println(zipFileCreationTime);
    }

    /**
     * 获取ZIP文件的创建时间
     * <p>
     * ZIP文件的创建时间可以取决于操作系统和文件系统的支持。不是所有的文件系统都能够准确地提供ZIP文件的创建时间。在某些情况下，文件系统可能不提供创建时间，或者创建时间可能与文件的其他属性相同。因此，创建时间的可用性可能因操作系统和文件系统而异。
     *
     * @param zipFilePath
     * @return
     */
    public Instant getZipFileCreationTime(String zipFilePath) {
        try {
            Path filePath = Paths.get(zipFilePath);
            BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            return attributes.creationTime().toInstant();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
