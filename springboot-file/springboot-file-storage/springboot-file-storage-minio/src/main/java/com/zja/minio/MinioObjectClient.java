/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-28 14:52
 * @Since:
 */
package com.zja.minio;

import io.minio.*;
import io.minio.http.Method;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class MinioObjectClient {

    private MinioClient minioClient;

    public MinioObjectClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public ObjectWriteResponse putObject(String bucketName, String objectName, String filename) throws Exception {
        return minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filename)
                        .build());
    }

    public void putObjectFolder(String bucketName, String objectName, String foldername) throws Exception {
        if (Files.isDirectory(Paths.get(foldername))) {
            File dir = new File(foldername);
            if (0 <= dir.list().length) {
                uploadFolder(bucketName, objectName, dir);
            } else {
                throw new NullPointerException("Directory is null.");
            }
        } else {
            throw new NullPointerException("Not is Directory.");
        }
    }

    public void downloadObject(String bucketName, String objectName, String filename) throws Exception {
        minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(filename)
                .build());
    }

    public String getObjectShareLink(String bucketName, String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    public void deleteObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    private void uploadFolder(String bucketName, String parentName, File file) throws Exception {
        for (File fileElem : Objects.requireNonNull(file.listFiles())) {
            if (Files.isDirectory(Paths.get(fileElem.toURI()))) {
                uploadFolder(bucketName, parentName + "/" + fileElem.getName(), fileElem);
            } else {
                putObject(bucketName, parentName + "/" + fileElem.getName(), fileElem.getAbsolutePath());
            }
        }
    }

}
