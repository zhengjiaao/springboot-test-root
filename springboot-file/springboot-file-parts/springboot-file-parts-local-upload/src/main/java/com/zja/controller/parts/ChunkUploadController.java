package com.zja.controller.parts;

import com.zja.service.parts.ChunkUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 这个控制器处理分块文件上传和合并。
 */
@CrossOrigin
@Api(tags = "Chunk Upload")
@RestController
@RequestMapping("/chunk")
public class ChunkUploadController {

    @Autowired
    private ChunkUploadService chunkUploadService;

    @ApiOperation(value = "初始化上传-生成文件唯一标识-前端使用", notes = "返回生成的文件唯一标识")
    @PostMapping("/identifier")
    public ResponseEntity<String> initUpload(@RequestParam(name = "fileName", required = false) String fileName) {
        String identifier = "";

        if (!StringUtils.isEmpty(fileName)) {
            identifier = UUID.nameUUIDFromBytes(fileName.getBytes()).toString().substring(25) + "_" + System.currentTimeMillis();
        } else {
            identifier = UUID.randomUUID().toString().substring(25) + "_" + System.currentTimeMillis();
        }

        return ResponseEntity.ok(identifier);
    }

    /**
     * 上传文件的一个分片
     *
     * @param file        文件分片
     * @param chunkNumber 当前分片编号 (分片编号)
     * @param totalChunks 总分片数 (分片总数)
     * @param identifier  文件唯一标识,自定义uuid/业务id，不允许重复
     * @param fileName    文件名，示例：test.txt
     */
    @ApiOperation(value = "上传文件的一个分片-前端使用", notes = "上传文件的一个分片以便稍后合并")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("chunkNumber") Integer chunkNumber,
                                    @RequestParam("totalChunks") Integer totalChunks,
                                    @RequestParam("identifier") String identifier,
                                    @RequestParam("fileName") String fileName) throws IOException {
        chunkUploadService.upload(identifier, file, chunkNumber, totalChunks, fileName);
        return ResponseEntity.ok().build();
    }

    /**
     * 合并上传的文件分片
     *
     * @param identifier 文件唯一标识
     * @param fileName   文件名
     */
    @ApiOperation(value = "合并上传的文件分片-前端使用", notes = "将所有上传的文件分片合并成一个文件")
    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestParam("identifier") String identifier,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("totalSize") long totalSize,
                                   @RequestParam("totalChunks") Integer totalChunks) throws IOException {
        chunkUploadService.merge(identifier, fileName, totalSize, totalChunks);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "获取合并的文件-后台使用", notes = "所有分片文件进行合并的文件")
    @GetMapping("/get/merge/file")
    public ResponseEntity<?> getMergeFile(@RequestParam("identifier") String identifier) throws IOException {
        File file = chunkUploadService.getMergeFile(identifier).getAbsoluteFile();
        return ResponseEntity.ok(file);
    }

    @ApiOperation(value = "删除已上传的文件分片-后台使用，后期定时删除", notes = "删除已上传的文件分片")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("identifier") String identifier) {
        chunkUploadService.delete(identifier);
        return ResponseEntity.ok().build();
    }

}
