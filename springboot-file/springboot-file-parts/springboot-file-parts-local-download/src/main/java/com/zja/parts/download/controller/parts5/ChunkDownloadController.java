package com.zja.parts.download.controller.parts5;

import com.zja.parts.download.controller.parts5.dto.FileInfoResult;
import com.zja.parts.download.controller.parts5.dto.FileItemResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 大文件分片下载控制器 - 支持断点续传、分片下载、MD5校验
 *
 * <p>API 列表：
 * <ul>
 *   <li>GET  /parts5-download/file/list                  - 获取可下载文件列表</li>
 *   <li>GET  /parts5-download/file/info                  - 获取文件信息(大小、分片数)</li>
 *   <li>GET  /parts5-download/file/download              - 分片下载(支持Range断点续传)</li>
 *   <li>GET  /parts5-download/file/chunk                 - 指定分片范围下载</li>
 *   <li>GET  /parts5-download/file/md5                   - 计算文件MD5校验值</li>
 *   <li>GET  /parts5-download/file/full              - 全量下载整个文件</li>
 * </ul>
 *
 * <p>前端测试页面: <a href="http://localhost:8080/parts5-download/ChunkDownloadParts5.html">ChunkDownloadParts5.html</a>
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Slf4j
@CrossOrigin
@Api(tags = {"Parts5-大文件分片下载"})
@RestController
@RequestMapping("/parts5-download")
public class ChunkDownloadController {

    @Autowired
    private ChunkDownloadService downloadService;

    /**
     * 获取可下载文件列表
     */
    @ApiOperation("获取可下载文件列表")
    @GetMapping("/file/list")
    public ResponseEntity<List<FileItemResult>> listFiles() {
        List<FileItemResult> files = downloadService.listFiles();
        return ResponseEntity.ok(files);
    }

    /**
     * 获取文件信息（大小、建议分片大小、总分片数）
     */
    @ApiOperation("获取文件信息")
    @GetMapping("/file/info")
    public ResponseEntity<FileInfoResult> getFileInfo(
            @ApiParam("文件名") @RequestParam String fileName) {
        FileInfoResult result = downloadService.getFileInfo(fileName);
        if (!result.isExists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 分片下载 - 支持 HTTP Range 断点续传标准协议
     * <p>
     * 前端通过设置 Range 请求头指定下载范围：
     * - Range: bytes=0-1048575      下载前 1MB
     * - Range: bytes=1048576-2097151 下载第 2 个 1MB
     * - Range: bytes=0-0              探测文件大小
     * <p>
     * 响应状态码：
     * - 200：返回整个文件（无Range头）
     * - 206：返回部分内容（有Range头且合法）
     * - 416：请求范围不满足
     */
    @ApiOperation("分片下载(支持Range断点续传)")
    @GetMapping("/file/download")
    public ResponseEntity<?> downloadWithRange(
            @ApiParam("文件名") @RequestParam String fileName,
            HttpServletRequest request) throws IOException {

        if (!downloadService.fileExists(fileName)) {
            return ResponseEntity.notFound().build();
        }

        long fileSize = downloadService.getFileSize(fileName);
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);

        // 无 Range 头 → 流式返回完整文件（避免大文件OOM）
        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            String encodedName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

            // 使用 FileSystemResource 流式传输，不会一次性加载到内存
            Resource resource = downloadService.getFileResource(fileName);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileSize)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(resource);
        }

        // 探测模式: Range: bytes=0-0 → 返回206及文件大小信息（符合HTTP规范）
        if ("bytes=0-0".equals(rangeHeader)) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes 0-0/" + fileSize)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(1)
                    .body(downloadService.readChunk(fileName, 0, 0));
        }

        // 解析 Range: bytes=start-end
        try {
            String byteRange = rangeHeader.substring(6); // 去掉 "bytes="
            String[] ranges = byteRange.split("-");
            long start = Long.parseLong(ranges[0].trim());
            long end = ranges.length > 1 && !ranges[1].trim().isEmpty()
                    ? Long.parseLong(ranges[1].trim())
                    : fileSize - 1;

            // 范围校验
            if (start < 0 || start >= fileSize || end >= fileSize || start > end) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }

            byte[] chunkData = downloadService.readChunk(fileName, start, end);
            String encodedName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(chunkData.length);
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(chunkData);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * 指定分片范围下载 - 更直观的显式分片API
     * <p>
     * 前端直接指定 chunkNumber，服务端根据分片大小自动计算字节范围
     *
     * @param fileName    文件名
     * @param chunkNumber 分片编号（从0开始）
     * @param chunkSize   分片大小（字节），不传则使用服务端默认值
     */
    @ApiOperation("指定分片范围下载")
    @GetMapping("/file/chunk")
    public ResponseEntity<byte[]> downloadChunk(
            @ApiParam("文件名") @RequestParam String fileName,
            @ApiParam("分片编号(从0开始)") @RequestParam int chunkNumber,
            @ApiParam("分片大小(字节)") @RequestParam(required = false) Long chunkSize) throws IOException {

        if (!downloadService.fileExists(fileName)) {
            return ResponseEntity.notFound().build();
        }

        long fileSize = downloadService.getFileSize(fileName);
        FileInfoResult info = downloadService.getFileInfo(fileName);
        long effectiveChunkSize = chunkSize != null ? chunkSize : info.getChunkSize();

        long start = (long) chunkNumber * effectiveChunkSize;
        if (start >= fileSize) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                    .build();
        }

        long end = Math.min(start + effectiveChunkSize - 1, fileSize - 1);
        byte[] chunkData = downloadService.readChunk(fileName, start, end);
        String encodedName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(chunkData.length);
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .body(chunkData);
    }

    /**
     * 计算文件MD5校验值 - 前端下载完成后可调用此接口校验完整性
     * <p>注意：大文件MD5计算可能需要数秒至数十秒
     */
    @ApiOperation("计算文件MD5校验值")
    @GetMapping("/file/md5")
    public ResponseEntity<Map<String, String>> getFileMD5(
            @ApiParam("文件名") @RequestParam String fileName) {
        if (!downloadService.fileExists(fileName)) {
            return ResponseEntity.notFound().build();
        }

        String md5 = downloadService.calculateMD5(fileName);
        return ResponseEntity.ok(Collections.singletonMap("md5", md5));
    }

    /**
     * 全量下载整个文件 - 适用于小文件或不需要分片的场景
     */
    @ApiOperation("全量下载文件")
    @GetMapping("/file/full")
    public ResponseEntity<Resource> downloadFullFile(
            @ApiParam("文件名") @RequestParam String fileName) throws Exception {
        if (!downloadService.fileExists(fileName)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = downloadService.getFileResource(fileName);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        String encodedName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(resource);
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

    /**
     * IO异常处理（返回 500）
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException e) {
        log.error("IO异常: {}", e.getMessage());
        Map<String, String> body = Collections.singletonMap("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
