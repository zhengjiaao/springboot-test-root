package com.zja.storage.minio;

import com.zja.storage.MinioApplicationTests;
import io.minio.GetObjectResponse;
import io.minio.StatObjectResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MinioObjectClientTest extends MinioApplicationTests {

    @Autowired
    MinioObjectClient minioObjectClient;

    @Value("${minio.bucketName}")
    public String bucketName;

    public String objectName = "test.jpg";

    String filename = "D:\\temp\\images\\test.jpg";

    @Test
    public void putObject() {
        minioObjectClient.uploadObject(
                bucketName,
                objectName,
                filename);
    }

    @Test
    public void downloadObject() {
        minioObjectClient.downloadObject(
                bucketName,
                objectName,
                "D:\\temp\\test-minio-download.jpg");
    }

    @Test
    public void getObject() {
        GetObjectResponse objectResponse = minioObjectClient.getObject(
                bucketName,
                objectName);
    }

    @Test
    public void getObjectInfo() {
        StatObjectResponse objectInfo = minioObjectClient.statObject(
                bucketName,
                objectName);
        System.out.println(objectInfo);

        System.out.println(objectInfo.etag()); // md5
    }

    @Test
    public void getObjectShareLink() {
        String shareLink = minioObjectClient.getObjectShareLink(
                bucketName,
                objectName);
        System.out.println(shareLink);
    }

    @Test
    public void deleteObject() {
        minioObjectClient.deleteObject(
                bucketName,
                objectName);
    }

    @Test
    public void putObjectFolder() {
        minioObjectClient.uploadFolder(
                bucketName,
                "动物",
                "D:\\temp\\images\\动物");
    }
}
