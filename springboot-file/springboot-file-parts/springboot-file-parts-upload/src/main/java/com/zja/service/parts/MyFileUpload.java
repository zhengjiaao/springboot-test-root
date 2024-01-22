package com.zja.service.parts;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/19 16:31
 */
@Component
public class MyFileUpload implements LargeFileUpload {

    @Override
    public void upload(InputStream inputStream, String fileName, long fileSize, long chunkSize,
                       long totalChunks, long currentChunk, String uploadDir, String uploadId) throws IOException {
        // 处理上传逻辑
        handleUpload(inputStream, fileName, fileSize, chunkSize, totalChunks, currentChunk, uploadDir, uploadId);
    }

    @Override
    public void handleUploadComplete(String uploadDir, String uploadId, String fileName) throws IOException {
        // 上传完成后的处理逻辑
        System.out.println("文件上传完成，文件名称：" + fileName);
        System.out.println("文件上传完成，存储位置：" + Paths.get(uploadDir, fileName));
        // 可以在这里执行进一步的操作，如文件的后续处理或触发其他业务逻辑

        // 清理分块目录
        // Path chunkDirectory = createChunkDirectory(uploadDir, uploadId);

        // 把文件上传到 MongoDB

        // 清理本地文件
    }
}
