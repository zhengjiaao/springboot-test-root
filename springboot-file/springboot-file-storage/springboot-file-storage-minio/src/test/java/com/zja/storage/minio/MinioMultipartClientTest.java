package com.zja.storage.minio;

import com.google.common.collect.Lists;
import com.zja.storage.MinioApplicationTests;
import com.zja.storage.minio.args.*;
import com.zja.storage.util.OkHttpUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.ObjectWriteResponse;
import io.minio.UploadPartResponse;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    // 源文件
    public String sourceFilePath = "D:\\temp\\zip\\test.zip";

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

        // 5. 获取文件下载地址
        String fileUrl = minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1, TimeUnit.DAYS).build());
        System.out.println("合并后的文件下载地址 fileUrl：" + fileUrl);
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

        // 5. 完成分片上传
        ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .parts(partList)
                .build());
        System.out.println("完成分片上传 objectWriteResponse.object：" + objectWriteResponse.object());

        // 6. 获取文件下载地址
        String fileUrl = minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1, TimeUnit.DAYS).build());
        System.out.println("合并后的文件下载地址 fileUrl：" + fileUrl);
    }

    @Test
    public void testUploadPart_3() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 源文件
        File sourceFile = new File(sourceFilePath);

        // 分片文件
        List<String> chunkFilePaths = Lists.newArrayList(
                "D:\\temp\\zip\\test\\part1.part",
                "D:\\temp\\zip\\test\\part2.part",
                "D:\\temp\\zip\\test\\part3.part"
        );

        // 1. 申请上传ID
        String uploadId = minioMultipartClient.applyForUploadId(ApplyForUploadIdArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());
        System.out.println("uploadId：" + uploadId);

        // 2. 上传分片-通过服务端上传分片
        for (int i = 0; i < chunkFilePaths.size(); i++) {
            Path chunkFilePath = new File(chunkFilePaths.get(i)).toPath();
            // 上传分片
            UploadPartResponse uploadPartResponse = minioMultipartClient.uploadPart(UploadPartArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId)
                    .partData(Files.newInputStream(chunkFilePath))
                    .partSize(Files.size(chunkFilePath))
                    .partNumber(i + 1)
                    .build());
            System.out.println("分片" + uploadPartResponse.partNumber() + "上传成功，etag=" + uploadPartResponse.etag());
        }

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

        // 4. 验证合并分片前，进行验证分片数据量
        boolean validatedUploadBefore = minioMultipartClient.validateCompleteMultipartUploadBefore(ValidateCompleteMultipartUploadBeforeArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .objectPartSize(3)
                .build());
        System.out.println("分片上传验证-验证分片数据量：" + validatedUploadBefore);

        // 4.1 验证分片上传（方式2，减少一次查询）
        boolean validatedListParts = minioMultipartClient.validateListParts(listPartsResponse, 3);
        System.out.println("分片上传验证-validatedListParts：" + validatedListParts);

        // 5. 合并分片：进行完成分片上传
        ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .parts(partList)
                .build());
        System.out.println("合并分片，完成分片上传 objectName：" + objectWriteResponse.object());
        System.out.println("合并分片，完成分片上传 objectEtag：" + objectWriteResponse.etag());

        // 6. 验证合并分片后，进行验证对象完整性(无法验证)
        String sourceFileMd5 = DigestUtils.md5DigestAsHex(Files.newInputStream(sourceFile.toPath()));
        System.out.println("sourceFileMd5：" + sourceFileMd5);
        System.out.println("sourceFileLength：" + sourceFile.length());

        boolean validateCompleteMultipartUploadAfter = minioMultipartClient.validateCompleteMultipartUploadAfter(ValidateCompleteMultipartUploadAfterArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .matchETag(objectWriteResponse.etag())
                .matchLength(sourceFile.length())
                .build());
        System.out.println("分片上传验证-验证对象完整性：" + validateCompleteMultipartUploadAfter);

        // 7. 获取文件下载地址
        String fileUrl = minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1, TimeUnit.DAYS).build());
        System.out.println("合并后的文件下载地址 fileUrl：" + fileUrl);
    }

    // 支持验证分片上传（合并分片前验证、合并分片后验证-目前看，只能根据文件大小进行验证源完整性，推荐在合并前进行验证分片数量。）
    @Test
    public void testUploadPart_4() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 源文件
        File sourceFile = new File(sourceFilePath);

        // 分片文件
        List<String> chunkFilePaths = Lists.newArrayList(
                "D:\\temp\\zip\\test\\part1.part",
                "D:\\temp\\zip\\test\\part2.part",
                "D:\\temp\\zip\\test\\part3.part"
        );

        // 1. 申请上传ID
        String uploadId = minioMultipartClient.applyForUploadId(ApplyForUploadIdArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());
        System.out.println("uploadId：" + uploadId);

        // 2. 获取分片上传地址-通过客户端上传分片
        for (int i = 0; i < chunkFilePaths.size(); i++) {
            File chunkFile = new File(chunkFilePaths.get(i));
            // 获取分片上传地址
            String chunkUploadUrl = minioMultipartClient.getPresignedPartUrl(GetPresignedPartUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .uploadId(uploadId)
                    .partNumber(i + 1)
                    .build());

            System.out.println("chunkUploadUrl = " + chunkUploadUrl);

            // 上传分片
            OkHttpUtils.doPutUploadFile(chunkUploadUrl, new MockMultipartFile(objectName, Files.newInputStream(chunkFile.toPath())));
        }

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

        // 4. 验证合并分片前，进行验证分片数据量
        boolean validatedUploadBefore = minioMultipartClient.validateCompleteMultipartUploadBefore(ValidateCompleteMultipartUploadBeforeArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .objectPartSize(3)
                .build());
        System.out.println("分片上传验证-验证分片数据量：" + validatedUploadBefore);

        // 4.1 验证分片上传（方式2，减少一次查询）
        boolean validatedListParts = minioMultipartClient.validateListParts(listPartsResponse, 3);
        System.out.println("分片上传验证-validatedListParts：" + validatedListParts);

        // 5. 合并分片：进行完成分片上传
        ObjectWriteResponse objectWriteResponse = minioMultipartClient.completeMultipartUpload(CompleteMultipartUploadArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .uploadId(uploadId)
                .parts(partList)
                .build());
        System.out.println("合并分片，完成分片上传 objectName：" + objectWriteResponse.object());
        System.out.println("合并分片，完成分片上传 objectEtag：" + objectWriteResponse.etag());

        // 6. 验证合并分片后，进行验证对象完整性(验证名称存在、长度)
        String sourceFileMd5 = DigestUtils.md5DigestAsHex(Files.newInputStream(sourceFile.toPath()));
        System.out.println("sourceFileMd5：" + sourceFileMd5);
        System.out.println("sourceFileLength：" + sourceFile.length());

        boolean validateCompleteMultipartUploadAfter = minioMultipartClient.validateCompleteMultipartUploadAfter(ValidateCompleteMultipartUploadAfterArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .matchETag(objectWriteResponse.etag())
                .matchLength(sourceFile.length())
                .build());
        System.out.println("分片上传验证-验证对象完整性：" + validateCompleteMultipartUploadAfter);

        // 7. 获取文件下载地址
        String fileUrl = minioMultipartClient.getFileUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1, TimeUnit.DAYS).build());
        System.out.println("合并后的文件下载地址 fileUrl：" + fileUrl);
    }
}
