package com.zja.controller.parts3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author: zhengja
 * @Date: 2024-09-14 9:56
 */
@CrossOrigin
@Slf4j
@Api(tags = {"LargeFilePartsUploadController"}, description = "大文件分片上传")
@RestController
@RequestMapping(value = "large/file/parts2")
public class LargeFilePartsUpload3Controller {


    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FileService fileService;

    @PostMapping("/initUpload")
    public ResponseEntity<String> initUpload() {
        String fileId = UUID.randomUUID().toString();
        fileService.createUploadSession(fileId);
        return ResponseEntity.ok(fileId);
    }

    @PostMapping("/uploadChunk")
    public ResponseEntity<String> uploadChunk(@RequestParam String fileId, @RequestParam Integer chunkIndex, @RequestParam MultipartFile fileChunk) {
        if (!fileService.isValidChunk(fileId, chunkIndex, fileChunk)) {
            return ResponseEntity.badRequest().body("Invalid chunk data");
        }

        try {
            fileService.saveChunk(fileId, chunkIndex, fileChunk);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving chunk");
        }

        return ResponseEntity.ok("Chunk uploaded successfully");
    }

    @PostMapping("/mergeFile")
    public ResponseEntity<String> mergeFile(@RequestParam String fileId) {
        try {
            fileService.mergeFile(fileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error merging file");
        }

        return ResponseEntity.ok("File merged successfully");
    }

    @GetMapping("/uploadProgress")
    public ResponseEntity<String> getUploadProgress(@RequestParam String fileId) throws IOException {
        int progress = fileService.calculateUploadProgress(fileId);
        return ResponseEntity.ok(progress + "%");
    }
}
