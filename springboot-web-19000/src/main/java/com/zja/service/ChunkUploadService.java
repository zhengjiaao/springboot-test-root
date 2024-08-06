package com.zja.service;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * 此类提供上传和合并文件块的方法。
 */
@Service
public class ChunkUploadService {

    /**
     * 上传具有给定参数的文件块。
     *
     * @param file 要上传的块文件
     * @param chunkNumber 正在上传的块的编号
     * @param totalChunks 文件分成的总块数
     * @param identifier 正在上传的文件的标识符
     * @param filename 正在上传的文件的名称
     * @throws IOException 如果发生I/O错误
     */
    public void upload(MultipartFile file, Integer chunkNumber, Integer totalChunks, String identifier, String filename) throws IOException {
        Path chunkPath = Paths.get("uploads", identifier, chunkNumber.toString());
        Files.createDirectories(chunkPath.getParent());
        if (!Files.exists(chunkPath)) {
            Files.write(chunkPath, file.getBytes());
        } else {
            // 处理块文件已存在的情况
        }
    }

    /**
     * 此方法将所有已上传的块合并为给定文件标识符和文件名的单个文件。
     * 如果没有上传所有块，则返回而不进行合并。
     * @param identifier 正在上传的文件的标识符
     * @param filename 正在上传的文件的名称
     * @param totalChunks 文件分成的总块数
     * @throws IOException 如果发生I/O错误
     */
    public void merge(String identifier, String filename, Integer totalChunks) throws IOException {
        Path dirPath = Paths.get("uploads", identifier);
        Path filePath = Paths.get("uploads", filename);

        if (!isUploadComplete(dirPath, totalChunks)) {
            // 处理未上传所有块的情况
            System.err.println("Not all chunks uploaded.");
            return;
        }

        try (OutputStream out = Files.newOutputStream(filePath)) {
            Files.list(dirPath)
                    .filter(path -> !Files.isDirectory(path))
                    .sorted(Comparator.comparingInt(path -> Integer.parseInt(path.getFileName().toString())))
                    .forEachOrdered(path -> {
                        try (InputStream in = Files.newInputStream(path)) {
                            IOUtils.copy(in, out);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }

    /**
     * 此方法检查是否已上传给定文件标识符和总块数的所有块。
     * 如果已上传所有块，则返回true。否则，返回false。
     * @param dirPath 块文件目录存储路径
     * @param totalChunks 文件分成的总块数
     * @return 如果已上传所有块，则为true，否则为false
     * @throws IOException 如果发生I/O错误
     */
    public boolean isUploadComplete(Path dirPath, Integer totalChunks) throws IOException {
        long count = Files.list(dirPath)
                .filter(path -> !Files.isDirectory(path))
                .count();
        return count == totalChunks;
    }
}
