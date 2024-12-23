package com.zja.file.minio.upload.service;

import com.google.common.collect.Lists;
import com.zja.file.minio.upload.model.request.MergePartRequest;
import com.zja.file.minio.upload.model.request.UploadPartRequest;
import com.zja.file.minio.upload.model.response.ChunkUploadRes;
import com.zja.file.minio.upload.model.response.UploadPartRes;
import com.zja.file.minio.upload.util.OkHttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-12-23 16:31
 */
@SpringBootTest
public class UploadServiceTest {

    @Autowired
    UploadService uploadService;

    String objectName = "test.zip";

    // 服务端分片上传
    @Test
    public void test_1() throws IOException {
        List<String> chunkFilePaths = Lists.newArrayList(
                "D:\\temp\\zip\\test\\part1.part",
                "D:\\temp\\zip\\test\\part2.part",
                "D:\\temp\\zip\\test\\part3.part"
        );

        // 申请分片上传
        ChunkUploadRes chunkUploadRes = uploadService.applyForUploadingLargeFileFragments(objectName, chunkFilePaths.size());

        String uploadId = chunkUploadRes.getUploadId();
        System.out.println("uploadId = " + uploadId);
        List<String> chunkUploadUrls = chunkUploadRes.getChunkUploadUrls();
        System.out.println("chunkUploadUrls = " + chunkUploadUrls);

        // 上传分片（采用服务端SDK上传）
        for (int i = 0; i < chunkFilePaths.size(); i++) {
            File chunkFile = new File(chunkFilePaths.get(i));
            MockMultipartFile multipartFile = new MockMultipartFile(chunkFile.getName(), Files.newInputStream(chunkFile.toPath()));
            UploadPartRes uploadPartRes = uploadService.uploadPart(
                    UploadPartRequest.builder()
                            .uploadId(uploadId)
                            .objectName(objectName)
                            .partNumber(i + 1)
                            .file(multipartFile).build()
            );
            System.out.println("uploadPartRes = " + uploadPartRes);
        }

        // 合并分片
        uploadService.mergePartUpload(MergePartRequest.builder().uploadId(uploadId).objectName(objectName).chunks(chunkFilePaths.size()).build());

        // 获取文件url
        String fileUrl = uploadService.getFileUrl(objectName);
        System.out.println("fileUrl = " + fileUrl);
    }

    // 客户端分片上传（前端直接通过url上传到minio，不走服务端，省去一次网络IO开销）
    @Test
    public void test_2() throws IOException {
        List<String> chunkFilePaths = Lists.newArrayList(
                "D:\\temp\\zip\\test\\part1.part",
                "D:\\temp\\zip\\test\\part2.part",
                "D:\\temp\\zip\\test\\part3.part"
        );

        // 申请分片上传
        ChunkUploadRes chunkUploadRes = uploadService.applyForUploadingLargeFileFragments(objectName, chunkFilePaths.size());

        String uploadId = chunkUploadRes.getUploadId();
        System.out.println("uploadId = " + uploadId);
        List<String> chunkUploadUrls = chunkUploadRes.getChunkUploadUrls();
        System.out.println("chunkUploadUrls = " + chunkUploadUrls);

        // 上传分片(采用客户端URL上传)
        for (int i = 0; i < chunkFilePaths.size(); i++) {
            File chunkFile = new File(chunkFilePaths.get(i));
            String chunkUploadUr = chunkUploadUrls.get(i);

            System.out.println("chunkUploadUr = " + chunkUploadUr);

            OkHttpUtils.doPutUploadFile(chunkUploadUr, new MockMultipartFile(objectName, Files.newInputStream(chunkFile.toPath())));
        }

        // 合并分片
        uploadService.mergePartUpload(MergePartRequest.builder()
                .uploadId(uploadId)
                .objectName(objectName)
                .chunks(chunkFilePaths.size()).build());

        // 获取文件url
        String fileUrl = uploadService.getFileUrl(objectName);
        System.out.println("fileUrl = " + fileUrl);
    }

    @Test
    public void test_3() {
        String fileUrl = uploadService.getFileUrl(objectName);
        System.out.println("fileUrl = " + fileUrl);
    }

}
