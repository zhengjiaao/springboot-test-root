package com.zja.file.minio.upload.service;

import com.google.common.collect.HashMultimap;
import com.zja.file.minio.upload.model.request.MergePartRequest;
import com.zja.file.minio.upload.model.response.ChunkUploadRes;
import com.zja.file.minio.upload.model.response.PartRes;
import com.zja.file.minio.upload.model.response.UploadPartRes;
import com.zja.file.minio.upload.storage.MinioMultipartClient;
import com.zja.file.minio.upload.model.request.UploadPartRequest;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author: zhengja
 * @Date: 2024-12-18 10:32
 */
@Slf4j
@Service
public class UploadService {

    @Autowired
    private MinioMultipartClient minioMultipartClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 申请大文件分片上传
     *
     * @param objectName 文件名
     * @param chunkCount 分片数量
     */
    public ChunkUploadRes applyForUploadingLargeFileFragments(String objectName, Integer chunkCount) {
        String uploadId = getUploadId(objectName, null);
        if (uploadId == null) {
            throw new RuntimeException("Failed to get upload ID");
        }

        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);

        List<String> uploadUrlList = IntStream.rangeClosed(1, chunkCount)
                .mapToObj(i -> {
                    reqParams.put("partNumber", String.valueOf(i));
                    return getPartPresignedObjectUrl(objectName, reqParams);
                })
                .collect(Collectors.toList());

        return ChunkUploadRes.builder()
                .uploadId(uploadId)
                .chunkUploadUrls(uploadUrlList)
                .build();
    }

    /**
     * 准备分片上传时，在此先获取上传任务id
     */
    public String getUploadId(String objectName, String contentType) {

        log.info("tip message: 通过 <{}-{}-{}> 开始初始化<分片上传>数据", objectName, contentType, bucketName);
        if (StringUtils.isBlank(contentType)) {
            contentType = "application/octet-stream";
        }
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        try {
            return minioMultipartClient.initMultiPartUpload(bucketName, null, objectName, headers, null);
        } catch (Exception e) {
            log.error("获取uploadId失败：", e);
            throw new UploadIdInitializationException("获取uploadId失败", e);
        }
    }

    /**
     * 获取分片上传的预签名URL
     */
    public String getPartPresignedObjectUrl(String objectName, Map<String, String> reqParams) {
        try {
            return minioMultipartClient.getPartPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.PUT).bucket(bucketName).object(objectName).expiry(1, TimeUnit.DAYS).extraQueryParams(reqParams).build());
        } catch (Exception e) {
            log.error("getPartPresignedObjectUrl failed:", e);
            throw new PresignedUrlGenerationException("getPartPresignedObjectUrl failed", e);
        }
    }

    /**
     * 上传分片
     */
    public UploadPartRes uploadPart(UploadPartRequest request) {
        try {
            MultipartFile file = request.getFile();
            UploadPartResponse uploadPartResponse = minioMultipartClient.uploadParts(bucketName, null, request.getObjectName(), file.getInputStream(), file.getSize(), request.getUploadId(), request.getPartNumber(), null, null);
            return UploadPartRes.builder().uploadId(uploadPartResponse.uploadId()).partNumber(uploadPartResponse.partNumber()).etag(uploadPartResponse.etag()).build();
        } catch (Exception e) {
            log.error("上传分片失败：", e);
            throw new MultipartUploadException("上传分片失败", e);
        }
    }

    /**
     * 获取已上传的分片列表
     */
    public List<PartRes> listParts(String uploadId, String objectName) {
        try {
            ListPartsResponse listPartsResponse = minioMultipartClient.getPartsList(bucketName, null, objectName, 1000, 0, uploadId, null, null);
            List<Part> partList = listPartsResponse.result().partList();
            log.info("已上传分片数量：{}", partList.size());
            return partList.stream().peek(part -> {
                log.info("分片etag：{}", part.etag());
                log.info("分片编号：{}", part.partNumber());
                log.info("分片大小：{}", part.partSize());
                log.info("分片最后修改时间：{}", part.lastModified());
            }).map(part -> PartRes.builder().etag(part.etag()).partNumber(part.partNumber()).lastModified(part.lastModified().toString()).size(part.partSize()).build()).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取分片列表失败：", e);
            throw new MultipartListException("获取分片列表失败", e);
        }
    }

    /**
     * 分片上传完后合并
     */
    public void mergePartUpload(MergePartRequest request) {
        try {
            String uploadId = request.getUploadId();
            String objectName = request.getObjectName();
            log.info("准备合并 <{} - {} - {}>", request.getObjectName(), request.getUploadId(), bucketName);
            // 查询上传后的分片数据
            ListPartsResponse listPartsResponse = minioMultipartClient.getPartsList(bucketName, null, objectName, 1000, 0, uploadId, null, null);
            int chunkCount = listPartsResponse.result().partList().size();
            log.info("分片数量：{}", chunkCount);
            if (chunkCount == 0) {
                throw new RuntimeException("分片数量为0，合并失败");
            }
            Part[] parts = new Part[chunkCount];
            for (int i = 0; i < chunkCount; i++) {
                Part part = listPartsResponse.result().partList().get(i);
                parts[i] = new Part(i + 1, part.etag());
            }

            // 合并分片
            ObjectWriteResponse objectWriteResponse = minioMultipartClient.mergeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
            log.info("合并成功: etag={}", objectWriteResponse.etag());
        } catch (Exception e) {
            log.error("合并失败：", e);
            throw new MultipartMergeException("合并分片失败", e);
        }
    }

    /**
     * 取消分片上传
     */
    public boolean cancelMultipartUpload(String objectName, String uploadId) {
        try {
            minioMultipartClient.cancelMultipartUpload(bucketName, null, objectName, uploadId, null, null);
        } catch (Exception e) {
            log.error("取消分片上传失败：", e);
            throw new MultipartCancelException("取消分片上传失败", e);
        }
        return true;
    }

    /**
     * 获取文件访问路径
     */
    public String getFileUrl(String objectName) {
        try {
            return minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(1, TimeUnit.DAYS).build());
        } catch (Exception e) {
            log.error("获取文件访问路径失败：", e);
            throw new MultipartCancelException("获取文件访问路径失败", e);
        }
    }


    // 自定义异常类
    public static class UploadIdInitializationException extends RuntimeException {
        public UploadIdInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MultipartUploadException extends RuntimeException {
        public MultipartUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class PresignedUrlGenerationException extends RuntimeException {
        public PresignedUrlGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MultipartMergeException extends RuntimeException {
        public MultipartMergeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MultipartListException extends RuntimeException {
        public MultipartListException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MultipartCancelException extends RuntimeException {
        public MultipartCancelException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
