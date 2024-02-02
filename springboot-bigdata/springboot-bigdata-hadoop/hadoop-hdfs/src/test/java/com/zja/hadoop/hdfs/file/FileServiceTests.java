/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-22 16:27
 * @Since:
 */
package com.zja.hadoop.hdfs.file;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/02/22 16:27
 */
@SpringBootTest
public class FileServiceTests {

    @Autowired
    FileService fileService;

    static String objectName = "3840x2160.jpg";
    static String filename = "D:\\temp\\images\\3840x2160.jpg";

    @Test
    public void file_test() throws Exception {

        // 上传
        fileService.uploadObject(objectName, filename);

        // 校验存在
        System.out.println(fileService.existObject(objectName));

        // 获取 InputStream
        InputStream inputStream = fileService.getObjectInputStream(objectName);
        System.out.println(inputStream.read());

        // 下载到本地
        String downloadPath = filename + ".download";
        fileService.downloadObject(objectName, downloadPath);
        System.out.println(downloadPath);

        // 删除文件
//        fileService.deleteObject(objectName);
    }

    @Test
    public void existObject_test() throws Exception {
        // 校验存在
        System.out.println(fileService.existObject(objectName));
    }

    @Test
    public void downloadObject_test() throws Exception {
        // 下载到本地
        String downloadPath = filename + ".download";
        fileService.downloadObject(objectName, downloadPath);
        System.out.println(downloadPath);
    }

    @Test
    public void deleteObject_test() throws Exception {
        // 删除文件
        fileService.deleteObject(objectName);
    }

}
