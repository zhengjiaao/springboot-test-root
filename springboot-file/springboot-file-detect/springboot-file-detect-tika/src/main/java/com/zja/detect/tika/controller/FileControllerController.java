package com.zja.detect.tika.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.tika.Tika;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 文件管理 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2025/05/26 15:26
 */
@Validated
@RestController
@RequestMapping("/rest/file")
@Api(tags = {"文件管理管理页面"})
public class FileControllerController {

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/plain"
    );

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/tiff"
    );

    // 上传文件

    private Tika tika = new Tika();

    // 检测文件类型
    public String detectFileType(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            return tika.detect(is);
        }
    }

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());

        System.out.println("contentType：" + file.getContentType());

        try {
            String fileType = detectFileType(file);


            System.out.println("fileType：" + fileType);

            if (fileType.contains("image")) {
                //图片
                System.out.println(file.getContentType() + " [" + fileType + "]是 image");
            } else if (fileType.contains("video")) {
                //视频
                System.out.println(file.getContentType() + " [" + fileType + "]是 video");
            } else if (fileType.contains("audio")) {
                //音频
                System.out.println(file.getContentType() + " [" + fileType + "]是 audio");
            } else if (fileType.contains("text")) {
                //文本
                System.out.println(file.getContentType() + " [" + fileType + "]是 text");
            } else if (fileType.contains("application")) {
                //文档
                System.out.println(file.getContentType()  + " [" + fileType + "]是 application");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}