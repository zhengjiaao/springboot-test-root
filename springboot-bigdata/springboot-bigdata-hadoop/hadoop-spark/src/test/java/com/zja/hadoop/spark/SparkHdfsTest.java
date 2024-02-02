package com.zja.hadoop.spark;

import lombok.val;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/02/01 16:23
 */
@SpringBootTest
public class SparkHdfsTest {

    @Test
    public void test_spark_hdfs() {
        // 创建Spark配置
        SparkConf sparkConf = new SparkConf()
                .setAppName("HadoopSparkExample")
                .setMaster("local"); // 在本地模式下运行Spark

        // 创建Spark上下文对象
        try (JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)) {

            // 读取HDFS上的文件
            // JavaRDD<String> lines = sparkContext.textFile("hdfs://localhost:9000/path/to/input.txt");
            JavaRDD<String> lines = sparkContext.textFile("hdfs://192.168.200.154:8020/ds/test.txt");
            // 打印结果
            // lines.foreach(System.out::println); // 会出现未序列化错误
            lines.foreachPartition(partition -> { // 不会出现未序列化错误
                while (partition.hasNext()) {
                    System.out.println(partition.next());
                }
            });

            // 对文件内容进行处理
            JavaRDD<String> processedLines = lines.map(String::toUpperCase);
            // 处理结果
            // processedLines.foreachPartition(partition -> {
            //     while (partition.hasNext()) {
            //         System.out.println(partition.next());
            //     }
            // });

            // 创建Hadoop配置对象
            // Configuration hadoopConf = sparkContext.hadoopConfiguration();

            // 设置HDFS用户(可选，若没有设置用户权限，则不需要配置)
            // String hdfsUser = "your-hdfs-user"; // 替换为实际的HDFS用户名
            String hdfsUser = "hdfs"; // 替换为实际的HDFS用户名
            UserGroupInformation ugi = UserGroupInformation.createRemoteUser(hdfsUser);
            UserGroupInformation.setLoginUser(ugi);

            // 将处理后的结果写入HDFS
            // processedLines.saveAsTextFile("hdfs://localhost:9000/path/to/output"); // 上传的结果，存在是分片的
            processedLines.saveAsTextFile("hdfs://192.168.200.154:8020/ds/output");

            System.out.println("Job completed successfully.");
        }
    }

}
