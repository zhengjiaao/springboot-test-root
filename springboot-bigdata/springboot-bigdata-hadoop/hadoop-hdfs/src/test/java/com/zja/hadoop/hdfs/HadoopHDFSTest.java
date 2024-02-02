package com.zja.hadoop.hdfs;

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
 * @since: 2024/02/01 16:35
 */
@SpringBootTest
public class HadoopHDFSTest {

    @Test
    public void test() throws Exception {

        Configuration conf = new Configuration();
        // conf.set("fs.defaultFS", "hdfs://localhost:9000"); // 设置HDFS的默认文件系统

        // 创建HDFS文件系统对象
        // FileSystem fs = FileSystem.get(conf);
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
