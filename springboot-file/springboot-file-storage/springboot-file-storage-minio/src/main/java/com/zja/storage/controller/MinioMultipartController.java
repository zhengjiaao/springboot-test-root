package com.zja.storage.controller;

import com.zja.storage.model.request.MergePartRequest;
import com.zja.storage.model.request.UploadPartRequest;
import com.zja.storage.model.response.ChunkUploadRes;
import com.zja.storage.model.response.PartRes;
import com.zja.storage.model.response.UploadPartRes;
import com.zja.storage.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-12-26 15:47
 */
@Validated
@RestController
@RequestMapping("/rest/minio/chunk/")
@Api(tags = {"minio分片文件管理页面"})
public class MinioMultipartController {

    @Autowired
    UploadService service;

    @ApiOperation(value = "申请分片上传", notes = "申请一个大文件分片上传，返回每个分片上传的URL")
    @PostMapping("/apply")
    public ResponseEntity<ChunkUploadRes> apply(@RequestParam("objectName") String objectName, @RequestParam("totalChunks") Integer totalChunks) {
        return ResponseEntity.ok(service.applyForMultipartUpload(objectName, totalChunks));
    }

    @ApiOperation(value = "上传分片", notes = "上传分片")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadPartRes> uploadPart(UploadPartRequest request) {
        return ResponseEntity.ok(service.uploadPart(request));
    }

    @ApiOperation(value = "合并分片", notes = "合并分片")
    @PostMapping("/merge")
    public ResponseEntity<Boolean> mergeFile(MergePartRequest request) {
        service.mergePartUpload(request);
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "获取已上传的分片列表", notes = "获取分片列表")
    @GetMapping("/listParts")
    public ResponseEntity<List<PartRes>> listParts(@RequestParam String uploadId, @RequestParam String objectName) {
        return ResponseEntity.ok(service.listParts(uploadId, objectName));
    }

    @ApiOperation(value = "取消分片上传", notes = "取消分片上传")
    @PostMapping("/cancel")
    public ResponseEntity<Boolean> cancelUpload(@RequestParam("uploadId") String uploadId, @RequestParam("objectName") String objectName) {
        service.cancelMultipartUpload(objectName, uploadId);
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "获取文件地址", notes = "获取文件访问or下载地址")
    @GetMapping("/getFileUrl")
    public ResponseEntity<String> getFileUrl(@RequestParam("objectName") String objectName) {
        return ResponseEntity.ok(service.getFileUrl(objectName));
    }
}
