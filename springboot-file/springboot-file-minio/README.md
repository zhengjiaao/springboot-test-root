# springboot-file-minio

- [minio 官网](https://min.io/)
- [minio github](https://github.com/minio/minio)

## 下载安装

- [minio download 页面](https://min.io/download#/windows)

安装启动

```batch
PS> Invoke-WebRequest -Uri "https://dl.min.io/server/minio/release/windows-amd64/minio.exe" -OutFile "C:\minio.exe"
PS> setx MINIO_ROOT_USER admin
PS> setx MINIO_ROOT_PASSWORD password
PS> C:\minio.exe server F:\Data --console-address ":9001"
```

## 依赖引入

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.4.3</version>
</dependency>
```

## 简单示例

```java
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
```
