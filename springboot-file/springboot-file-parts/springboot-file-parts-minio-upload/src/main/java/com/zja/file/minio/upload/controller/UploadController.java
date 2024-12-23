package com.zja.file.minio.upload.controller;

import com.zja.file.minio.upload.model.request.MergePartRequest;
import com.zja.file.minio.upload.model.request.UploadPartRequest;
import com.zja.file.minio.upload.model.response.ChunkUploadRes;
import com.zja.file.minio.upload.model.response.PartRes;
import com.zja.file.minio.upload.model.response.UploadPartRes;
import com.zja.file.minio.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2024/12/18 10:32
 */
@Validated
@RestController
@RequestMapping("/rest/minio/parts/upload")
@Api(tags = {"文件管理页面"})
public class UploadController {

    @Autowired
    UploadService service;

    @ApiOperation(value = "申请一个大文件分片上传", notes = "申请一个大文件分片上传")
    @PostMapping("/apply/FragmentUpload")
    public ResponseEntity<ChunkUploadRes> apply(@RequestParam("objectName") String objectName, @RequestParam("chunkCount") Integer chunkCount) {
        return ResponseEntity.ok(service.applyForUploadingLargeFileFragments(objectName, chunkCount));
    }

    @ApiOperation(value = "上传分片", notes = "上传分片")
    @PostMapping(value = "/uploadPart", consumes = "multipart/form-data")
    public ResponseEntity<UploadPartRes> uploadPart(UploadPartRequest request) {
        return ResponseEntity.ok(service.uploadPart(request));
    }

    @ApiOperation(value = "合并分片", notes = "合并分片")
    @PostMapping("/mergeFile")
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
    @PostMapping("/cancelUpload")
    public ResponseEntity<Boolean> cancelUpload(@RequestParam("objectName") String objectName, @RequestParam("uploadId") String uploadId) {
        boolean cancelled = service.cancelMultipartUpload(objectName, uploadId);
        return ResponseEntity.ok(cancelled);
    }

    @ApiOperation(value = "获取文件地址", notes = "获取文件访问or下载地址")
    @GetMapping("/getFileUrl")
    public ResponseEntity<String> getFileUrl(@RequestParam("objectName") String objectName) {
        return ResponseEntity.ok(service.getFileUrl(objectName));
    }

}