package com.zja.service.parts1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * @author: zhengja
 * @since: 2024/01/19 16:28
 */
public interface LargeFilePartsUpload {

    void upload(InputStream inputStream, String fileName, long fileSize, long chunkSize,
                long totalChunks, long currentChunk, String uploadDir, String uploadId) throws IOException;

    default void handleUpload(InputStream inputStream, String fileName, long fileSize, long chunkSize,
                              long totalChunks, long currentChunk, String uploadDir, String uploadId) throws IOException {
        // 创建用于保存分块文件的目录
        Path chunkDirectory = createChunkDirectory(uploadDir, uploadId);

        // 保存分块文件
        saveChunkFile(inputStream, chunkDirectory, currentChunk, fileName);

        // 检查是否已接收到所有分块
        if (isAllChunksReceived(chunkDirectory, totalChunks)) {
            // 合并分块文件
            mergeChunks(chunkDirectory, uploadDir, fileName);
            // 执行上传完成后的逻辑
            handleUploadComplete(uploadDir, uploadId, fileName);
        }
    }

    default Path createChunkDirectory(String uploadDir, String uploadId) throws IOException {
        Path chunkDirectory = Paths.get(uploadDir, uploadId);
        // Path chunkDirectory = Paths.get(uploadDir, uploadId, "parts"); // todo 分块固定存储目录：parts
        Files.createDirectories(chunkDirectory);
        return chunkDirectory;
    }

    default void saveChunkFile(InputStream inputStream, Path chunkDirectory, long currentChunk, String fileName) throws IOException {
        Path chunkFilePath = Paths.get(chunkDirectory.toString(), currentChunk + "_" + fileName);
        Files.copy(inputStream, chunkFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    default boolean isAllChunksReceived(Path chunkDirectory, long totalChunks) throws IOException {
        return Files.list(chunkDirectory).count() == totalChunks;
    }

    default void mergeChunks(Path chunkDirectory, String uploadDir, String fileName) throws IOException {
        Path mergedFilePath = Paths.get(uploadDir, fileName);
        if (Files.exists(mergedFilePath)) {
            // 删除现有的文件再重新创建
            Files.delete(mergedFilePath);
        }
        // 创建目标文件
        Files.createFile(mergedFilePath);
        // 合并分块文件
        Files.list(chunkDirectory)
                .sorted()
                .forEach(chunkFile -> {
                    try {
                        Files.write(mergedFilePath, Files.readAllBytes(chunkFile), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        throw new RuntimeException("合并文件失败. chunkDirectory=" + chunkDirectory + ",mergedFilePath=" + mergedFilePath);
                    }
                });
    }

    void handleUploadComplete(String uploadDir, String uploadId, String fileName) throws IOException;
}
