package com.zja.web;

import com.zja.service.ChunkUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This controller handles chunked file uploads and merging.
 */
@CrossOrigin
@Api(tags = "Chunk Upload")
@RestController
@RequestMapping("/chunk")
public class ChunkUploadController {

    @Autowired
    private ChunkUploadService chunkUploadService;

    /**
     * 上传文件的一个分片
     * @param file 文件分片
     * @param chunkNumber 当前分片编号
     * @param totalChunks 总分片数
     * @param identifier 文件唯一标识
     * @param filename 文件名
     * @throws IOException
     */
    @ApiOperation(value = "Upload a chunk of a file", notes = "Uploads a single chunk of a file for later merging")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                     @RequestParam("chunkNumber") Integer chunkNumber,
                                     @RequestParam("totalChunks") Integer totalChunks,
                                     @RequestParam("identifier") String identifier,
                                     @RequestParam("filename") String filename) throws IOException {
        chunkUploadService.upload(file, chunkNumber, totalChunks, identifier, filename);
        return ResponseEntity.ok().build();
    }

    /**
     * 合并上传的文件分片
     * @param identifier 文件唯一标识
     * @param filename 文件名
     * @throws IOException
     */
    @ApiOperation(value = "Merge uploaded file chunks", notes = "Merges all uploaded file chunks into a single file")
    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestParam("identifier") String identifier,
                                   @RequestParam("filename") String filename,
                                   @RequestParam("totalChunks") Integer totalChunks) throws IOException {
        chunkUploadService.merge(identifier, filename, totalChunks);
        return ResponseEntity.ok().build();
    }

}
