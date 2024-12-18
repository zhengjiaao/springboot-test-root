package com.zja.controller.parts4;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-09-14 9:56
 */
//@CrossOrigin
//@Slf4j
//@Api(tags = {"LargeFilePartsUploadController"}, description = "大文件分片上传")
//@RestController
//@RequestMapping(value = "large/file/parts3")
public class LargeFilePartsUpload4Controller {

//    @Autowired
    private FileService4 fileService4;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // 处理文件上传请求
        // 可以根据需要生成文件上传会话
        String fileId = fileService4.createUploadSession();
        // 进行文件分片处理、保存等操作
        return fileId;
    }

    @PostMapping("/uploadChunk/{fileId}/{chunkIndex}")
    public void uploadChunk(@PathVariable String fileId,
                            @PathVariable int chunkIndex,
                            @RequestParam("file") MultipartFile fileChunk) throws IOException {
        // 处理分片上传请求
        if (fileService4.isValidChunk(fileId, chunkIndex, fileChunk)) {
            fileService4.saveChunk(fileId, chunkIndex, fileChunk);
        }
    }

    @GetMapping("/pauseUpload/{fileId}")
    public void pauseUpload(@PathVariable String fileId) {
        fileService4.pauseUpload(fileId);
    }

    @GetMapping("/resumeUpload/{fileId}")
    public void resumeUpload(@PathVariable String fileId) {
        fileService4.resumeUpload(fileId);
    }

    @GetMapping("/cancelUpload/{fileId}")
    public void cancelUpload(@PathVariable String fileId) {
        fileService4.cancelUpload(fileId);
    }

    @GetMapping("/chunkProgress/{fileId}")
    public int getChunkProgress(@PathVariable String fileId) {
        return fileService4.getChunkUploadProgress(fileId);
    }

    @GetMapping("/totalProgress/{fileId}")
    public int getTotalProgress(@PathVariable String fileId) {
        return fileService4.getTotalUploadProgress(fileId);
    }
}
