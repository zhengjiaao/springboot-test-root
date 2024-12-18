package com.zja.service.parts;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 此类提供上传和合并文件块的方法。
 */
@Slf4j
@Service
public class ChunkUploadService {

    // 上传块文件存储目录
    private static final String UPLOAD_CHUNK_DIR = "uploads_chunk";
    // 上传的块文件合并目录
    private static final String UPLOAD_MERGE_DIR = "uploads";

    /**
     * 上传具有给定参数的文件块。
     *
     * @param identifier  文件标识符
     * @param file        要上传的块文件
     * @param chunkNumber 块编号
     * @param totalChunks 总块数
     * @param fileName    文件名称
     */
    public void upload(String identifier, MultipartFile file, Integer chunkNumber, Integer totalChunks, String fileName) throws IOException {
        validateUploadParams(file, chunkNumber, identifier, fileName, totalChunks);

        Path chunkDirPath = Paths.get(UPLOAD_CHUNK_DIR, identifier);
        if (isUploadComplete(chunkDirPath, totalChunks)) {
            log.info("所有块已上传完成，无需再次上传.");
            return;
        }

        Path chunkFilePath = Paths.get(UPLOAD_CHUNK_DIR, identifier, String.valueOf(chunkNumber));
        try {
            Files.createDirectories(chunkFilePath.getParent());
            if (Files.notExists(chunkFilePath)) {
                // 块文件不存在时，进行保存
                file.transferTo(chunkFilePath);
                log.debug("块文件保存成功. chunkPath={}", chunkFilePath);
            } else if (file.getSize() != Files.size(chunkFilePath)) {
                // 块文件大小不一致时抛异常
                log.error("块文件大小不一致，存在脏数据，请先根据identifier删除文件. chunkPath={}", chunkFilePath);
                delete(identifier);
                throw new RuntimeException("块文件大小不一致，存在脏数据，请先重新上传文件.");
            } else {
                // 块文件已存在
                log.warn("块文件已存在. chunkPath={}", chunkFilePath);
            }
        } catch (IOException e) {
            log.error("上传块文件时错误.", e);
            throw new RuntimeException("上传块文件时错误.", e);
        }
    }

    /**
     * 合并所有已上传的块为单个文件。
     *
     * @param identifier  文件标识符
     * @param fileName    文件名称
     * @param totalSize   文件总大小
     * @param totalChunks 总块数
     */
    public void merge(String identifier, String fileName, long totalSize, Integer totalChunks) throws IOException {
        validateMergeParams(identifier, fileName, totalSize, totalChunks);

        Path chunkDirPath = Paths.get(UPLOAD_CHUNK_DIR, identifier);
        Path mergeFilePath = Paths.get(UPLOAD_MERGE_DIR, identifier, fileName);

        // 在合并之前校验
        verifyMerge(identifier, totalSize, totalChunks, chunkDirPath, mergeFilePath);
        if (Files.exists(mergeFilePath) && totalSize == Files.size(mergeFilePath)) {
            log.warn("已存在合并文件，不再进行合并操作.");
            return;
        }

        Files.createDirectories(mergeFilePath.getParent());

        try (OutputStream out = Files.newOutputStream(mergeFilePath);
             Stream<Path> stream = Files.list(chunkDirPath)) {
            log.debug("开始合并块文件. chunkDirPath={}", chunkDirPath);
            stream.filter(path -> !Files.isDirectory(path))
                    .sorted(Comparator.comparingInt(path -> Integer.parseInt(path.getFileName().toString())))
                    .forEachOrdered(path -> {
                        try (InputStream in = Files.newInputStream(path)) {
                            IOUtils.copy(in, out);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            log.debug("合并块文件成功. mergePath={}", mergeFilePath);
        } catch (IOException e) {
            log.error("合并块文件时错误.", e);
            throw new RuntimeException("合并块文件时错误.", e);
        }
    }

    /**
     * 获取合并文件。
     *
     * @param identifier 文件标识符
     * @return 合并文件
     */
    public File getMergeFile(String identifier) {
        Path mergeDirPath = Paths.get(UPLOAD_MERGE_DIR, identifier);
        if (Files.notExists(mergeDirPath)) {
            throw new RuntimeException("未找到合并文件目录.");
        }

        try (Stream<Path> stream = Files.list(mergeDirPath)) {
            Optional<Path> firstFile = stream.filter(path -> !Files.isDirectory(path)).sorted()
                    .findFirst();

            if (firstFile.isPresent() && firstFile.get().toFile().exists()) {
                return firstFile.get().toFile();
            }

            log.warn("未找到合并文件: identifier={}", identifier);
            return null;
        } catch (IOException e) {
            log.warn("未找到合并文件.");
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除给定标识符的文件块和合并文件。
     *
     * @param identifier 文件标识符
     */
    public void delete(String identifier) {
        Path chunkDirPath = Paths.get(UPLOAD_CHUNK_DIR, identifier);
        Path mergePath = Paths.get(UPLOAD_MERGE_DIR, identifier);
        deleteIfExists(chunkDirPath);
        deleteIfExists(mergePath);
    }

    // 私有方法

    private void validateUploadParams(MultipartFile file, Integer chunkNumber, String identifier, String fileName, Integer totalChunks) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is empty.");
        }
        if (file.getSize() > 1024 * 1024 * 10) {
            throw new IllegalArgumentException("file size is greater than 10MB.");
        }
        if (chunkNumber < 1) {
            throw new IllegalArgumentException("chunkNumber is less than 1.");
        }
        if (totalChunks < 1) {
            throw new IllegalArgumentException("totalChunks is less than 1.");
        }
        if (chunkNumber > totalChunks) {
            throw new IllegalArgumentException("chunkNumber is greater than totalChunks.");
        }
        if (StringUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty.");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName is empty.");
        }
    }

    private void validateMergeParams(String identifier, String fileName, long totalSize, Integer totalChunks) {
        if (totalChunks < 1) {
            throw new IllegalArgumentException("totalChunks is less than 1.");
        }
        if (totalSize < 1) {
            throw new IllegalArgumentException("totalSize is less than 1.");
        }
        if (StringUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty.");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName is empty.");
        }
    }

    private void verifyMerge(String identifier, long totalSize, Integer totalChunks, Path chunkDirPath, Path mergeFilePath) throws IOException {
        if (notUploadedChunk(identifier)) {
            throw new RuntimeException("未上传块，请先上传块文件.");
        }
        if (isUploadIncomplete(chunkDirPath, totalChunks)) {
            throw new RuntimeException("并非所有块都已上传，请先上传全部块文件.");
        }
        if (Files.exists(mergeFilePath) && totalSize != Files.size(mergeFilePath)) {
            log.error("合并文件大小不一致，存在脏数据，请先根据identifier删除文件.mergeFilePath={}", mergeFilePath);
            delete(identifier);
            throw new RuntimeException("合并文件大小不一致，存在脏数据，请先重新上传文件.");
        }
    }

    private void deleteIfExists(Path dirPath) {
        try (Stream<Path> stream = Files.walk(dirPath)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            dirPath.toFile().delete();
        } catch (IOException e) {
            log.error("删除文件时错误.", e);
        }
    }

    private boolean notUploadedChunk(String identifier) {
        Path chunkDirPath = Paths.get(UPLOAD_CHUNK_DIR, identifier);
        return Files.notExists(chunkDirPath);
    }

    private boolean isUploadIncomplete(Path chunkDirPath, Integer totalChunks) {
        return !isUploadComplete(chunkDirPath, totalChunks);
    }

    private boolean isUploadComplete(Path chunkDirPath, Integer totalChunks) {
        if (!chunkDirPath.toFile().exists()) {
            return false;
        }

        try (Stream<Path> stream = Files.list(chunkDirPath).filter(path -> !Files.isDirectory(path))) {
            long count = stream.count();
            return count == totalChunks;
        } catch (IOException e) {
            log.error("检查上传完成时出错.", e);
            return false;
        }
    }
}
