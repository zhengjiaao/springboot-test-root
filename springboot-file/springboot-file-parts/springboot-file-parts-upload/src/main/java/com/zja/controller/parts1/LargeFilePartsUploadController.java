package com.zja.controller.parts1;

import com.zja.service.parts1.LargeFilePartsUploadImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2024/01/22 13:20
 */
@Api(value = "大文件分片上传", tags = {"LargeFilePartsUploadController"})
@RestController
@RequestMapping(value = "large/file/parts/upload")
public class LargeFilePartsUploadController {

    @Autowired
    LargeFilePartsUploadImpl myFileUploader;

    public static final String uploadDir = "D:\\temp\\parts";

    @ApiOperation(value = "文件分片上传", notes = "上传完成，自动合并")
    @PostMapping(value = "/v1")
    public Object uploadFileParts(
            @ApiParam(value = "选择文件分片") @RequestPart("filePart") MultipartFile filePart,
            @ApiParam(value = "文件名称") String fileName,
            @ApiParam(value = "文件大小") long fileSize,
            @ApiParam(value = "文件块大小") long chunkSize,
            @ApiParam(value = "总块数") long totalChunks,
            @ApiParam(value = "当前块所属第几块") long currentChunk,
            @ApiParam(value = "上传文件唯一标识") String uploadId) throws IOException {

        InputStream inputStream = filePart.getInputStream();
        // String fileName = "test.txt";
        // long fileSize = 1000;
        // long chunkSize = 100;
        // long totalChunks = fileSize / chunkSize;
        // long currentChunk = 0;
        // String uploadDir = "D:\\temp\\txt\\parts";
        // String uploadId = "12345";

        // 调用方法
        myFileUploader.upload(inputStream, fileName, fileSize, chunkSize,
                totalChunks, currentChunk, uploadDir, uploadId);
        return true;
    }
}
