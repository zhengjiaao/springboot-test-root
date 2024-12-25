package com.zja.storage.minio;

import com.zja.storage.MinioApplicationTests;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhengja
 * @Date: 2024-12-25 14:07
 */
public class MinioClientTest extends MinioApplicationTests {

    @Autowired
    MinioClient minioClient;

    @Test
    public void test() {
        try {
            // MinioClient minioClient = MinioClient.builder()
            //         .endpoint("http://127.0.0.1:9000/")
            //         .credentials("admin", "password").build();

            // 桶若不存在，就新建一个桶
            String bucketName = "test-buket";
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传文件
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object("test-minio.jpg")
                    .filename("D:\\temp\\images\\test.jpg").build()); // 注意：本地磁盘的路径
            System.out.println("上传成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
