package com.zja.storage.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Retention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 13:29
 */
@Slf4j
// @Component
public class MinioObjectClient extends MinioClient {

    public MinioObjectClient(MinioClient minioClient) {
        super(minioClient);
    }

    public ObjectWriteResponse uploadObject(String bucketName, String objectName, String filename) {
        try {
            return super.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filename)
                            .build());
        } catch (Exception e) {
            log.error("uploadObject failed:", e);
            throw new RuntimeException("uploadObject failed.", e);
        }
    }

    public void uploadFolder(String bucketName, String objectName, String foldername) {
        Path path = Paths.get(foldername).normalize();
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        File dir = path.toFile();
        String[] files = dir.list();
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("The directory is empty or cannot be accessed.");
        }

        uploadFolder(bucketName, objectName, dir);
    }

    private void uploadFolder(String bucketName, String parentName, File file) {
        for (File fileElem : Objects.requireNonNull(file.listFiles())) {
            if (Files.isDirectory(Paths.get(fileElem.toURI()))) {
                uploadFolder(bucketName, parentName + "/" + fileElem.getName(), fileElem);
            } else {
                uploadObject(bucketName, parentName + "/" + fileElem.getName(), fileElem.getAbsolutePath());
            }
        }
    }

    public void downloadObject(String bucketName, String objectName, String filename) {
        try {
            super.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(filename)
                    .build());
        } catch (Exception e) {
            log.error("downloadObject failed:", e);
            throw new RuntimeException("downloadObject failed.", e);
        }
    }

    public String getObjectShareLink(String bucketName, String objectName) {
        try {
            return super.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("getObjectShareLink failed:", e);
            throw new RuntimeException("getObjectShareLink failed.", e);
        }
    }

    public GetObjectResponse getObject(String bucketName, String objectName) {
        try {
            return super.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("getObject failed:", e);
            throw new RuntimeException("getObject failed.", e);
        }
    }

    public StatObjectResponse statObject(String bucketName, String objectName) {
        try {
            return super.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("statObject failed:", e);
            throw new RuntimeException("statObject failed.", e);
        }
    }

    public void deleteObject(String bucketName, String objectName) {
        try {
            super.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("deleteObject failed:", e);
            throw new RuntimeException("deleteObject failed.", e);
        }

    }

    public String getObjectUrl(String bucketName, String objectName) {
        return getObjectUrl(bucketName, objectName, 7, TimeUnit.DAYS);
    }

    public String getObjectUrl(String bucketName, String objectName, int duration, TimeUnit unit) {
        try {
            return super.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(duration, unit).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException |
                 IOException e) {
            log.error("获取对象url失败：{}", e.getMessage());
            throw new RuntimeException("获取对象url失败.", e);
        }
    }

    public String getObjectUploadUrl(String partBucketName, String partObjectName) {
        try {
            return super.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(partBucketName)
                    .object(partObjectName)
                    .method(Method.PUT)
                    .expiry(1, TimeUnit.DAYS).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            log.error("获取对象上传url失败：{}", e.getMessage());
            throw new RuntimeException("获取对象上传url失败.", e);
        }
    }

    public boolean isFileExists(String bucketName, String objectName) {
        return isFileExists(bucketName, objectName, null);
    }

    public boolean isFileExists(String bucketName, String objectName, String etag) {
        try {
            if (etag == null) {
                StatObjectResponse statObject = super.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
                return statObject != null;
            }
            return super.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()).etag().equals(etag);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException |
                 ServerException | XmlParserException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException e) {
            log.error("判断文件是否存在失败：{}", e.getMessage());
            throw new RuntimeException("判断文件是否存在失败.", e);
        }
    }

    /**
     * 合并分片
     *
     * @param objectName         合并后的文件名
     * @param partBucketName     分片文件所在桶
     * @param partObjectNameList 分片文件名集合
     */
    public void mergePart(String bucketName, String objectName, String partBucketName, List<String> partObjectNameList) {
        // 分片数据放到另一个桶里面：slice
        List<ComposeSource> sources = new ArrayList<>();
        for (String partObjectName : partObjectNameList) {
            sources.add(ComposeSource.builder()
                    .bucket(partBucketName)
                    .object(partObjectName).build());
        }

        // 合并：将分片合并成一个文件，放到指定桶里面
        final ComposeObjectArgs args = ComposeObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .sources(sources).build();

        try {
            super.composeObject(args);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("合并分片文件失败：{}", e.getMessage());
            throw new RuntimeException("合并分片文件失败.", e);
        }
    }

}
