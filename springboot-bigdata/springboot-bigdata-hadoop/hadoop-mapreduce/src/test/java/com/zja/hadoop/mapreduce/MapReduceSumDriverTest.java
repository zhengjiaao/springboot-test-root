package com.zja.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/02/04 10:03
 */
@SpringBootTest
public class MapReduceSumDriverTest {

    /**
     * 这里是一个简单的MapReduce实例，展示了如何使用MapReduce框架计算一组整数的总和。
     */
    @Test
    public void test() throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Sum");
        job.setJarByClass(MapReduceSumDriverTest.class);

        // 设置Mapper类和Reducer类
        job.setMapperClass(SumMapper.class);
        job.setReducerClass(SumReducer.class);

        // 设置Mapper的输出键值类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置Reducer的输出键值类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置输入和输出格式
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 设置输入和输出路径
        // TextInputFormat.addInputPath(job, new Path(args[0]));
        // TextOutputFormat.setOutputPath(job, new Path(args[1]));
        FileInputFormat.addInputPath(job, new Path("input_sum.txt")); // 读取的是项目根路径下的文件
        FileOutputFormat.setOutputPath(job, new Path("output_sum")); // 输出的是项目根路径下目录

        // 提交作业并等待完成
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


    // 创建一个SumMapper类来定义Map函数
    public static class SumMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private final Text word = new Text("sum");

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            int num = Integer.parseInt(value.toString());
            context.write(word, new IntWritable(num));
        }
    }

    // 创建一个SumReducer类来定义Reduce函数
    public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
}
