package com.zja.controller.parts5;

import com.zja.controller.parts5.dto.ChunkCheckResult;
import com.zja.controller.parts5.dto.MergeResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * 大文件分片上传控制器（Parts5）- 支持断点续传、秒传、合并下载
 *
 * <p>API 列表：
 * <ul>
 *   <li>GET  /parts5/chunk/check  - 检查文件/分片上传状态（秒传 + 断点续传）</li>
 *   <li>POST /parts5/chunk/upload - 上传单个分片</li>
 *   <li>POST /parts5/chunk/merge  - 合并所有分片</li>
 *   <li>GET  /parts5/file/download/{identifier}/{fileName} - 下载合并后文件</li>
 *   <li>DELETE /parts5/chunk/cancel/{identifier} - 取消上传并清理分片</li>
 * </ul>
 *
 * <p>前端测试页面: <a href="http://localhost:8080/parts5/ChunkUploadParts5.html">ChunkUploadParts5.html</a>
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Slf4j
@CrossOrigin
@Api(tags = {"Parts5-大文件分片上传"})
@RestController
@RequestMapping("/parts5")
public class ChunkUploadController5 {

    @Autowired
    private ChunkUploadService5 uploadService;

    /**
     * 检查文件上传状态 - 用于秒传和断点续传
     * <p>前端在上传前调用此接口：
     * - 如果返回 uploaded=true，说明文件已存在，实现秒传
     * - 如果返回 uploadedChunks 列表，前端跳过已上传的分片，实现断点续传
     */
    @ApiOperation("检查文件/分片上传状态")
    @GetMapping("/chunk/check")
    public ResponseEntity<ChunkCheckResult> checkChunks(
            @ApiParam("文件唯一标识(MD5)") @RequestParam String identifier,
            @ApiParam("文件名") @RequestParam String fileName,
            @ApiParam("总分片数") @RequestParam int totalChunks) {
        ChunkCheckResult result = uploadService.checkChunks(identifier, fileName, totalChunks);
        return ResponseEntity.ok(result);
    }

    /**
     * 上传单个分片
     */
    @ApiOperation("上传单个分片")
    @PostMapping("/chunk/upload")
    public ResponseEntity<String> uploadChunk(
            @ApiParam("分片文件") @RequestParam("file") MultipartFile file,
            @ApiParam("当前分片编号(从1开始)") @RequestParam int chunkNumber,
            @ApiParam("总分片数") @RequestParam int totalChunks,
            @ApiParam("文件唯一标识(MD5)") @RequestParam String identifier,
            @ApiParam("文件名") @RequestParam String fileName) throws IOException {
        uploadService.uploadChunk(file, chunkNumber, totalChunks, identifier, fileName);
        return ResponseEntity.ok("分片 " + chunkNumber + " 上传成功");
    }

    /**
     * 合并所有分片为完整文件
     */
    @ApiOperation("合并所有分片")
    @PostMapping("/chunk/merge")
    public ResponseEntity<MergeResult> mergeChunks(
            @ApiParam("文件唯一标识(MD5)") @RequestParam String identifier,
            @ApiParam("文件名") @RequestParam String fileName,
            @ApiParam("总分片数") @RequestParam int totalChunks,
            @ApiParam("文件总大小(字节)") @RequestParam long totalSize) {
        MergeResult result = uploadService.mergeChunks(identifier, fileName, totalChunks, totalSize);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 下载合并后的文件
     */
    @ApiOperation("下载合并后的文件")
    @GetMapping("/file/download/{identifier}/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String identifier,
            @PathVariable String fileName) throws Exception {
        Path filePath = uploadService.getMergedFileForDownload(identifier, fileName);
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath.toFile());
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    /**
     * 取消上传，清理已上传的分片
     */
    @ApiOperation("取消上传并清理分片")
    @DeleteMapping("/chunk/cancel/{identifier}")
    public ResponseEntity<String> cancelUpload(@PathVariable String identifier) {
        uploadService.cancelUpload(identifier);
        return ResponseEntity.ok("上传已取消，分片已清理");
    }

    // ==================== 异常处理 ====================

    /**
     * 参数校验异常处理（返回 400）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        Map<String, String> body = Collections.singletonMap("error", e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}
