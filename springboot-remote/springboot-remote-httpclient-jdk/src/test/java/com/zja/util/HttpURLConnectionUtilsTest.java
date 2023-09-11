/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-11 9:28
 * @Since:
 */
package com.zja.util;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/09/11 9:28
 */
public class HttpURLConnectionUtilsTest {

    @Test
    public void testDoGet() throws IOException {
        String url = "http://localhost:19000/get";
//        String url = "http://localhost:19000/get/param/v1";
        Map<String, String> params = new HashMap<>();
        params.put("param", "value1");

        String response = HttpURLConnectionUtils.doGet(url, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testDoPostWithFormParams() throws IOException {
//        String url = "http://localhost:19000/post";
        String url = "http://localhost:19000/post/param";
        Map<String, String> params = new HashMap<>();
        params.put("name", "value1");
        params.put("age", "value2");

        String response = HttpURLConnectionUtils.doPost(url, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testDoPostWithBodyParams() throws IOException {
//        String url = "http://localhost:19000/post/object/v1";
        String url = "http://localhost:19000/post/object/v2";
        Map<String, String> params = new HashMap<>();
        params.put("id", "value1");
        params.put("name", "value2");

        String response = HttpURLConnectionUtils.doPost(url, JSON.toJSONString(params));

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testDoPut() throws IOException {
//        String url = "http://localhost:19000/put";
        String url = "http://localhost:19000/put/param";
        Map<String, String> params = new HashMap<>();
        params.put("param", "value1");

        String response = HttpURLConnectionUtils.doPut(url, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testDoDelete() throws IOException {
//        String url = "http://localhost:19000/delete";
        String url = "http://localhost:19000/delete/param";
        Map<String, String> params = new HashMap<>();
        params.put("param", "value1");

        String response = HttpURLConnectionUtils.doDelete(url, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testdoPostUploadFile() throws IOException {
        String url = "http://localhost:19000/post/upload/v1";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        String response = HttpURLConnectionUtils.doPostUploadFile(url, file);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testdoPostUploadFileWithParams() throws IOException {
        String url = "http://localhost:19000/post/upload/v3";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        Map<String, String> params = new HashMap<>();
        params.put("filename", "test2.txt");

        String response = HttpURLConnectionUtils.doPostUploadFile(url, file, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }


    @Test
    public void testdoPostUploadFileList() throws IOException {
        String url = "http://localhost:19000/post/upload/v2";
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "Hello, World 1!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "Hello, World 2!".getBytes());
        files.add(file1);
        files.add(file2);

        String response = HttpURLConnectionUtils.doPostUploadFiles(url, files);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testdoPostUploadFileListWithParams() throws IOException {
        String url = "http://localhost:19000/post/upload/v5";
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "Hello, World 1!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "Hello, World 2!".getBytes());
        files.add(file1);
        files.add(file2);
        Map<String, String> params = new HashMap<>();
        params.put("filename", "value1");

        String response = HttpURLConnectionUtils.doPostUploadFiles(url, files, params);

        // Add your assertions here
        Assert.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testDoGetDownloadFile() throws IOException {
        // Arrange
        String url = "http://localhost:19000/file/jpg.jpg";
//        String url = "http://localhost:19000/get/download/v2";
        Map<String, String> params = new HashMap<>();
        params.put("filename", "jpg.jpg");
        // Act
        InputStream stream = HttpURLConnectionUtils.doGetDownloadFile(url, params);

        // Assert
        Assertions.assertNotNull(stream);
        // Add more assertions as needed
        System.out.println(stream.available());
        saveInputStreamToFile(stream,"target/jpg1.jpg");
    }

    @Test
    public void testDoPostDownloadFile() throws IOException {
        // Arrange
//        String url = "http://localhost:19000/file/jpg.jpg";
        String url = "http://localhost:19000/post/download/v1";

        Map<String, String> params = new HashMap<>();
        params.put("filename", "jpg.jpg");
        // Act
        InputStream stream = HttpURLConnectionUtils.doPostDownloadFile(url, params);

        // Assert
        Assertions.assertNotNull(stream);
        // Add more assertions as needed
        System.out.println(stream.available());
        saveInputStreamToFile(stream,"target/jpg2.jpg");
    }


    public static void saveInputStreamToFile(InputStream inputStream, String filePath) throws IOException {
        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }
}
