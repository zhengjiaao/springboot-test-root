package com.zja.controller;

import com.zja.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author: zhengja
 * @Date: 2025-09-08 20:08
 */
@RestController
@RequestMapping("/ftp")
public class FtpBController {

    @Autowired
    private FtpService ftpService;

    @PostMapping("/upload/{server}")
    public String uploadFile(@PathVariable String server, @RequestParam String remotePath, @RequestParam MultipartFile file) {
        boolean success = ftpService.uploadFile(server, remotePath, file);
        return success ? "Upload successful" : "Upload failed";
    }

    @GetMapping("/download/{server}")
    public void downloadFile(@PathVariable String server, @RequestParam String remotePath, @RequestParam String localPath) {
        File localFile = new File(localPath);
        boolean success = ftpService.downloadFile(server, remotePath, localFile);
        if (!success) {
            // 处理下载失败，例如抛出异常或返回错误信息
            throw new RuntimeException("Download failed");
        }
    }

    // 你可以根据需要添加其他端点，如 delete, exists 等
}