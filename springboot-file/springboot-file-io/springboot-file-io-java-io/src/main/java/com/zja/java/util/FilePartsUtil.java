package com.zja.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @Author: zhengja
 * @Date: 2024-12-18 15:31
 */
public class FilePartsUtil {

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(FilePartsUtil.class);
    // 分片文件后缀
    private static final String SPLIT_FILE_SUFFIX = ".part";

    private FilePartsUtil() {
    }

    public static void splitFile(String sourceFilePath, String splitFileDir, long partSize) {
        if (sourceFilePath == null || splitFileDir == null) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        if (partSize <= 0) {
            throw new IllegalArgumentException("分片大小必须大于0");
        }

        File file = new File(sourceFilePath);
        if (!file.exists()) {
            throw new RuntimeException("源文件不存在: " + sourceFilePath);
        }
        long fileSize = file.length();
        int partCount = (int) (fileSize / partSize + (fileSize % partSize == 0 ? 0 : 1));

        // 确保目录存在
        File dir = new File(splitFileDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("无法创建目录: " + splitFileDir);
        }

        for (int i = 0; i < partCount; i++) {
            long start = i * partSize;
            long end = Math.min(start + partSize, fileSize);
            String partFilePath = splitFileDir + File.separator + "part" + (i + 1) + SPLIT_FILE_SUFFIX;
            try (FileInputStream fis = new FileInputStream(file);
                 FileChannel channel = fis.getChannel();
                 FileOutputStream fos = new FileOutputStream(partFilePath);
                 FileChannel targetChannel = fos.getChannel()) {
                channel.transferTo(start, end - start, targetChannel);
                LOGGER.debug("分片完成：{}", partFilePath);
            } catch (IOException e) {
                throw new RuntimeException("分片文件时发生错误: " + partFilePath, e);
            }
        }
    }

    public static void mergeFile(String splitFileDir, String mergeFilePath) {
        if (splitFileDir == null || mergeFilePath == null) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File targetFile = new File(mergeFilePath);
        if (targetFile.exists()) {
            throw new RuntimeException("需合并的文件已存在: " + mergeFilePath);
        }

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            FileChannel targetChannel = fos.getChannel();
            for (int i = 1; ; i++) {
                String partFilePath = splitFileDir + File.separator + "part" + i + SPLIT_FILE_SUFFIX;
                File partFile = new File(partFilePath);
                if (!partFile.exists()) {
                    break; // 假设分片文件按顺序编号，最后一个不存在则停止
                }
                try (FileInputStream fis = new FileInputStream(partFile);
                     FileChannel channel = fis.getChannel()) {
                    targetChannel.transferFrom(channel, targetChannel.size(), partFile.length());
                    LOGGER.debug("合并完成：{}", partFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("合并文件时发生错误: " + partFilePath, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("合并文件时发生错误: " + mergeFilePath, e);
        }
    }

    public static void deleteSplitFile(String splitFileDir) throws IOException {
        if (splitFileDir == null) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        deleteDirectory(Paths.get(splitFileDir));
    }

    private static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }

        File splitFileDirObj = dir.toFile();
        if (!splitFileDirObj.isDirectory()) {
            throw new IllegalArgumentException("提供的路径不是一个目录: " + dir);
        }

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        String sourceFilePath = "D:\\temp\\zip\\test.zip";
        String splitFileDir = "D:\\temp\\zip\\test";
        String mergeFilePath = "D:\\temp\\zip\\test2.zip";

        System.out.println("开始分片...");
        long partSize = 1024 * 1024; // 1MB
        splitFile(sourceFilePath, splitFileDir, partSize);
        System.out.println("文件分片完成，开始合并...");
        mergeFile(splitFileDir, mergeFilePath);
        System.out.println("文件合并完成！");
        deleteSplitFile(splitFileDir);
        System.out.println("分片文件已删除！");
    }
}
