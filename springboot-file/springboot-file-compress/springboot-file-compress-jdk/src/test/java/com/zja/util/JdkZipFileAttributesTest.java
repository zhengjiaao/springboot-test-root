package com.zja.util;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author: zhengja
 * @since: 2024/03/18 14:43
 */
public class JdkZipFileAttributesTest {

    private final static String zipFilePath = "D:\\temp\\zip\\测试目录.zip";

    // 获取ZIP文件创建时间
    @Test
    public void getZipFileCreationTime_test() {
        Instant zipFileCreationTime = getZipFileCreationTime(zipFilePath);
        System.out.println(formatInstant(zipFileCreationTime, "Asia/Shanghai", "yyyy-MM-dd HH:mm:ss"));
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

    public String formatInstant(Instant instant, String timeZoneId, String pattern) {
        if (null == instant) {
            return null;
        }
        ZoneId zoneId = ZoneId.of(timeZoneId);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }


    // 设置ZIP文件自定义属性、并读取自定义后的属性
    @Test
    public void customAttributeToZipFile_test() {
        setCustomAttributeToZipFile(zipFilePath, "key", "value");
        String keyValue = getCustomAttributeToZipFile(zipFilePath, "key");
        System.out.println(keyValue);
    }

    public void setCustomAttributeToZipFile(String zipFilePath, String attributeName, String attributeValue) {
        try {
            Path filePath = Paths.get(zipFilePath);
            UserDefinedFileAttributeView attributeView = Files.getFileAttributeView(filePath, UserDefinedFileAttributeView.class);
            // 将属性值转换为字节数组
            byte[] valueBytes = attributeValue.getBytes(StandardCharsets.UTF_8);
            // 设置自定义属性
            attributeView.write(attributeName, ByteBuffer.wrap(valueBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCustomAttributeToZipFile(String zipFilePath, String attributeName) {
        try {
            Path filePath = Paths.get(zipFilePath);
            UserDefinedFileAttributeView attributeView = Files.getFileAttributeView(filePath, UserDefinedFileAttributeView.class);
            // 读取自定义属性
            ByteBuffer buffer = ByteBuffer.allocate(attributeView.size(attributeName));
            attributeView.read(attributeName, buffer);
            buffer.flip();
            return new String(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
