package com.zja.controller.parts3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @Author: zhengja
 * @Date: 2024-09-14 9:59
 */
@Slf4j
@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";
    private static final int TOTAL_CHUNKS = 5; // 假设总共有5个分片

    public void createUploadSession(String fileId) {
        // 可以在数据库或缓存中记录文件上传的会话信息，如文件名、大小、上传时间等
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
    }

    public void mergeFile(String fileId) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR + fileId);
        File mergedFile = new File(UPLOAD_DIR + fileId + ".merged");

        for (int i = 0; i < TOTAL_CHUNKS; i++) {
            Files.write(mergedFile.toPath(), Files.readAllBytes(uploadPath.resolve(i + ".part")), java.nio.file.StandardOpenOption.APPEND);
        }

        // 删除临时分片文件
        Files.walk(uploadPath)
                .sorted(java.util.Comparator.reverseOrder())
                .map(java.nio.file.Path::toFile)
                .forEach(java.io.File::delete);
    }

    public int calculateUploadProgress(String fileId) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR + fileId);
        long uploadedChunks;
        try (Stream<Path> stream = Files.list(uploadPath)) {
            uploadedChunks = stream.count();
        } catch (IOException e) {
            log.error("计算上传进度失败", e);
            throw new IOException("计算上传进度失败.", e);
        }
        return Math.toIntExact((uploadedChunks * 100) / TOTAL_CHUNKS);
    }
}