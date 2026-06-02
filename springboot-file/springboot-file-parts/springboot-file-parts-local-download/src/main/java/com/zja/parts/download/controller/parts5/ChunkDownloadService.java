package com.zja.parts.download.controller.parts5;

import com.zja.parts.download.controller.parts5.dto.FileInfoResult;
import com.zja.parts.download.controller.parts5.dto.FileItemResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 大文件分片下载服务 - 支持断点续传、分片下载、MD5校验
 *
 * <p>核心流程：
 * <ol>
 *   <li>前端查询文件信息（大小、分片数、MD5）</li>
 *   <li>前端根据文件大小和分片策略，逐片请求下载</li>
 *   <li>服务端通过 Range 请求头或显式分片参数返回指定字节范围</li>
 *   <li>前端将所有分片合并为完整文件，可选 MD5 校验完整性</li>
 * </ol>
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Slf4j
@Service
public class ChunkDownloadService {

    /**
     * 下载文件存储根目录
     */
    @Value("${parts5-download.file-dir:D://FileTest//parts5-download}")
    private String fileBaseDir;

    /**
     * 默认分片大小: 5MB
     */
    @Value("${parts5-download.chunk-size:5242880}")
    private long defaultChunkSize;
    
    /**
     * 单次请求分片大小上限: 50MB
     */
    private static final long MAX_CHUNK_SIZE = 50L * 1024 * 1024;
    
    /**
     * 合法的文件名：禁止路径分隔符和特殊字符
     */
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[^/\\:*?\"<>|]+$");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(fileBaseDir));
            log.info("分片下载目录初始化完成: fileDir={}, chunkSize={}MB", fileBaseDir, defaultChunkSize / (1024 * 1024));
        } catch (IOException e) {
            log.error("初始化下载目录失败", e);
            throw new RuntimeException("初始化下载目录失败", e);
        }
    }

    /**
     * 获取可下载文件列表
     *
     * @return 文件列表
     */
    public List<FileItemResult> listFiles() {
        Path dir = Paths.get(fileBaseDir);
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }

        List<FileItemResult> result = new ArrayList<>();
        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                            String fileName = path.getFileName().toString();
                            result.add(FileItemResult.builder()
                                    .fileName(fileName)
                                    .fileSize(attrs.size())
                                    .fileSizeFormatted(formatFileSize(attrs.size()))
                                    .lastModified(attrs.lastModifiedTime().toMillis())
                                    .build());
                        } catch (IOException e) {
                            log.warn("读取文件属性失败: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("读取文件列表失败", e);
        }

        result.sort(Comparator.comparingLong(FileItemResult::getLastModified).reversed());
        return result;
    }

    /**
     * 获取文件信息（大小、分片数、MD5）
     *
     * @param fileName 文件名
     * @return 文件信息
     */
    public FileInfoResult getFileInfo(String fileName) {
        validateFileName(fileName);
        Path filePath = resolveFilePath(fileName);

        if (!Files.exists(filePath)) {
            return FileInfoResult.builder()
                    .fileName(fileName)
                    .exists(false)
                    .build();
        }

        try {
            long fileSize = Files.size(filePath);
            long totalChunks = (long) Math.ceil((double) fileSize / defaultChunkSize);

            return FileInfoResult.builder()
                    .fileName(fileName)
                    .fileSize(fileSize)
                    .chunkSize(defaultChunkSize)
                    .totalChunks(totalChunks)
                    .exists(true)
                    .build();
        } catch (IOException e) {
            log.error("获取文件信息失败: {}", fileName, e);
            throw new RuntimeException("获取文件信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取指定范围的文件字节（分片下载核心方法）
     *
     * @param fileName 文件名
     * @param start    起始字节位置（含）
     * @param end      结束字节位置（含）
     * @return 字节数组
     */
    public byte[] readChunk(String fileName, long start, long end) throws IOException {
        validateFileName(fileName);
        Path filePath = resolveFilePath(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("文件不存在: " + fileName);
        }

        long fileSize = Files.size(filePath);

        // 参数校验
        if (start < 0 || start >= fileSize) {
            throw new IllegalArgumentException("起始位置无效: start=" + start + ", fileSize=" + fileSize);
        }
        if (end < start || end >= fileSize) {
            end = fileSize - 1;
        }

        int length = (int) (end - start + 1);

        // 分片大小上限校验，防止OOM
        if (length > MAX_CHUNK_SIZE) {
            throw new IllegalArgumentException("单次请求分片大小不能超过 " + MAX_CHUNK_SIZE + " 字节, 当前请求: " + length + " 字节");
        }

        ByteBuffer buffer = ByteBuffer.allocate(length);

        // 使用 FileChannel 高效随机读取（避免 MappedByteBuffer 直接内存不可控问题）
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r");
             FileChannel channel = raf.getChannel()) {
            channel.position(start);
            channel.read(buffer);
        }

        return buffer.array();
    }

    /**
     * 计算文件 MD5 校验值（用于下载完整性校验）
     * <p>注意：大文件 MD5 计算可能较慢，建议前端按需调用
     *
     * @param fileName 文件名
     * @return MD5 十六进制字符串
     */
    public String calculateMD5(String fileName) {
        validateFileName(fileName);
        Path filePath = resolveFilePath(fileName);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("文件不存在: " + fileName);
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[65536]; // 64KB缓冲区，大文件更高效
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }

            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("计算MD5失败: {}", fileName, e);
            throw new RuntimeException("计算MD5失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取文件总大小
     *
     * @param fileName 文件名
     * @return 文件大小，不存在返回 -1
     */
    public long getFileSize(String fileName) {
        validateFileName(fileName);
        Path filePath = resolveFilePath(fileName);

        if (!Files.exists(filePath)) {
            return -1;
        }

        try {
            return Files.size(filePath);
        } catch (IOException e) {
            log.error("获取文件大小失败: {}", fileName, e);
            return -1;
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String fileName) {
        validateFileName(fileName);
        return Files.exists(resolveFilePath(fileName));
    }

    /**
     * 获取文件的 FileSystemResource（供 Controller 流式传输使用）
     *
     * @param fileName 文件名
     * @return FileSystemResource，文件不存在时返回 null
     */
    public FileSystemResource getFileResource(String fileName) {
        validateFileName(fileName);
        Path filePath = resolveFilePath(fileName);
        if (!Files.exists(filePath)) {
            return null;
        }
        return new FileSystemResource(filePath.toFile());
    }

    /**
     * 解析文件路径（私有，防止绕过校验直接调用）
     * <p>增加路径规范化校验，防止路径穿越攻击
     */
    private Path resolveFilePath(String fileName) {
        Path resolved = Paths.get(fileBaseDir, fileName).normalize();
        if (!resolved.startsWith(Paths.get(fileBaseDir).normalize())) {
            throw new IllegalArgumentException("非法路径访问: " + fileName);
        }
        return resolved;
    }

    // ==================== 私有方法 ====================

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

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1fKB", size / 1024.0);
        } else if (size < 1024L * 1024 * 1024) {
            return String.format("%.1fMB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2fGB", size / (1024.0 * 1024 * 1024));
        }
    }
}
