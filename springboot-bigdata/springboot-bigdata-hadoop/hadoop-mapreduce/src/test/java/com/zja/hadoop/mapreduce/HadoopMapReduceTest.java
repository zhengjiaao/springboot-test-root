package com.zja.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 当使用Hadoop的MapReduce框架时，通常需要定义Mapper和Reducer函数来自定义数据的处理逻辑。
 *
 * @author: zhengja
 * @since: 2024/02/01 16:29
 */
@Deprecated // todo 错误：系统找不到路径
@SpringBootTest
public class HadoopMapReduceTest {

    /**
     * MapReduce实例，用于计算给定文本文件中每个单词的出现次数：
     */
    @Test
    public void test() throws Exception {
        Configuration conf = new Configuration();
        // 获取 Windows 系统的临时目录路径
        String temporaryDir = "D:/test";  // 使用双反斜杠或正斜杠
        // 设置临时目录
        conf.set("fs.defaultFS", "file://" + temporaryDir);

        conf.set("mapreduce.framework.name", "yarn"); // 可以根据你的环境和需求将参数值设置为 "yarn" 或 "local", 若设置为 "local"，请确认你正在运行的是一个单节点的 Hadoop 环境，并且没有使用 YARN。

        // yarn 设置
        // conf.set("yarn.resourcemanager.address", "your_resourcemanager_address");
        // conf.set("yarn.resourcemanager.scheduler.address", "your_scheduler_address");
        conf.set("yarn.resourcemanager.address", "192.168.200.155:8032");
        conf.set("yarn.resourcemanager.scheduler.address", "192.168.200.155:8030");
        // 将应用程序的内存设置为 1024 MB，虚拟核心数设置为 1
        conf.set("yarn.app.mapreduce.am.resource.mb", "1024");
        conf.set("yarn.app.mapreduce.am.resource.cpu-vcores", "1");

        Job job = Job.getInstance(conf, "word count"); // 创建作业实例
        job.setJarByClass(HadoopMapReduceTest.class); // 设置作业的主类

        // 配置Mapper和Reducer
        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);

        // 配置输出键值对的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 配置输入和输出路径
        FileInputFormat.addInputPath(job, new Path("input.txt"));
        FileOutputFormat.setOutputPath(job, new Path("output"));

        // 提交作业并等待完成
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    // Mapper类
    public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable ONE = new IntWritable(1);
        private final Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, ONE); // 输出键值对到上下文
            }
        }
    }

    // Reducer类
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            result.set(sum);
            context.write(key, result); // 输出键值对到上下文
        }
    }

}
