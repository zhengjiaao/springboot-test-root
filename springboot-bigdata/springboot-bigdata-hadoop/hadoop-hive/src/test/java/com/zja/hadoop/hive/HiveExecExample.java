package com.zja.hadoop.hive;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.processors.CommandProcessorResponse;
import org.apache.hadoop.hive.ql.session.SessionState;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/02/01 16:41
 */
public class HiveExecExample {

    public static void main(String[] args) throws IOException {
        // 创建HiveConf对象
        HiveConf conf = new HiveConf();

        // 设置Hive配置属性
        conf.set("hive.metastore.uris", "thrift://localhost:9083");

        // 创建SessionState对象
        SessionState ss = new SessionState(conf);

        // 设置当前线程的SessionState
        SessionState.start(ss);

        try {
            // 创建Driver对象
            Driver driver = new Driver(conf);

            // 执行Hive查询
            String query = "SELECT * FROM example_table";
            CommandProcessorResponse response = driver.run(query);
            int responseCode = response.getResponseCode();

            // 检查查询结果
            if (responseCode == 0) {
                System.out.println("Query executed successfully.");
                String output = ss.getHiveHistory().getHistFileName();
                System.out.println("Query output: " + output);
            } else {
                System.out.println("Query execution failed.");
                System.out.println("Error message: " + response.getErrorMessage());
            }
        } finally {
            // 关闭SessionState
            ss.close();
        }
    }
}
