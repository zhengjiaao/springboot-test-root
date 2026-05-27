package com.zja.controller.parts6;

import com.zja.controller.parts6.request.ChunkUploadStatusRequest;
import com.zja.controller.parts6.request.FileChunkMergeRequest;
import com.zja.controller.parts6.request.FileChunkUploadRequest;
import com.zja.controller.parts6.request.FileQueryRequest;
import com.zja.controller.parts6.vo.ChunkUploadStatusVO;
import com.zja.controller.parts6.vo.FileChunkMergeResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 通用文件分片上传控制器
 * 提供项目全局通用的文件分片上传及合并功能
 *
 * @author: zhengja
 * @since: 2026/04/23 10:00:00
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/fileChunk")
@Api(tags = {"通用文件分片上传"})
public class FileChunkController {

    private final FileChunkService fileChunkService;

    @PostMapping(value = "/upload")
    @ApiOperation("上传文件分片")
    public void uploadChunk(@Valid FileChunkUploadRequest request,
                            @RequestPart(value = "file") MultipartFile file) {
        fileChunkService.uploadChunk(request, file);
    }

    @PostMapping("/upload/status")
    @ApiOperation("查询分片上传状态（支持断点续传，可选的）")
    public ChunkUploadStatusVO getUploadStatus(@Valid @RequestBody ChunkUploadStatusRequest request) {
        return fileChunkService.getUploadStatus(request);
    }

    @PostMapping("/merge")
    @ApiOperation("合并文件分片")
    public FileChunkMergeResultVO mergeChunks(@Valid @RequestBody FileChunkMergeRequest request) {
        return fileChunkService.mergeChunks(request);
    }

    @PostMapping("/merge/fileInfo")
    @ApiOperation("获取已合并文件的信息")
    public FileChunkMergeResultVO getFileInfo(@Valid @RequestBody FileQueryRequest request) {
        return fileChunkService.getFileInfo(request);
    }

    @GetMapping("/download")
    @ApiOperation("下载已合并的文件")
    public void downloadFile(@NotBlank(message = "文件唯一标识不能为空") @RequestParam("fileIdentifier") String fileIdentifier,
                             @NotBlank(message = "文件名不能为空") @RequestParam("fileName") String fileName,
                             HttpServletResponse response) {
        FileQueryRequest request = new FileQueryRequest();
        request.setFileIdentifier(fileIdentifier);
        request.setFileName(fileName);
        fileChunkService.downloadFile(request, response);
    }
}
