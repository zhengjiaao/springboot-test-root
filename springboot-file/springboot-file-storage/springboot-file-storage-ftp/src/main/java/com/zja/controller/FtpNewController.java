package com.zja.controller;

import com.zja.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author: zhengja
 * @Date: 2025-09-08 20:08
 */
@RestController
@RequestMapping("/ftp")
public class FtpNewController {

    @Autowired
    private FtpService ftpService;

    @PostMapping("/upload/{server}")
    public ResponseEntity<String> uploadFile(@PathVariable String server,
                                           @RequestParam String remotePath,
                                           @RequestParam MultipartFile file) {
        try {
            boolean success = ftpService.uploadFile(server, remotePath, file);
            if (success) {
                return ResponseEntity.ok("Upload successful");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload error: " + e.getMessage());
        }
    }

    @GetMapping("/download/{server}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String server,
                                             @RequestParam String remotePath,
                                             @RequestParam(required = false) String localPath) {
        try {
            // 如果提供了本地路径，则下载到指定位置
            if (localPath != null && !localPath.isEmpty()) {
                File localFile = new File(localPath);
                // 确保父目录存在
                File parentDir = localFile.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                boolean success = ftpService.downloadFile(server, remotePath, localFile);
                if (success) {
                    return ResponseEntity.status(HttpStatus.OK).body(("File downloaded to local path: " + localPath).getBytes());
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Download failed".getBytes());
                }
            } else {
                // 否则以字节数组形式返回文件内容
                java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
                boolean success = ftpService.downloadFile(server, remotePath, outputStream);

                if (success) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("attachment", new File(remotePath).getName());
                    return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found".getBytes());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Download error: " + e.getMessage()).getBytes());
        }
    }

    @DeleteMapping("/delete/{server}")
    public ResponseEntity<String> deleteFile(@PathVariable String server,
                                           @RequestParam String remotePath) {
        try {
            boolean success = ftpService.deleteFile(server, remotePath);
            if (success) {
                return ResponseEntity.ok("File deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File deletion failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete error: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{server}")
    public ResponseEntity<String> fileExists(@PathVariable String server,
                                           @RequestParam String remotePath) {
        try {
            boolean exists = ftpService.fileExists(server, remotePath);
            return ResponseEntity.ok(String.valueOf(exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Check existence error: " + e.getMessage());
        }
    }

    @GetMapping("/info/{server}")
    public ResponseEntity<String> getServerInfo(@PathVariable String server) {
        try {
            // 这里可以添加获取服务器信息的逻辑
            return ResponseEntity.ok("Server info for: " + server);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server info error: " + e.getMessage());
        }
    }

    @PostMapping("/test-connection/{server}")
    public ResponseEntity<String> testConnection(@PathVariable String server) {
        try {
            boolean exists = ftpService.fileExists(server, "/");
            if (exists || !ftpService.fileExists(server, "/nonexistent")) {
                return ResponseEntity.ok("Connection successful");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Connection failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Connection test error: " + e.getMessage());
        }
    }
}
