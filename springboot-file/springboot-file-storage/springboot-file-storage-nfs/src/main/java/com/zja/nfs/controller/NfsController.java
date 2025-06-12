package com.zja.nfs.controller;

import com.zja.nfs.nfs.FileInfo;
import com.zja.nfs.nfs.NfsClient;
import com.zja.nfs.nfs.NfsException;
import com.zja.nfs.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-06-12 14:59
 */
@RestController
@RequestMapping("/api/nfs")
public class NfsController {

    @Autowired
    private NfsClient nfsClient;

    // ========== 桶相关接口 ==========

    /**
     * 创建桶
     */
    @PostMapping("/bucket/create")
    public ResponseEntity<?> createBucket(@RequestParam String bucket) {
        try {
            String result = nfsClient.createBucket(bucket);
            return ResponseEntity.ok(ResponseUtil.successData("path", result));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 删除桶（必须为空）
     */
    @DeleteMapping("/bucket/delete")
    public ResponseEntity<?> deleteBucket(@RequestParam String bucket) throws NfsException {
        boolean deleted = nfsClient.deleteBucket(bucket);
        return ResponseEntity.ok(ResponseUtil.successData("deleted", deleted));
    }

    /**
     * 判断桶是否存在
     */
    @GetMapping("/bucket/exists")
    public ResponseEntity<?> existsBucket(@RequestParam String bucket) {
        try {
            return ResponseEntity.ok(ResponseUtil.successData("exists", nfsClient.existsBucket(bucket)));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 判断桶是否为空
     */
    @GetMapping("/bucket/empty")
    public ResponseEntity<?> isBucketEmpty(@RequestParam String bucket) {
        try {
            return ResponseEntity.ok(ResponseUtil.successData("empty", nfsClient.isBucketEmpty(bucket)));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 获取所有桶列表
     */
    @GetMapping("/bucket/list")
    public ResponseEntity<?> listBuckets() {
        try {
            List<String> buckets = nfsClient.listBuckets();
            return ResponseEntity.ok(ResponseUtil.successData("buckets", buckets));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 列出桶下的子桶（仅目录）
     */
    @GetMapping("/bucket/list/sub")
    public ResponseEntity<?> listSubBuckets(@RequestParam String bucket) {
        try {
            List<String> buckets = nfsClient.listBucketsInBucket(bucket);
            return ResponseEntity.ok(ResponseUtil.successData("buckets", buckets));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 重命名桶
     */
    @PostMapping("/bucket/rename")
    public ResponseEntity<?> renameBucket(@RequestParam String oldName, @RequestParam String newName) {
        try {
            boolean renamed = nfsClient.renameBucket(oldName, newName);
            return ResponseEntity.ok(ResponseUtil.successData("renamed", renamed));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }


    // ========== 文件相关接口 ==========

    /**
     * 上传文件到指定桶
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam String bucket,
            @RequestParam String fileName,
            @RequestPart MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            nfsClient.upload(bucket, fileName, is);
            return ResponseEntity.ok(ResponseUtil.success("上传成功"));
        } catch (IOException | NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error("上传失败: " + e.getMessage()));
        }
    }

    /**
     * 流式下载文件（用于大文件或在线播放）
     */
    @GetMapping("/download/stream")
    public ResponseEntity<InputStreamResource> downloadStream(@RequestParam String bucket, @RequestParam String fileName) throws NfsException {
        InputStream inputStream = nfsClient.downloadStream(bucket, fileName);

        // 使用 UTF-8 编码文件名，避免中文乱码
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);

        // 构建 Content-Disposition 头，使用 filename* 支持 UTF-8
        String contentDispositionValue = "inline; filename=\"" + fileName + "\"; filename*=UTF-8''" + encodedFileName;

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionValue)
                .body(new InputStreamResource(inputStream));
    }

    /**
     * 下载文件到本地路径（服务端本地下载）
     */
    @GetMapping("/download/local")
    public ResponseEntity<?> downloadToLocal(@RequestParam String bucket, @RequestParam String fileName, @RequestParam String localPath) {
        try {
            nfsClient.downloadToFile(bucket, fileName, localPath);
            return ResponseEntity.ok(ResponseUtil.success("下载成功: " + localPath));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error("下载失败: " + e.getMessage()));
        }
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/file/exists")
    public ResponseEntity<?> fileExists(@RequestParam String bucket, @RequestParam String fileName) {
        try {
            boolean exists = nfsClient.existsFile(bucket, fileName);
            return ResponseEntity.ok(ResponseUtil.successData("exists", exists));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/file/delete")
    public ResponseEntity<?> deleteFile(@RequestParam String bucket, @RequestParam String fileName) throws NfsException {
        boolean deleted = nfsClient.delete(bucket, fileName);
        return ResponseEntity.ok(ResponseUtil.successData("deleted", deleted));
    }

    /**
     * 列出桶中所有文件
     */
    @GetMapping("/files/list")
    public ResponseEntity<?> listFiles(@RequestParam String bucket) {
        try {
            List<String> files = nfsClient.listFilesInBucket(bucket);
            return ResponseEntity.ok(ResponseUtil.successData("files", files));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error("列出文件失败: " + e.getMessage()));
        }
    }

    @GetMapping("/files/list/v2")
    public ResponseEntity<?> listFilesV4(@RequestParam String bucket) {
        try {
            List<FileInfo> files = nfsClient.listFilesWithInfo(bucket);
            return ResponseEntity.ok(ResponseUtil.successData("files", files));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error("列出文件失败: " + e.getMessage()));
        }
    }

    /**
     * 列出桶下的文件（仅文件）
     */
    @GetMapping("/file/list")
    public ResponseEntity<?> listFilesOnly(@RequestParam String bucket) {
        try {
            List<String> files = nfsClient.listFilesInBucketOnly(bucket);
            return ResponseEntity.ok(ResponseUtil.successData("files", files));
        } catch (NfsException e) {
            return ResponseEntity.status(500).body(ResponseUtil.error(e.getMessage()));
        }
    }

}
