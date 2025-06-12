package com.zja.nfs.nfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: zhengja
 * @Date: 2025-06-12 13:40
 */
@Component
public class NfsClientImpl implements NfsClient {

    private static final Logger logger = LoggerFactory.getLogger(NfsClientImpl.class);

    // nfs 挂载路径（指挂载的本机路径）
    @Value("${nfs.mount-path}")
    private String basePath;

    private Path resolvePath(String relativePath) {
        return Paths.get(basePath, relativePath);
    }

    // 校验桶名是否合法（防止路径穿越）
    private void validateBucket(String bucket) throws NfsException {
        if (bucket == null || bucket.trim().isEmpty()) {
            throw new NfsException("桶名称不能为空");
        }
        if (bucket.contains("..")) {
            throw new NfsException("非法的桶名称，包含路径穿越字符: " + bucket);
        }
    }

    // 获取桶目录 Path
    private Path getBucketPath(String bucket) throws NfsException {
        validateBucket(bucket);
        return resolvePath(bucket);
    }

    // 获取文件 Path
    private Path getFilePath(String bucket, String fileName) throws NfsException {
        return getBucketPath(bucket).resolve(fileName);
    }

    @Override
    public String createBucket(String bucket) throws NfsException {
        try {
            Path path = getBucketPath(bucket);
            if (Files.exists(path)) {
                logger.warn("桶 {} 已存在", bucket);
                return path.toString();
            }
            Path created = Files.createDirectories(path);
            logger.info("创建桶成功: {}", bucket);
            return created.toString();
        } catch (IOException e) {
            logger.error("创建桶失败: {}", bucket, e);
            throw new NfsException("创建桶失败: " + bucket, e);
        }
    }

    @Override
    public boolean deleteBucket(String bucket) throws NfsException {
        try {
            Path dir = getBucketPath(bucket);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                logger.warn("桶不存在或不是目录: {}", bucket);
                return false;
            }
            if (!isBucketEmpty(bucket)) {
                logger.warn("删除非空桶失败: {}", bucket);
                return false;
            }
            Files.delete(dir);
            logger.info("删除桶成功: {}", bucket);
            return true;
        } catch (IOException e) {
            logger.error("删除桶失败: {}", bucket, e);
            throw new NfsException("删除桶失败: " + bucket, e);
        }
    }

    @Override
    public boolean existsBucket(String bucket) throws NfsException {
        try {
            Path dir = getBucketPath(bucket);
            boolean exists = Files.exists(dir) && Files.isDirectory(dir);
            logger.debug("检查桶是否存在 {}: {}", bucket, exists ? "存在" : "不存在");
            return exists;
        } catch (NfsException e) {
            logger.warn("检查桶是否存在时发生错误: {}", bucket, e);
            return false;
        }
    }

    @Override
    public boolean isBucketEmpty(String bucket) throws NfsException {
        Path dir = getBucketPath(bucket);
        if (!existsBucket(bucket)) {
            throw new NfsException("桶不存在: " + bucket);
        }
        try (Stream<Path> stream = Files.list(dir)) {
            boolean isEmpty = !stream.findFirst().isPresent();
            logger.debug("桶 {} 是否为空: {}", bucket, isEmpty ? "是" : "否");
            return isEmpty;
        } catch (IOException e) {
            logger.error("判断桶是否为空失败: {}", bucket, e);
            throw new NfsException("判断桶是否为空失败: " + bucket, e);
        }
    }

    @Override
    public List<String> listBuckets() throws NfsException {
        try {
            Path baseDir = Paths.get(basePath);
            if (!Files.exists(baseDir)) {
                logger.warn("挂载路径不存在: {}", basePath);
                return new ArrayList<>();
            }
            try (Stream<Path> stream = Files.list(baseDir)) {
                List<String> buckets = stream
                        .filter(Files::isDirectory)
                        .map(p -> p.getFileName().toString())
                        .sorted()
                        .collect(Collectors.toList());
                logger.info("列出所有桶共 {} 个", buckets.size());
                return buckets;
            }
        } catch (IOException e) {
            logger.error("列出所有桶失败", e);
            throw new NfsException("列出所有桶失败", e);
        }
    }

    @Override
    public boolean renameBucket(String oldName, String newName) throws NfsException {
        Path oldPath = getBucketPath(oldName);
        Path newPath = getBucketPath(newName);

        if (!existsBucket(oldName)) {
            throw new NfsException("原桶不存在: " + oldName);
        }

        if (existsBucket(newName)) {
            throw new NfsException("目标桶已存在: " + newName);
        }

        try {
            Files.move(oldPath, newPath);
            logger.info("桶重命名为: {} -> {}", oldName, newName);
            return true;
        } catch (IOException e) {
            logger.error("重命名桶失败: {} -> {}", oldName, newName, e);
            throw new NfsException("重命名桶失败: " + oldName + " -> " + newName, e);
        }
    }

    @Override
    public void upload(String bucket, String fileName, InputStream inputStream) throws NfsException {
        try {
            Path targetDir = getBucketPath(bucket);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path targetFile = targetDir.resolve(fileName);
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("文件 {} 存储到桶 {} 成功", fileName, bucket);
        } catch (IOException e) {
            logger.error("存储文件到桶 {} 失败", bucket, e);
            throw new NfsException("存储文件失败: " + fileName, e);
        }
    }

    @Override
    public InputStream downloadStream(String bucket, String fileName) throws NfsException {
        Path filePath = getFilePath(bucket, fileName);
        if (!Files.exists(filePath)) {
            throw new NfsException("文件不存在: " + filePath);
        }
        try {
            logger.info("获取文件流成功: {}/{}", bucket, fileName);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            logger.error("获取文件流失败: {}/{}", bucket, fileName, e);
            throw new NfsException("获取文件流失败: " + fileName, e);
        }
    }

    @Override
    public void downloadToFile(String bucket, String fileName, String localPath) throws NfsException {
        Path filePath = getFilePath(bucket, fileName);
        if (!Files.exists(filePath)) {
            throw new NfsException("文件不存在: " + filePath);
        }

        Path localFile = Paths.get(localPath);
        try {
            // 确保目标目录存在
            Files.createDirectories(localFile.getParent());

            // 复制文件到本地
            Files.copy(filePath, localFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("文件 {}/{} 下载到本地成功: {}", bucket, fileName, localPath);
        } catch (IOException e) {
            logger.error("下载文件到本地失败: {}/{}", bucket, fileName, e);
            throw new NfsException("下载文件到本地失败: " + fileName, e);
        }
    }

    @Override
    public boolean existsFile(String bucket, String fileName) throws NfsException {
        Path filePath = getFilePath(bucket, fileName);
        boolean exists = Files.exists(filePath) && Files.isRegularFile(filePath);

        logger.debug("检查文件 {}/{} 是否存在: {}", bucket, fileName, exists ? "存在" : "不存在");
        return exists;
    }

    @Override
    public boolean delete(String bucket, String fileName) throws NfsException {
        try {
            Path filePath = getFilePath(bucket, fileName);
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                logger.info("文件删除成功: {}/{}", bucket, fileName);
            } else {
                logger.warn("文件不存在或删除失败: {}/{}", bucket, fileName);
            }
            return deleted;
        } catch (IOException e) {
            logger.error("删除文件失败: {}/{}", bucket, fileName, e);
            throw new NfsException("删除文件失败: " + fileName, e);
        }
    }

    @Override
    public List<String> listFilesInBucket(String bucket) throws NfsException {
        Path directory = getBucketPath(bucket);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new NfsException("无效的桶名称: " + bucket);
        }
        try (Stream<Path> stream = Files.list(directory)) {
            List<String> files = stream.map(Path::getFileName)
                    .map(Path::toString)
                    .sorted()
                    .collect(Collectors.toList());
            logger.info("列出桶 {} 中的文件共 {} 个", bucket, files.size());
            return files;
        } catch (IOException e) {
            logger.error("列出桶中的文件失败: {}", bucket, e);
            throw new NfsException("列出桶中的文件失败: " + bucket, e);
        }
    }

    @Override
    public List<String> listBucketsInBucket(String bucket) throws NfsException {
        Path directory = getBucketPath(bucket);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new NfsException("无效的桶名称: " + bucket);
        }

        try (Stream<Path> stream = Files.list(directory)) {
            List<String> subDirectories = stream
                    .filter(path -> Files.isDirectory(path))
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());

            logger.info("列出桶 {} 下的子桶共 {} 个", bucket, subDirectories.size());
            return subDirectories;
        } catch (IOException e) {
            logger.error("列出桶下的子桶失败: {}", bucket, e);
            throw new NfsException("列出桶下的子桶失败: " + bucket, e);
        }
    }

    @Override
    public List<String> listFilesInBucketOnly(String bucket) throws NfsException {
        Path directory = getBucketPath(bucket);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new NfsException("无效的桶名称: " + bucket);
        }

        try (Stream<Path> stream = Files.list(directory)) {
            List<String> filesOnly = stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());

            logger.info("列出桶 {} 下的文件共 {} 个", bucket, filesOnly.size());
            return filesOnly;
        } catch (IOException e) {
            logger.error("列出桶下的文件失败: {}", bucket, e);
            throw new NfsException("列出桶下的文件失败: " + bucket, e);
        }
    }

    @Override
    public List<FileInfo> listFilesWithInfo(String bucket) throws NfsException {
        Path directory = getBucketPath(bucket);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new NfsException("无效的桶名称: " + bucket);
        }

        try (Stream<Path> stream = Files.list(directory)) {
            List<FileInfo> files = stream.map(path -> {
                        try {
                            boolean isDir = Files.isDirectory(path);
                            return new FileInfo(
                                    path.getFileName().toString(),
                                    isDir,
                                    isDir ? 0 : Files.size(path)
                            );
                        } catch (IOException e) {
                            logger.warn("读取文件信息失败: {}", path.getFileName());
                            return null;
                        }
                    })
                    .filter(file -> file != null)
                    .sorted((a, b) -> {
                        // 目录排前面，再按名称排序
                        if (a.isDirectory() && !b.isDirectory()) return -1;
                        if (!a.isDirectory() && b.isDirectory()) return 1;
                        return a.getName().compareTo(b.getName());
                    })
                    .collect(Collectors.toList());

            logger.info("列出桶 {} 中的文件共 {} 项", bucket, files.size());
            return files;
        } catch (IOException e) {
            logger.error("列出桶中的文件失败: {}", bucket, e);
            throw new NfsException("列出桶中的文件失败: " + bucket, e);
        }
    }
}
