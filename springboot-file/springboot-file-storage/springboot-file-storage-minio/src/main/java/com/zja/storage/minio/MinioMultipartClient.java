package com.zja.storage.minio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.zja.storage.minio.args.*;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: zhengja
 * @Date: 2024-12-18 10:19
 */
@Slf4j
// @Component
public class MinioMultipartClient extends MinioAsyncClient {

    public MinioMultipartClient(MinioAsyncClient minioAsyncClient) {
        super(minioAsyncClient);
    }

    /**
     * 申请上传id（uploadId）
     */
    public String applyForUploadId(ApplyForUploadIdArgs args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        CreateMultipartUploadResponse response = createMultipartUpload(args);
        if (response == null) {
            return null;
        }
        return response.result().uploadId();
    }

    /**
     * 创建多部分上传
     */
    public CreateMultipartUploadResponse createMultipartUpload(ApplyForUploadIdArgs args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            CompletableFuture<CreateMultipartUploadResponse> future = super.createMultipartUploadAsync(args.bucket(), args.region(), args.object(), args.extraHeaders(), args.extraQueryParams());
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 上传分片（推荐采用minio官网sdk封装的minioClient.uploadObject(UploadObjectArgs args)方法上传，底层已经封装好了分片上传逻辑，不需要自己重新封装。）
     */
    public UploadPartResponse uploadPart(UploadPartArgs args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            return uploadPartAsync(args).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 上传分片-异步
     */
    public CompletableFuture<UploadPartResponse> uploadPartAsync(UploadPartArgs args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException {
        return super.uploadPartAsync(args.bucket(), args.region(), args.object(), args.partData(), args.partSize(), args.uploadId(), args.partNumber(), args.extraHeaders(), args.extraQueryParams());
    }

    /**
     * 获取分片上传的预签名URL(通过此 预签名URL PUT请求进行上传分片)
     */
    public String getPresignedPartUrl(GetPresignedPartUrlArgs args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Multimap<String, String> queryParams = HashMultimap.create();

        if (args.extraQueryParams() != null) {
            queryParams.putAll(args.extraQueryParams());
        }
        queryParams.put("uploadId", args.uploadId());
        queryParams.put("partNumber", String.valueOf(args.partNumber()));

        Multimap<String, String> extraQueryParams = Multimaps.unmodifiableMultimap(queryParams);

        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder().method(Method.PUT).bucket(args.bucket()).object(args.object()).region(args.region()).expiry(args.expiry()).extraHeaders(args.extraHeaders()).versionId(args.versionId()).extraQueryParams(extraQueryParams).build();

        return super.getPresignedObjectUrl(urlArgs);
    }

    /**
     * 查询指定对象（即文件）的已上传分片列表。
     * 应用场景：当你已经开始了某个对象的分片上传，并且想要查看该对象当前已经上传了哪些分片时使用。通常用于在合并分片之前，确认所有分片是否都已经成功上传。
     */
    public ListPartsResponse listParts(ListPartsArgs args) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            return super.listPartsAsync(args.bucket(), args.region(), args.object(), args.maxParts(), args.partNumberMarker(), args.uploadId(), args.extraHeaders(), args.extraQueryParams()).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 查询指定存储桶中正在进行的多部分上传任务列表。
     * 应用场景：当你想要查看某个存储桶中所有正在进行的多部分上传任务时使用。这可以帮助你了解当前有哪些文件正在分片上传，但还没有完成合并。
     */
    public ListMultipartUploadsResponse listMultipartUploads(ListMultipartUploadsArgs args) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            return super.listMultipartUploadsAsync(args.bucket(), args.region(), args.delimiter(), args.encodingType(), args.keyMarker(), args.maxUploads(), args.prefix(), args.uploadIdMarker(), args.extraHeaders(), args.extraQueryParams()).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 合并分片：完成分片上传，执行合并文件
     */
    public ObjectWriteResponse completeMultipartUpload(CompleteMultipartUploadArgs args) throws IOException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, InvalidKeyException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            return completeMultipartUploadAsync(args).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 合并分片-异步：完成分片上传，执行合并文件
     */
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(CompleteMultipartUploadArgs args) throws IOException, NoSuchAlgorithmException, InsufficientDataException, InternalException, XmlParserException, InvalidKeyException {
        return super.completeMultipartUploadAsync(args.bucket(), args.region(), args.object(), args.uploadId(), args.parts(), args.extraHeaders(), args.extraQueryParams());
    }

    /**
     * 验证合并分片前，进行验证分片数据量
     */
    public boolean validateCompleteMultipartUploadBefore(ValidateCompleteMultipartUploadBeforeArgs args) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        ListPartsResponse listPartsResponse = listParts(ListPartsArgs.builder()
                .bucket(args.bucket())
                .object(args.object())
                .region(args.region())
                .uploadId(args.uploadId())
                .maxParts(10000)
                .extraHeaders(args.extraHeaders())
                .extraQueryParams(args.extraQueryParams()).build());
        return validateListParts(listPartsResponse, args.objectPartSize());
    }

    // 完成合并分片前，进行验证分片数据量
    public boolean validateListParts(ListPartsResponse partsResponse, long objectPartSize) {
        if (partsResponse == null || partsResponse.result() == null || partsResponse.result().partList() == null) {
            throw new RuntimeException("分片上传未开始，请先上传分片！");
        }

        List<Part> partList = partsResponse.result().partList();
        int size = partList.size();

        if (size == objectPartSize) {
            // 分片上传已结束
            return true;
        } else if (size < objectPartSize) {
            // 分片上传未完成
            return false;
        } else {
            // 分片上传已超限
            throw new RuntimeException("分片上传已超限，请重新申请上传分片！");
        }
    }

    /**
     * 验证合并分片后，进行验证对象完整性（支持按对象名称、对象ETag、对象长度等可选条件匹配验证是否上传完整）
     */
    public boolean validateCompleteMultipartUploadAfter(ValidateCompleteMultipartUploadAfterArgs args) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            StatObjectResponse objectResponse = super.statObjectAsync(StatObjectArgs.builder()
                    .bucket(args.bucket())
                    .region(args.region())
                    .object(args.object())
                    .matchETag(args.matchETag())
                    .extraHeaders(args.extraHeaders())
                    .extraQueryParams(args.extraQueryParams()).build()).get();
            if (objectResponse == null || objectResponse.deleteMarker()) {
                return false;
            }
            return args.matchLength() == 0 || args.matchLength() == objectResponse.size();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 取消分片上传
     */
    public AbortMultipartUploadResponse abortMultipartUpload(AbortMultipartUploadArgs args) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {
        try {
            CompletableFuture<AbortMultipartUploadResponse> future = super.abortMultipartUploadAsync(args.bucket(), args.region(), args.object(), args.uploadId(), args.extraHeaders(), args.extraQueryParams());
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            super.throwEncapsulatedException(e);
            return null;
        }
    }

    /**
     * 取消分片上传-异步
     */
    public CompletableFuture<AbortMultipartUploadResponse> abortMultipartUploadAsync(AbortMultipartUploadArgs args) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException {
        return super.abortMultipartUploadAsync(args.bucket(), args.region(), args.object(), args.uploadId(), args.extraHeaders(), args.extraQueryParams());
    }

    /**
     * 获取文件链接地址（用于预览、下载等）
     */
    public String getFileUrl(GetPresignedObjectUrlArgs args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return super.getPresignedObjectUrl(args);
    }
}
