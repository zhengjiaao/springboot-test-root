package com.zja.file.minio.upload.storage;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: zhengja
 * @Date: 2024-12-18 10:19
 */
@Slf4j
public class MinioMultipartClient extends MinioAsyncClient {

    public MinioMultipartClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 初始化分片上传即获取 uploadId
     */
    public String initMultiPartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, ExecutionException, InterruptedException {
        CompletableFuture<CreateMultipartUploadResponse> future = super.createMultipartUploadAsync(bucket, region, object, headers, extraQueryParams);
        CreateMultipartUploadResponse uploadResponse = future.get();
        return uploadResponse.result().uploadId();
    }

    /**
     * 上传分片
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param data             数据，示例：InputStream or RandomAccessFile
     * @param uploadId         上传ID
     * @param partNumber       分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    public UploadPartResponse uploadParts(String bucketName, String region, String objectName, Object data, long length, String uploadId, int partNumber, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, ExecutionException, InterruptedException {
        return super.uploadPartAsync(bucketName, region, objectName, data, length, uploadId, partNumber, extraHeaders, extraQueryParams).get();
    }

    /**
     * 获取分片上传的预签名URL(可以通过此URL上传分片文件)
     */
    public String getPartPresignedObjectUrl(GetPresignedObjectUrlArgs reqParams) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return super.getPresignedObjectUrl(reqParams);
    }

    /**
     * 功能：查询指定对象（即文件）的已上传分片列表。
     * 应用场景：当你已经开始了某个对象的分片上传，并且想要查看该对象当前已经上传了哪些分片时使用。通常用于在合并分片之前，确认所有分片是否都已经成功上传。
     */
    public ListPartsResponse getPartsList(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams).get();
    }

    /**
     * 功能：查询指定存储桶中正在进行的多部分上传任务列表。
     * 应用场景：当你想要查看某个存储桶中所有正在进行的多部分上传任务时使用。这可以帮助你了解当前有哪些文件正在分片上传，但还没有完成合并。
     */
    public ListMultipartUploadsResponse getMultipartUploadList(String bucketName, String region, String delimiter, String encodingType, String keyMarker, Integer maxUploads, String prefix, String uploadIdMarker, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return super.listMultipartUploadsAsync(bucketName, region, delimiter, encodingType, keyMarker, maxUploads, prefix, uploadIdMarker, extraHeaders, extraQueryParams).get();
    }

    /**
     * 合并分片：完成分片上传，执行合并文件
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param parts            分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    public ObjectWriteResponse mergeMultipartUpload(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws IOException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, InvalidKeyException, ExecutionException, InterruptedException {
        return super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams).get();
    }

    /**
     * 取消分片上传
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    public void cancelMultipartUpload(String bucketName, String region, String objectName, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        CompletableFuture<AbortMultipartUploadResponse> future = super.abortMultipartUploadAsync(bucketName, region, objectName, uploadId, extraHeaders, extraQueryParams);
        future.get();
    }

    /**
     * 获取文件链接地址
     */
    public String getFileUrl(GetPresignedObjectUrlArgs reqParams) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return super.getPresignedObjectUrl(reqParams);
    }
}
