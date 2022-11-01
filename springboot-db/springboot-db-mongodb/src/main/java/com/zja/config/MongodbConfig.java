package com.zja.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.zja.utils.MongoFileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.Resource;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-22 17:45
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Mongodb 配置类
 */
@Configuration
public class MongodbConfig {

    @Resource
    private MongoDbFactory mongoDbFactory;

    @Bean
    public GridFSBucket getGridFSBuckets() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db);
    }

    @Bean
    public MongoFileUtil mongoFileUtil(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket) {
        return new MongoFileUtil(gridFsTemplate, gridFSBucket, ConfigConstants.file.localStoragePath, ConfigConstants.file.proxyBaseURL());
    }
}
