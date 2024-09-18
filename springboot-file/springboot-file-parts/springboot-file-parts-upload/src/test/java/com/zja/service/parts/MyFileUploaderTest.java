package com.zja.service.parts;

import com.zja.service.parts1.LargeFilePartsUploadImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author: zhengja
 * @since: 2024/01/19 16:36
 */
@SpringBootTest
public class MyFileUploaderTest {

    @Autowired
    LargeFilePartsUploadImpl myFileUploader;

    @Test
    public void test() throws IOException {
        File file = new File("D:\\temp\\word\\341000黄山市2020年年度体检报告.docx");
        String uploadDir = "D:\\temp\\word\\parts";
        String uploadId = "123456";

        myFileUploader.upload(Files.newInputStream(file.toPath()), file.getName(), file.length(), file.length(), 1, 1, uploadDir, uploadId);
    }

    @Test
    public void testUpload() throws IOException {
        // 设置测试数据
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String fileName = "test.txt";
        long fileSize = 1000;
        long chunkSize = 100;
        long totalChunks = fileSize / chunkSize;
        long currentChunk = 0;
        String uploadDir = "D:\\temp\\txt\\parts";
        String uploadId = "12345";

        // 调用方法
        myFileUploader.upload(inputStream, fileName, fileSize, chunkSize,
                totalChunks, currentChunk, uploadDir, uploadId);
    }

}
