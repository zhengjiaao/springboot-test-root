package com.zja.controller.parts5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 定时清理任务 - 自动清理过期的分片文件和合并文件
 *
 * <p>清理策略：
 * <ul>
 *   <li>分片目录：清理超过指定时间未完成合并的分片（可能是用户中断上传后遗留的）</li>
 *   <li>合并目录：清理超过指定时间的已合并文件（可选，默认不清理）</li>
 * </ul>
 *
 * <p>执行频率：默认每小时执行一次，可通过配置 parts5.cleanup.cron 修改
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Slf4j
@Component
public class ChunkCleanupTask5 {

    @Value("${parts5.chunk-dir:D://FileTest//parts5//chunks}")
    private String chunkBaseDir;

    @Value("${parts5.merge-dir:D://FileTest//parts5//merged}")
    private String mergeBaseDir;

    /**
     * 分片过期时间（小时），默认24小时
     */
    @Value("${parts5.cleanup.chunk-expire-hours:24}")
    private int chunkExpireHours;

    /**
     * 合并文件过期时间（小时），默认72小时（3天），设为0则不清理合并文件
     */
    @Value("${parts5.cleanup.merge-expire-hours:72}")
    private int mergeExpireHours;

    /**
     * 是否启用定时清理
     */
    @Value("${parts5.cleanup.enabled:true}")
    private boolean cleanupEnabled;

    /**
     * 定时清理任务 - 默认每小时执行一次
     * <p>可通过 parts5.cleanup.cron 配置修改执行频率
     */
    @Scheduled(cron = "${parts5.cleanup.cron:0 0 * * * ?}")
    public void scheduledCleanup() {
        if (!cleanupEnabled) {
            return;
        }
        log.info("===== 开始执行定时清理任务 =====");
        long startTime = System.currentTimeMillis();

        int chunksDeleted = cleanupExpiredChunks();
        int mergedDeleted = cleanupExpiredMergedFiles();

        long elapsed = System.currentTimeMillis() - startTime;
        log.info("===== 定时清理任务完成: 清理分片目录={}个, 清理合并目录={}个, 耗时={}ms =====",
                chunksDeleted, mergedDeleted, elapsed);
    }

    /**
     * 清理过期的分片目录
     * <p>遍历 chunkBaseDir 下的所有子目录，检查目录的最后修改时间，
     * 如果超过 chunkExpireHours 则认为上传已中断，执行清理。
     *
     * @return 清理的目录数量
     */
    public int cleanupExpiredChunks() {
        Path basePath = Paths.get(chunkBaseDir);
        if (!Files.exists(basePath)) {
            return 0;
        }

        AtomicInteger deletedCount = new AtomicInteger(0);
        Instant threshold = Instant.now().minus(Duration.ofHours(chunkExpireHours));

        try (Stream<Path> dirs = Files.list(basePath)) {
            dirs.filter(Files::isDirectory)
                    .forEach(dir -> {
                        try {
                            Instant lastModified = getDirectoryLastModifiedTime(dir);
                            if (lastModified.isBefore(threshold)) {
                                deleteDirectory(dir);
                                deletedCount.incrementAndGet();
                                log.info("清理过期分片目录: {}, 最后修改时间: {}", dir.getFileName(), lastModified);
                            }
                        } catch (IOException e) {
                            log.warn("检查/清理分片目录失败: {}", dir, e);
                        }
                    });
        } catch (IOException e) {
            log.error("遍历分片根目录失败: {}", basePath, e);
        }

        return deletedCount.get();
    }

    /**
     * 清理过期的合并文件目录
     * <p>遍历 mergeBaseDir 下的所有子目录（以 identifier 命名），
     * 检查目录内文件的最后修改时间，如果超过 mergeExpireHours 则执行清理。
     *
     * @return 清理的目录数量
     */
    public int cleanupExpiredMergedFiles() {
        if (mergeExpireHours <= 0) {
            log.debug("合并文件清理已禁用 (merge-expire-hours=0)");
            return 0;
        }

        Path basePath = Paths.get(mergeBaseDir);
        if (!Files.exists(basePath)) {
            return 0;
        }

        AtomicInteger deletedCount = new AtomicInteger(0);
        Instant threshold = Instant.now().minus(Duration.ofHours(mergeExpireHours));

        try (Stream<Path> dirs = Files.list(basePath)) {
            dirs.filter(Files::isDirectory)
                    .forEach(dir -> {
                        try {
                            Instant lastModified = getDirectoryLastModifiedTime(dir);
                            if (lastModified.isBefore(threshold)) {
                                deleteDirectory(dir);
                                deletedCount.incrementAndGet();
                                log.info("清理过期合并文件目录: {}, 最后修改时间: {}", dir.getFileName(), lastModified);
                            }
                        } catch (IOException e) {
                            log.warn("检查/清理合并目录失败: {}", dir, e);
                        }
                    });
        } catch (IOException e) {
            log.error("遍历合并根目录失败: {}", basePath, e);
        }

        return deletedCount.get();
    }

    // ==================== 私有工具方法 ====================

    /**
     * 获取目录的最后修改时间（取目录内所有文件中最新的修改时间）
     * <p>如果目录为空，则使用目录本身的修改时间
     */
    private Instant getDirectoryLastModifiedTime(Path dir) throws IOException {
        try (Stream<Path> files = Files.walk(dir)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                            return attrs.lastModifiedTime().toInstant();
                        } catch (IOException e) {
                            return Instant.MIN;
                        }
                    })
                    .max(Instant::compareTo)
                    .orElse(Files.getLastModifiedTime(dir).toInstant());
        }
    }

    /**
     * 递归删除目录及其所有内容
     */
    private void deleteDirectory(Path dir) throws IOException {
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.warn("删除文件失败: {}", path, e);
                        }
                    });
        }
    }
}
