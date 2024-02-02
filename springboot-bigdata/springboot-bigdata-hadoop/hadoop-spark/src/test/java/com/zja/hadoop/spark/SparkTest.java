package com.zja.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author: zhengja
 * @since: 2024/02/02 15:59
 */
@SpringBootTest
public class SparkTest {

    /**
     * 使用本地模式
     * 过程：创建了一个Spark应用程序，它将创建一个包含整数的RDD，并计算每个整数的平方。最后，通过foreach操作打印结果。
     */
    @Test
    public void test_local() {
        // 创建Spark配置对象
        SparkConf conf = new SparkConf()
                .setAppName("SparkExample")
                .setMaster("local[*]"); // 使用本地模式，[*]表示使用所有可用的CPU核心

        // 创建Spark上下文对象
        try (JavaSparkContext sparkContext = new JavaSparkContext(conf)) {

            // 创建一个RDD（弹性分布式数据集）
            JavaRDD<Integer> numbersRDD = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 5));

            // 对RDD进行转换和操作
            JavaRDD<Integer> squaredRDD = numbersRDD.map(num -> num * num);

            // 打印结果
            // squaredRDD.foreach(System.out::println); // 会出现未序列化错误
            squaredRDD.foreachPartition(partition -> { // 不会出现未序列化错误
                while (partition.hasNext()) {
                    System.out.println(partition.next());
                }
            });
        }
    }


    /**
     * 使用Spark和YARN集成
     * 过程：创建了一个使用YARN集群模式的Spark应用程序。它将创建一个包含整数的RDD，并计算每个整数的平方。最后，通过foreach操作打印结果。
     */
    @Test
    public void test_yarn() {
        // 创建Spark配置对象
        SparkConf conf = new SparkConf()
                .setAppName("SparkYarnExample")
                .setMaster("yarn")
                .set("spark.submit.deployMode", "cluster"); // 设置提交模式为集群模式 可以是"client" 或 "cluster"，"cluster" 由集群管理器在集群中的某个节点上启动,"client"将在提交它的客户端机器上启动
        // .set("spark.submit.deployMode", "client"); // todo 无法在本地运行， Could not parse Master URL: 'yarn'
        // .set("spark.submit.deployMode", "cluster"); // todo 无法在本地运行， Detected yarn cluster mode, but isn't running on a cluster. Deployment to YARN is not supported directly by SparkContext. Please use spark-submit

        // 创建Spark上下文对象
        try (JavaSparkContext sparkContext = new JavaSparkContext(conf)) {
            // 创建一个RDD（弹性分布式数据集）
            JavaRDD<Integer> numbersRDD = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 5));

            // 对RDD进行转换和操作
            JavaRDD<Integer> squaredRDD = numbersRDD.map(num -> num * num);

            // 打印结果
            // squaredRDD.foreach(System.out::println); // 会出现未序列化错误
            squaredRDD.foreachPartition(partition -> { // 不会出现未序列化错误
                while (partition.hasNext()) {
                    System.out.println(partition.next());
                }
            });
        }
    }
}
