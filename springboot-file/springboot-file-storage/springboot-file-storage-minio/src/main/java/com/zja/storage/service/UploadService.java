package com.zja.storage.service;

import com.zja.storage.minio.MinioMultipartClient;
import com.zja.storage.minio.args.*;
import com.zja.storage.model.request.MergePartRequest;
import com.zja.storage.model.request.UploadPartRequest;
import com.zja.storage.model.response.ChunkUploadRes;
import com.zja.storage.model.response.PartRes;
import com.zja.storage.model.response.UploadPartRes;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.ObjectWriteResponse;
import io.minio.UploadPartResponse;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
     * @param totalChunks 分片数量
     */
    public ChunkUploadRes applyForMultipartUpload(String objectName, Integer totalChunks) {
        try {
            String uploadId = minioMultipartClient.applyForUploadId(ApplyForUploadIdArgs.builder()
                    .bucket(bucketName)
                    .object(objectName).build());
            if (uploadId == null) {
                throw new RuntimeException("Failed to get upload ID");
            }

            List<String> uploadUrlList = IntStream.rangeClosed(1, totalChunks).mapToObj(i -> {
                return getPresignedPartUrl(objectName, uploadId, i);
            }).collect(Collectors.toList());

            return ChunkUploadRes.builder()
                    .uploadId(uploadId)
                    .chunkUploadUrls(uploadUrlList).build();
        } catch (Exception e) {
            log.error("申请失败：", e);
            throw new RuntimeException("申请失败", e);
        }
    }

    /**
     * 获取分片上传的预签名URL
     */
    private String getPresignedPartUrl(String objectName, String uploadId, int partNumber) {
        try {
            return minioMultipartClient.getPresignedPartUrl(GetPresignedPartUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .expiry(1, TimeUnit.DAYS).build());
        } catch (Exception e) {
            log.error("getPartPresignedObjectUrl failed:", e);
            throw new RuntimeException("getPartPresignedObjectUrl failed", e);
        }
    }

    /**
     * 上传分片
     */
    public UploadPartRes uploadPart(UploadPartRequest request) {
        try {
            MultipartFile file = request.getFile();
            UploadPartResponse uploadPartResponse = minioMultipartClient.uploadPart(UploadPartArgs.builder()
                    .bucket(bucketName)
                    .object(request.getObjectName())
                    .uploadId(request.getUploadId())
                    .partData(file.getInputStream())
                    .partSize(file.getSize())
                    .partNumber(request.getPartNumber())
                    .build());
            return UploadPartRes.builder().uploadId(uploadPartResponse.uploadId()).partNumber(uploadPartResponse.partNumber()).etag(uploadPartResponse.etag()).build();
        } catch (Exception e) {
            log.error("上传分片失败：", e);
            throw new RuntimeException("上传分片失败", e);
        }
    }

    /**
     * 获取已上传的分片列表
     */
    public List<PartRes> listParts(String uploadId, String objectName) {
        try {
            ListPartsResponse listPartsResponse = minioMultipartClient.listParts(ListPartsArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId).build());
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
            throw new RuntimeException("获取分片列表失败", e);
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
            ListPartsResponse listPartsResponse = minioMultipartClient.listParts(ListPartsArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId).build());
            List<Part> partList = listPartsResponse.result().partList();

            // 验证分片数量
            minioMultipartClient.validateListParts(listPartsResponse, request.getTotalChunks());

            // 合并分片：注，S3分片最小限制为5MB，也就是分片的大小必须大于5MB，同时小于5GB
            ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId)
                    .parts(partList)
                    .build());
            log.info("合并成功: etag={}", objectWriteResponse.etag());
        } catch (Exception e) {
            log.error("合并失败：", e);
            throw new RuntimeException("合并分片失败", e);
        }
    }

    /**
     * 取消分片上传
     */
    public void cancelMultipartUpload(String objectName, String uploadId) {
        try {
            minioMultipartClient.abortMultipartUpload(AbortMultipartUploadArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId).build());
        } catch (Exception e) {
            log.error("取消分片上传失败：", e);
            throw new RuntimeException("取消分片上传失败", e);
        }
    }

    /**
     * 获取合并后的文件访问地址（下载、预览等）
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
            throw new RuntimeException("获取文件访问路径失败", e);
        }
    }

}
