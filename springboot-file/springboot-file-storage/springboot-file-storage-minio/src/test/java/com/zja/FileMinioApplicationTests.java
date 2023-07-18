/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-28 14:53
 * @Since:
 */
package com.zja;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileMinioApplicationTests {

    @Test
    public void test() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000/")
                    .credentials("admin", "password")
                    .build();

            //桶若不存在，就新建一个桶
            String bucketName = "testbuket";
            boolean found = minioClient.bucketExists(BucketExistsArgs.
                    builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            //上传文件
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object("minio-3840x2160.jpg")
                            //注意：本地磁盘的路径
                            .filename("D:\\temp\\images\\3840x2160.jpg")
                            .build()
            );
            System.out.println("上传成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
