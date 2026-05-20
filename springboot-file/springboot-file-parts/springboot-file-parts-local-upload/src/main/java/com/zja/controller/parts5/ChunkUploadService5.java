package com.zja.controller.parts5;

import com.zja.controller.parts5.dto.ChunkCheckResult;
import com.zja.controller.parts5.dto.MergeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 大文件分片上传服务 - 支持断点续传、秒传、文件合并与下载
 *
 * <p>核心流程：
 * <ol>
 *   <li>前端计算文件MD5作为唯一标识(identifier)</li>
 *   <li>检查该文件是否已上传(秒传)或部分上传(断点续传)</li>
 *   <li>逐片上传，服务端校验并持久化每个分片</li>
 *   <li>全部分片上传完成后，合并为完整文件</li>
 * </ol>
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Slf4j
@Service
public class ChunkUploadService5 {

    /**
     * 分片临时存储根目录
     */
    @Value("${parts5.chunk-dir:D://FileTest//parts5//chunks}")
    private String chunkBaseDir;

    /**
     * 合并后文件存储根目录
     */
    @Value("${parts5.merge-dir:D://FileTest//parts5//merged}")
    private String mergeBaseDir;

    /**
     * 合法的 identifier 格式：仅允许十六进制字符（MD5值，32位）
     */
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-fA-F0-9]{32}$");

    /**
     * 合法的文件名：禁止路径分隔符和特殊字符
     */
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[^/\\\\:*?\"<>|]+$");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(chunkBaseDir));
            Files.createDirectories(Paths.get(mergeBaseDir));
            log.info("分片上传目录初始化完成: chunks={}, merged={}", chunkBaseDir, mergeBaseDir);
        } catch (IOException e) {
            log.error("初始化上传目录失败", e);
            throw new RuntimeException("初始化上传目录失败", e);
        }
    }

    /**
     * 检查文件上传状态 - 支持秒传和断点续传
     *
     * @param identifier 文件唯一标识(MD5)
     * @param fileName   文件名
     * @param totalChunks 总分片数
     * @return 检查结果
     */
    public ChunkCheckResult checkChunks(String identifier, String fileName, int totalChunks) {
        // 0. 参数安全校验
        validateIdentifier(identifier);
        validateFileName(fileName);

        // 1. 先检查合并后的文件是否已存在（秒传）
        Path mergedFile = getMergedFilePath(identifier, fileName);
        if (Files.exists(mergedFile)) {
            log.info("文件已存在，支持秒传: identifier={}, fileName={}", identifier, fileName);
            return ChunkCheckResult.builder()
                    .uploaded(true)
                    .uploadedChunks(Collections.emptyList())
                    .fileUrl("/parts5/file/download/" + identifier + "/" + fileName)
                    .build();
        }

        // 2. 检查已上传的分片列表（断点续传）
        Path chunkDir = getChunkDir(identifier);
        List<Integer> uploadedChunks = new ArrayList<>();
        if (Files.exists(chunkDir)) {
            try (Stream<Path> stream = Files.list(chunkDir)) {
                uploadedChunks = stream
                        .filter(p -> p.getFileName().toString().endsWith(".part"))
                        .map(p -> {
                            String name = p.getFileName().toString();
                            return Integer.parseInt(name.replace(".part", ""));
                        })
                        .sorted()
                        .collect(Collectors.toList());
            } catch (IOException e) {
                log.error("读取分片目录失败: {}", chunkDir, e);
            }
        }

        log.info("断点续传检查: identifier={}, 已上传分片数={}/{}", identifier, uploadedChunks.size(), totalChunks);
        return ChunkCheckResult.builder()
                .uploaded(false)
                .uploadedChunks(uploadedChunks)
                .build();
    }

    /**
     * 上传单个分片
     *
     * @param file        分片文件
     * @param chunkNumber 当前分片编号（从1开始）
     * @param totalChunks 总分片数
     * @param identifier  文件唯一标识
     * @param fileName    原始文件名
     */
    public void uploadChunk(MultipartFile file, int chunkNumber, int totalChunks,
                            String identifier, String fileName) throws IOException {
        // 参数安全校验
        validateIdentifier(identifier);
        validateFileName(fileName);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("分片文件不能为空");
        }
        if (chunkNumber < 1 || chunkNumber > totalChunks) {
            throw new IllegalArgumentException("分片编号无效: " + chunkNumber + ", 总分片数: " + totalChunks);
        }

        Path chunkDir = getChunkDir(identifier);
        Files.createDirectories(chunkDir);

        // 保存分片文件（REPLACE_EXISTING 自动覆盖已存在的分片）
        Path chunkFile = chunkDir.resolve(chunkNumber + ".part");
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, chunkFile, StandardCopyOption.REPLACE_EXISTING);
        }

        log.info("分片上传成功: identifier={}, chunk={}/{}, size={}KB",
                identifier, chunkNumber, totalChunks, file.getSize() / 1024);
    }

    /**
     * 合并所有分片为完整文件
     *
     * @param identifier  文件唯一标识
     * @param fileName    原始文件名
     * @param totalChunks 总分片数
     * @param totalSize   文件总大小
     * @return 合并结果
     */
    public MergeResult mergeChunks(String identifier, String fileName, int totalChunks, long totalSize) {
        Path chunkDir = getChunkDir(identifier);
        Path mergedFile = getMergedFilePath(identifier, fileName);

        try {
            // 1. 校验所有分片是否齐全
            for (int i = 1; i <= totalChunks; i++) {
                Path chunkFile = chunkDir.resolve(i + ".part");
                if (!Files.exists(chunkFile)) {
                    String msg = String.format("分片 %d 不存在，无法合并", i);
                    log.error(msg);
                    return MergeResult.builder()
                            .success(false)
                            .message(msg)
                            .build();
                }
            }

            // 2. 确保合并目标目录存在
            Files.createDirectories(mergedFile.getParent());

            // 3. 使用 FileChannel 高效合并分片（NIO零拷贝，循环确保大文件完整传输）
            try (FileOutputStream fos = new FileOutputStream(mergedFile.toFile());
                 FileChannel outChannel = fos.getChannel()) {

                for (int i = 1; i <= totalChunks; i++) {
                    Path chunkFile = chunkDir.resolve(i + ".part");
                    try (FileInputStream fis = new FileInputStream(chunkFile.toFile());
                         FileChannel inChannel = fis.getChannel()) {
                        long size = inChannel.size();
                        long position = 0;
                        while (position < size) {
                            position += inChannel.transferTo(position, size - position, outChannel);
                        }
                    }
                }
            }

            // 4. 校验合并后的文件大小
            long mergedSize = Files.size(mergedFile);
            if (totalSize > 0 && mergedSize != totalSize) {
                log.warn("合并后文件大小不一致: expected={}, actual={}", totalSize, mergedSize);
            }

            // 5. 清理分片文件
            cleanupChunks(identifier);

            String fileUrl = "/parts5/file/download/" + identifier + "/" + fileName;
            log.info("文件合并成功: identifier={}, fileName={}, size={}MB",
                    identifier, fileName, mergedSize / (1024 * 1024));

            return MergeResult.builder()
                    .success(true)
                    .fileName(fileName)
                    .fileSize(mergedSize)
                    .fileUrl(fileUrl)
                    .message("文件合并成功")
                    .build();

        } catch (IOException e) {
            log.error("文件合并失败: identifier={}", identifier, e);
            return MergeResult.builder()
                    .success(false)
                    .message("文件合并失败: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 获取合并后文件的 Path，用于下载
     *
     * @param identifier 文件唯一标识
     * @param fileName   文件名
     * @return 文件路径，不存在则返回 null
     */
    public Path getMergedFileForDownload(String identifier, String fileName) {
        validateIdentifier(identifier);
        validateFileName(fileName);
        Path mergedFile = getMergedFilePath(identifier, fileName);
        if (Files.exists(mergedFile)) {
            return mergedFile;
        }
        return null;
    }

    /**
     * 取消上传，清理分片文件
     *
     * @param identifier 文件唯一标识
     */
    public void cancelUpload(String identifier) {
        validateIdentifier(identifier);
        cleanupChunks(identifier);
        log.info("上传已取消，分片已清理: identifier={}", identifier);
    }

    // ==================== 私有方法 ====================

    /**
     * 获取分片存储目录: {chunkBaseDir}/{identifier}/
     */
    private Path getChunkDir(String identifier) {
        return Paths.get(chunkBaseDir, identifier);
    }

    /**
     * 获取合并后文件路径: {mergeBaseDir}/{identifier}/{fileName}
     */
    private Path getMergedFilePath(String identifier, String fileName) {
        return Paths.get(mergeBaseDir, identifier, fileName);
    }

    /**
     * 清理分片目录
     */
    private void cleanupChunks(String identifier) {
        Path chunkDir = getChunkDir(identifier);
        if (Files.exists(chunkDir)) {
            try (Stream<Path> walk = Files.walk(chunkDir)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                log.warn("删除分片文件失败: {}", path, e);
                            }
                        });
                log.info("分片目录清理完成: {}", chunkDir);
            } catch (IOException e) {
                log.error("清理分片目录失败: {}", chunkDir, e);
            }
        }
    }

    // ==================== 安全校验 ====================

    /**
     * 校验 identifier 格式（必须为32位十六进制 MD5 值）
     */
    private void validateIdentifier(String identifier) {
        if (identifier == null || !IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException("无效的文件标识符(identifier), 应为32位MD5值: " + identifier);
        }
    }

    /**
     * 校验文件名（禁止路径穿越字符）
     */
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        if (!SAFE_FILENAME_PATTERN.matcher(fileName).matches()) {
            throw new IllegalArgumentException("文件名包含非法字符: " + fileName);
        }
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("文件名不允许包含路径穿越字符: " + fileName);
        }
    }
}
