package com.zja.controller.parts4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @Author: zhengja
 * @Date: 2024-09-14 9:59
 */
@Slf4j
//@Service
public class FileService4 {

    private static final String UPLOAD_DIR = "uploads/";
    private static final int TOTAL_CHUNKS = 5; // 假设总共有5个分片
    private Map<String, Integer> chunkStatus = new HashMap<>();

    public String createUploadSession() {
        String fileId = UUID.randomUUID().toString();
        chunkStatus.put(fileId, 0);
        return fileId;
    }

    public void pauseUpload(String fileId) {
        // 可以暂停上传，记录当前分片上传进度
        chunkStatus.put(fileId, chunkStatus.get(fileId));
    }

    public void resumeUpload(String fileId) {
        // 可以继续上传，根据记录的进度继续上传未完成的分片
        int currentChunk = chunkStatus.get(fileId);
        for (int i = currentChunk; i < TOTAL_CHUNKS; i++) {
            // 重新上传未完成的分片
        }
    }

    public void cancelUpload(String fileId) {
        // 可以取消上传，删除已上传的分片文件
        Path uploadPath = Paths.get(UPLOAD_DIR + fileId);
        try {
            Files.walk(uploadPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chunkStatus.remove(fileId);
    }

    public boolean isValidChunk(String fileId, int chunkIndex, MultipartFile fileChunk) {
        // 可根据实际需求进行分片数据的校验，比如校验分片序号、大小、MD5等信息
        return true;
    }

    public void saveChunk(String fileId, int chunkIndex, MultipartFile fileChunk) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR + fileId);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path chunkPath = uploadPath.resolve(chunkIndex + ".part");
        fileChunk.transferTo(chunkPath);

        chunkStatus.put(fileId, chunkStatus.get(fileId) + 1);
    }

    public void mergeFile(String fileId) throws IOException {
        // 合并上传的分片文件
        Path uploadPath = Paths.get(UPLOAD_DIR + fileId);
        Path mergedFilePath = Paths.get(UPLOAD_DIR + fileId + ".mp4");

        try (Stream<Path> stream = Files.list(uploadPath)) {
            stream.sorted()
                    .map(Path::toFile)
                    .forEach(file -> {
                        try {
                            Files.copy(file.toPath(), mergedFilePath, StandardCopyOption.REPLACE_EXISTING);
                       } catch (IOException e) {
                            log.error("合并分片文件失败", e);
                        }
                    });
        }
    }

    public int getChunkUploadProgress(String fileId) {
        return chunkStatus.getOrDefault(fileId, 0);
    }

    public int getTotalUploadProgress(String fileId) {
        int uploadedChunks = chunkStatus.getOrDefault(fileId, 0);
        return (int) ((uploadedChunks * 100) / TOTAL_CHUNKS);
    }
}