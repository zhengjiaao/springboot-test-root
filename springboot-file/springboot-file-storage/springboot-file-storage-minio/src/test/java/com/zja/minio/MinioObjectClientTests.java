/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-28 15:30
 * @Since:
 */
package com.zja.minio;

import com.zja.FileMinioApplicationTests;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
import org.junit.Before;
import org.junit.Test;

public class MinioObjectClientTests extends FileMinioApplicationTests {

    MinioObjectClient minioObjectClient;

    String bucketName = "testbuket";
    String objectName = "3840x2160.jpg";
    String filename = "D:\\temp\\images\\3840x2160.jpg";

    @Before
    public void init() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000/")
                .credentials("admin", "password")
                .build();

        minioObjectClient = new MinioObjectClient(minioClient);
    }

    @Test
    public void putObject() throws Exception {
        minioObjectClient.putObject(
                bucketName,
                objectName,
                filename);
    }

    @Test
    public void downloadObject() throws Exception {
        minioObjectClient.downloadObject(
                bucketName,
                objectName,
                "D:\\temp\\3840x2160-minio.jpg");
    }

    @Test
    public void getObject() throws Exception {
        GetObjectResponse objectResponse = minioObjectClient.getObject(
                bucketName,
                objectName);
    }

    @Test
    public void getObjectInfo() throws Exception {
        StatObjectResponse objectInfo = minioObjectClient.getObjectInfo(
                bucketName,
                objectName);
        System.out.println(objectInfo);

        System.out.println(objectInfo.etag()); // md5
    }

    @Test
    public void getObjectShareLink() throws Exception {
        String shareLink = minioObjectClient.getObjectShareLink(
                bucketName,
                objectName);
        System.out.println(shareLink);
    }

    @Test
    public void deleteObject() throws Exception {
        minioObjectClient.deleteObject(
                bucketName,
                objectName);
    }

    @Test
    public void putObjectFolder() throws Exception {
        minioObjectClient.putObjectFolder(
                bucketName,
                "minio-path",
                "D:\\temp\\images");
    }

}
