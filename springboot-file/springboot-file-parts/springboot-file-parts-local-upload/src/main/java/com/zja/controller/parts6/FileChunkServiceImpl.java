package com.zja.controller.parts6;

import com.zja.controller.parts6.config.FileStorageConfig;
import com.zja.controller.parts6.request.ChunkUploadStatusRequest;
import com.zja.controller.parts6.request.FileChunkMergeRequest;
import com.zja.controller.parts6.request.FileChunkUploadRequest;
import com.zja.controller.parts6.request.FileQueryRequest;
import com.zja.controller.parts6.vo.ChunkUploadStatusVO;
import com.zja.controller.parts6.vo.FileChunkMergeResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用文件分片上传服务实现层
 *
 * @author: zhengja
 * @since: 2026/04/23 10:00:00
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FileChunkServiceImpl implements FileChunkService {

    private final FileStorageConfig fileStorageConfig;

    @Override
    public void uploadChunk(FileChunkUploadRequest request, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("分片文件不能为空");
        }

        String fileIdentifier = request.getFileIdentifier();
        Integer chunkNumber = request.getChunkNumber();
        Integer totalChunks = request.getTotalChunks();

        // 参数校验
        if (chunkNumber < 1 || chunkNumber > totalChunks) {
            throw new IllegalArgumentException("分片序号不合法");
        }

        try {
            // 创建临时目录（使用fileIdentifier作为目录名，防止路径遍历）
            Path tempDir = getChunkTempDir(fileIdentifier);
            
            // 检查分片是否已存在（幂等性支持，提前判断避免不必要的IO）
            Path chunkPath = tempDir.resolve(String.valueOf(chunkNumber));
            if (Files.exists(chunkPath)) {
                log.info("分片已存在，跳过上传，fileIdentifier: {}, chunkNumber: {}", 
                    fileIdentifier, chunkNumber);
                return;
            }
            
            // 确保目录存在（只在分片不存在时才创建目录）
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }

            // 保存分片文件（以分片序号命名）
            Files.copy(file.getInputStream(), chunkPath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            log.info("分片上传成功，fileIdentifier: {}, chunkNumber: {}/{}, fileName: {}",
                    fileIdentifier, chunkNumber, totalChunks, request.getFileName());

        } catch (IOException e) {
            log.error("分片上传失败，fileIdentifier: {}, chunkNumber: {}", fileIdentifier, chunkNumber, e);
            throw new RuntimeException("分片上传失败: " + e.getMessage());
        }
    }

    @Override
    public FileChunkMergeResultVO mergeChunks(FileChunkMergeRequest request) {
        String fileIdentifier = request.getFileIdentifier();
        String fileName = request.getFileName();
        Integer totalChunks = request.getTotalChunks();
        
        // 参数校验
        if (totalChunks == null || totalChunks <= 0) {
            log.warn("总分片数不合法: {}", totalChunks);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .message("总分片数不合法")
                    .build();
        }

        // 文件名安全检查，防止路径遍历攻击
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            log.warn("非法文件名: {}", fileName);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .message("文件名不合法")
                    .build();
        }

        Path tempDir = getChunkTempDir(fileIdentifier);
        Path mergedFilePath = tempDir.resolve(fileName);

        try {
            // 检查所有分片是否存在
            validateAllChunksExist(fileIdentifier, totalChunks);

            // 合并分片
            mergeChunksToFile(fileIdentifier, totalChunks, mergedFilePath);

            // 获取文件大小
            long fileSize = Files.size(mergedFilePath);

            // 构建文件访问路径（相对路径，用于后续访问）
            String filePath = buildFilePath(fileIdentifier, fileName);

            log.info("文件分片合并成功，fileIdentifier: {}, fileName: {}, fileSize: {}",
                    fileIdentifier, fileName, fileSize);

            return FileChunkMergeResultVO.builder()
                    .success(true)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileSize(fileSize)
                    .filePath(filePath)
                    .fileExists(true)
                    .message("文件合并成功")
                    .build();

        } catch (Exception e) {
            log.error("文件分片合并失败，fileIdentifier: {}", fileIdentifier, e);
            // 清理已合并的部分文件
            cleanupMergedFile(mergedFilePath);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .message("文件合并失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public FileChunkMergeResultVO getFileInfo(FileQueryRequest request) {
        String fileIdentifier = request.getFileIdentifier();
        String fileName = request.getFileName();

        // 文件名安全检查，防止路径遍历攻击
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            log.warn("非法文件名: {}", fileName);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileExists(false)
                    .message("文件名不合法")
                    .build();
        }

        // fileIdentifier格式校验
        if (!fileIdentifier.matches("^[a-zA-Z0-9_-]+$")) {
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileExists(false)
                    .message("文件标识符格式不合法")
                    .build();
        }

        Path filePath = getChunkTempDir(fileIdentifier).resolve(fileName);

        // 检查文件是否存在
        if (!Files.exists(filePath)) {
            log.info("文件不存在，fileIdentifier: {}, fileName: {}", fileIdentifier, fileName);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileExists(false)
                    .message("文件不存在")
                    .build();
        }

        try {
            // 获取文件大小
            long fileSize = Files.size(filePath);

            log.info("文件查询成功，fileIdentifier: {}, fileName: {}, fileSize: {}",
                    fileIdentifier, fileName, fileSize);

            return FileChunkMergeResultVO.builder()
                    .success(true)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileSize(fileSize)
                    .filePath(filePath.toString())
                    .fileExists(true)
                    .message("文件存在")
                    .build();

        } catch (IOException e) {
            log.error("获取文件信息失败，fileIdentifier: {}, fileName: {}", fileIdentifier, fileName, e);
            return FileChunkMergeResultVO.builder()
                    .success(false)
                    .fileIdentifier(fileIdentifier)
                    .fileName(fileName)
                    .fileExists(false)
                    .message("获取文件信息失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ChunkUploadStatusVO getUploadStatus(ChunkUploadStatusRequest request) {
        String fileIdentifier = request.getFileIdentifier();
        String fileName = request.getFileName();
        Integer totalChunks = request.getTotalChunks();

        // 参数校验
        if (totalChunks == null || totalChunks <= 0) {
            throw new IllegalArgumentException("总分片数必须大于0");
        }

        Path tempDir = getChunkTempDir(fileIdentifier);
        List<Integer> uploadedChunks = new ArrayList<>(totalChunks); // 预分配容量

        // 检查已上传的分片（优化：提前终止如果已找到所有分片）
        for (int i = 1; i <= totalChunks; i++) {
            Path chunkPath = tempDir.resolve(String.valueOf(i));
            if (Files.exists(chunkPath)) {
                uploadedChunks.add(i);
            }
        }

        boolean isComplete = uploadedChunks.size() == totalChunks;

        log.info("查询分片上传状态，fileIdentifier: {}, fileName: {}, 已上传: {}/{}, 完成: {}",
                fileIdentifier, fileName, uploadedChunks.size(), totalChunks, isComplete);

        return ChunkUploadStatusVO.builder()
                .fileIdentifier(fileIdentifier)
                .fileName(fileName)
                .totalChunks(totalChunks)
                .uploadedChunks(uploadedChunks)
                .uploadedCount(uploadedChunks.size())
                .isComplete(isComplete)
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 4 * * ?") // 定时任务，每天凌晨4点执行
    public void cleanupExpiredFiles() {
        log.info("开始清理过期的分片临时文件...");

        Path baseTempDir = Paths.get(fileStorageConfig.getChunkTempPath());

        if (!Files.exists(baseTempDir)) {
            log.info("临时文件目录不存在: {}", baseTempDir);
            return;
        }

        try {
            AtomicInteger cleanedCount = new AtomicInteger(0);
            long expireTimeMillis = System.currentTimeMillis() - (fileStorageConfig.getChunkExpireHours() * 3600 * 1000L);

            // 遍历所有分片目录，只删除超过过期时间的目录
            try (java.util.stream.Stream<Path> stream = Files.list(baseTempDir)) {
                stream.filter(Files::isDirectory)
                    .forEach(dir -> {
                        try {
                            // 检查目录的最后修改时间
                            long lastModified = Files.getLastModifiedTime(dir).toMillis();
                            
                            // 如果超过过期时间，则删除
                            if (lastModified < expireTimeMillis) {
                                deleteDirectoryRecursively(dir);
                                int count = cleanedCount.incrementAndGet();
                                log.info("已清理过期临时文件目录: {}, 最后修改时间: {}, 累计清理: {}", 
                                    dir.getFileName(), new Date(lastModified), count);
                            }
                        } catch (IOException e) {
                            log.warn("清理临时文件目录失败: {}", dir, e);
                        }
                    });
            }

            log.info("分片临时文件清理完成，共清理 {} 个过期目录", cleanedCount.get());

        } catch (IOException e) {
            log.error("清理分片临时文件失败", e);
        }
    }

    @Override
    public void downloadFile(FileQueryRequest request, HttpServletResponse response) {
        String fileIdentifier = request.getFileIdentifier();
        String fileName = request.getFileName();

        // 文件名安全检查
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("文件名不合法");
        }

        // fileIdentifier格式校验
        if (!fileIdentifier.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("文件标识符格式不合法");
        }

        Path filePath = getChunkTempDir(fileIdentifier).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("文件不存在");
        }

        try {
            long fileSize = Files.size(filePath);

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(fileSize));

            // 流式写入响应
            try (InputStream is = Files.newInputStream(filePath);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }

            log.info("文件下载成功，fileIdentifier: {}, fileName: {}, fileSize: {}",
                    fileIdentifier, fileName, fileSize);

        } catch (IOException e) {
            log.error("文件下载失败，fileIdentifier: {}, fileName: {}", fileIdentifier, fileName, e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 验证所有分片是否存在（优化：批量检查，快速失败）
     */
    private void validateAllChunksExist(String fileIdentifier, int totalChunks) {
        Path tempDir = getChunkTempDir(fileIdentifier);
        
        // 先检查第一个和最后一个分片，快速发现常见问题
        Path firstChunk = tempDir.resolve("1");
        if (!Files.exists(firstChunk)) {
            throw new RuntimeException("分片文件不存在，序号: 1, 总分片数: " + totalChunks);
        }
        
        Path lastChunk = tempDir.resolve(String.valueOf(totalChunks));
        if (!Files.exists(lastChunk)) {
            throw new RuntimeException("分片文件不存在，序号: " + totalChunks + ", 总分片数: " + totalChunks);
        }
        
        // 检查中间的分片
        for (int i = 2; i < totalChunks; i++) {
            Path chunkPath = tempDir.resolve(String.valueOf(i));
            if (!Files.exists(chunkPath)) {
                throw new RuntimeException("分片文件不存在，序号: " + i + ", 总分片数: " + totalChunks);
            }
        }
        
        log.debug("所有分片验证通过，fileIdentifier: {}, 总分片数: {}", fileIdentifier, totalChunks);
    }

    /**
     * 获取分片临时目录
     */
    private Path getChunkTempDir(String fileIdentifier) {
        // 确保fileIdentifier只包含安全字符（字母、数字、下划线、连字符）
        if (!fileIdentifier.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("文件标识符格式不合法");
        }
        return Paths.get(fileStorageConfig.getChunkTempPath(), fileIdentifier);
    }

    /**
     * 合并分片到文件（使用NIO缓冲通道，提升大文件性能）
     */
    private void mergeChunksToFile(String fileIdentifier, int totalChunks, Path mergedFilePath) throws IOException {
        Path tempDir = getChunkTempDir(fileIdentifier);

        // 使用FileChannel进行高效文件传输
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(mergedFilePath.toFile(), true);
             java.nio.channels.FileChannel outputChannel = fos.getChannel()) {
            
            for (int i = 1; i <= totalChunks; i++) {
                Path chunkPath = tempDir.resolve(String.valueOf(i));
                if (!Files.exists(chunkPath)) {
                    throw new RuntimeException("分片文件不存在: " + i);
                }

                // 使用FileChannel.transferFrom实现零拷贝传输
                try (java.io.FileInputStream fis = new java.io.FileInputStream(chunkPath.toFile());
                     java.nio.channels.FileChannel inputChannel = fis.getChannel()) {
                    long size = inputChannel.size();
                    long transferred = 0;
                    
                    // 循环确保所有数据都被传输
                    while (transferred < size) {
                        long bytesTransferred = outputChannel.transferFrom(inputChannel, outputChannel.position(), size - transferred);
                        if (bytesTransferred <= 0) {
                            break; // 防止无限循环
                        }
                        transferred += bytesTransferred;
                    }
                }

                log.debug("合并分片: {}/{}", i, totalChunks);
            }
            outputChannel.force(false); // 强制刷新到磁盘
        }

        log.info("分片合并完成，fileIdentifier: {}, 总分片数: {}", fileIdentifier, totalChunks);
    }

    /**
     * 构建文件访问路径
     */
    private String buildFilePath(String fileIdentifier, String fileName) {
        Path filePath = getChunkTempDir(fileIdentifier).resolve(fileName);
        return filePath.toString();
    }

    /**
     * 递归删除目录
     */
    private void deleteDirectoryRecursively(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (java.util.stream.Stream<Path> stream = Files.walk(directory)) {
                stream.sorted((a, b) -> b.compareTo(a)) // 先删除文件，再删除目录
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.warn("删除文件失败: {}", path, e);
                        }
                    });
            }
        }
    }

    /**
     * 清理合并失败的文件
     */
    private void cleanupMergedFile(Path mergedFilePath) {
        try {
            if (Files.exists(mergedFilePath)) {
                Files.delete(mergedFilePath);
                log.debug("已清理合并失败的文件: {}", mergedFilePath);
            }
        } catch (IOException e) {
            log.warn("清理合并文件失败: {}", mergedFilePath, e);
        }
    }
}
