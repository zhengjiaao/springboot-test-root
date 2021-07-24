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

/**Mongodb 配置类
 * @author zhengja@dist.com.cn
 * @data 2019/6/19 17:26
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
