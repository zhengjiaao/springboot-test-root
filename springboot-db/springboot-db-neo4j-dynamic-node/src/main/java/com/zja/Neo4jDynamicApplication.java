/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-14 15:48
 * @Since:
 */
package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories("com.zja.repositorys")
@SpringBootApplication
public class Neo4jDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(Neo4jDynamicApplication.class, args);
    }

}
