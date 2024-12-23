package com.zja.file.minio.upload;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: zhengja
 * @Date: 2024-12-18 10:06
 */
public class MinioFileUploaderTest {

    private MinioClient minioClient;

    @BeforeEach
    public void init() {
        // Create a minioClient with the MinIO server playground, its access key and secret key.
        minioClient = MinioClient.builder()
                .endpoint("https://play.min.io")
                .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                .build();
    }

    @Test
    public void test() {
        try {
            // Make 'asiatrip' bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("asiatrip")
                            .object("asiaphotos-2015.zip")
                            .filename("D:\\temp\\zip\\asiaphotos.zip")
                            .build());
            System.out.println("'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                    + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
