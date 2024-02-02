package com.zja.hadoop.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author: zhengja
 * @since: 2024/02/02 14:53
 */
@SpringBootTest
public class ConfigurationTest {

    @Test
    public void test_file() {
        // 创建 Hadoop 配置对象
        Configuration conf = new Configuration();

        // 获取 Windows 系统的临时目录路径
        String temporaryDir = "D:/test";  // 使用双反斜杠或正斜杠

        // 设置临时目录
        conf.set("fs.defaultFS", "file://" + temporaryDir);

        try {
            // 获取文件系统实例
            FileSystem fs = FileSystem.get(conf);

            // 在临时目录下创建一个文件
            Path tempFile = new Path(temporaryDir + "/temp.txt");
            fs.create(tempFile);

            // 输出文件路径
            System.out.println(tempFile);

            // 关闭文件系统连接
            fs.close();

            System.out.println("临时文件创建完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_hdfs() throws Exception {


        // Configuration conf = new Configuration();
        // conf.set("fs.defaultFS", "hdfs://localhost:9000"); // 设置HDFS的默认文件系统

        // 创建HDFS文件系统对象
        // FileSystem fs = FileSystem.get(conf);
        // 创建HDFS文件系统对象
        FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), new Configuration(), "hdfs");

        try {
            // 在HDFS上创建一个新的目录
            // Path dirPath = new Path("/example");
            Path dirPath = new Path("/ds");
            fs.mkdirs(dirPath);
            System.out.println("Created directory: " + dirPath);

            // 在HDFS上创建一个新的文件
            // Path filePath = new Path("/example/test.txt");
            Path filePath = new Path("/ds/test.txt");
            OutputStream os = fs.create(filePath);
            os.write("Hello, HDFS!".getBytes());
            os.close();
            System.out.println("Created file: " + filePath);

            // 从HDFS上读取文件内容
            InputStream is = fs.open(filePath);
            byte[] buffer = new byte[1024];
            int bytesRead = is.read(buffer);
            String content = new String(buffer, 0, bytesRead);
            is.close();
            System.out.println("File content: " + content);
        } finally {
            // 关闭HDFS文件系统对象
            fs.close();
        }
    }


}
