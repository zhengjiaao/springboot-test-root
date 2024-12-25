package com.zja.storage.minio;

import com.zja.storage.MinioApplicationTests;
import com.zja.storage.minio.args.*;
import com.zja.storage.util.OkHttpUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-12-25 14:04
 */
public class MinioMultipartClientTest extends MinioApplicationTests {

    @Autowired
    MinioMultipartClient minioMultipartClient;

    @Value("${minio.bucketName}")
    public String bucketName;

    public String objectName = "test.zip";

    // 服务端上传分片
    @Test
    public void testUploadPart_1() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        // 1. 申请上传ID
        String uploadId = minioMultipartClient.applyForUploadId(ApplyForUploadIdArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());
        System.out.println("uploadId：" + uploadId);

        // 2 上传分片-通过服务端上传分片
        minioMultipartClient.uploadPart(UploadPartArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partData(Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part1.part")))
                .partSize(Files.size(Paths.get("D:\\temp\\zip\\test\\part1.part")))
                .partNumber(1)
                .build());

        minioMultipartClient.uploadPart(UploadPartArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partData(Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part2.part")))
                .partSize(Files.size(Paths.get("D:\\temp\\zip\\test\\part2.part")))
                .partNumber(2)
                .build());

        minioMultipartClient.uploadPart(UploadPartArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partData(Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part3.part")))
                .partSize(Files.size(Paths.get("D:\\temp\\zip\\test\\part3.part")))
                .partNumber(3)
                .build());

        // 3. 验证分片上传
        // minioMultipartClient.validateMultipartUpload(ApplyForUploadIdArgs.builder()
        //         .bucket(bucketName)
        //         .object(objectName)
        //         .uploadId(uploadId)
        //         .build());

        // 3. 列出分片上传
        ListPartsResponse listPartsResponse = minioMultipartClient.listParts(ListPartsArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId).build());
        List<Part> partList = listPartsResponse.result().partList();
        int chunkCount = listPartsResponse.result().partList().size();
        System.out.println("分片数量：" + chunkCount);
        if (chunkCount == 0) {
            throw new RuntimeException("分片数量为[0].");
        }
        for (int i = 0; i < chunkCount; i++) {
            Part part = partList.get(i);
            System.out.println("分片" + part.partNumber() + "上传成功，etag=" + part.etag());
        }

        // 4. 完成分片上传
        ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .parts(partList)
                .build());
        System.out.println("完成分片上传 objectWriteResponse.object：" + objectWriteResponse.object());
    }


    // 客户端上传分片(模拟，例如：浏览器请求)
    @Test
    public void testUploadPart_2() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        // 1. 申请上传ID
        String uploadId = minioMultipartClient.applyForUploadId(ApplyForUploadIdArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());
        System.out.println("uploadId：" + uploadId);

        // 2. 获取分片上传地址-通过客户端上传分片
        String presignedPartUrl1 = minioMultipartClient.getPresignedPartUrl(GetPresignedPartUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partNumber(1)
                .build());
        String presignedPartUrl2 = minioMultipartClient.getPresignedPartUrl(GetPresignedPartUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partNumber(2)
                .build());
        String presignedPartUrl3 = minioMultipartClient.getPresignedPartUrl(GetPresignedPartUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .partNumber(3)
                .build());

        System.out.println("presignedPartUrl1：" + presignedPartUrl1);
        System.out.println("presignedPartUrl2：" + presignedPartUrl2);
        System.out.println("presignedPartUrl3：" + presignedPartUrl3);

        // 3. 上传分片-通过客户端上传分片
        OkHttpUtils.doPutUploadFile(presignedPartUrl1, new MockMultipartFile(
                objectName,
                Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part1.part"))));

        OkHttpUtils.doPutUploadFile(presignedPartUrl2, new MockMultipartFile(
                objectName,
                Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part2.part"))));

        OkHttpUtils.doPutUploadFile(presignedPartUrl3, new MockMultipartFile(
                objectName,
                Files.newInputStream(Paths.get("D:\\temp\\zip\\test\\part3.part"))));

        // 4. 列出分片上传
        ListPartsResponse listPartsResponse = minioMultipartClient.listParts(ListPartsArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId).build());
        List<Part> partList = listPartsResponse.result().partList();
        int chunkCount = listPartsResponse.result().partList().size();
        System.out.println("分片数量：" + chunkCount);
        if (chunkCount == 0) {
            throw new RuntimeException("分片数量为[0].");
        }
        for (int i = 0; i < chunkCount; i++) {
            Part part = partList.get(i);
            System.out.println("分片" + part.partNumber() + "上传成功，etag=" + part.etag());
        }

        // 5. 验证分片上传
        boolean validated = validateCompleteMultipartUpload(listPartsResponse, 3);
        System.out.println("分片上传验证：" + validated);

        // 6. 完成分片上传
        ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .parts(partList)
                .build());
        System.out.println("完成分片上传 objectWriteResponse.object：" + objectWriteResponse.object());

        // 7. 获取文件下载地址
        String fileUrl = minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1, TimeUnit.DAYS).build());
        System.out.println("fileUrl：" + fileUrl);
    }

    public boolean validateCompleteMultipartUpload(ListPartsResponse partsResponse, long maxParts) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {
        if (partsResponse == null || partsResponse.result() == null || partsResponse.result().partList() == null) {
            throw new MultipartListException("分片上传未开始，请先上传分片！");
        }

        List<Part> partList = partsResponse.result().partList();
        int size = partList.size();

        if (size == maxParts) {
            // 分片上传已结束
            return true;
        } else if (size < maxParts) {
            // 分片上传未完成
            return false;
        } else {
            // 分片上传已超限
            throw new MultipartListException("分片上传已超限，请重新申请上传分片！");
        }
    }

    public static class MultipartListException extends RuntimeException {
        public MultipartListException(String message) {
            super(message);
        }
    }

}
