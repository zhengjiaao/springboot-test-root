package com.zja.config.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * 如果需要使用SpringDataMongo的Repositories则需要将注释打开
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb.first")
public class FirstMongoConfigure extends MongoProperties {

    @Override
    @Primary
    @Bean(name = "firstMongoDbFactory")
    public MongoDbFactory mongoDbFactory() {
        // 有认证的初始化方法
        /*ServerAddress serverAddress = new ServerAddress(super.getHost(), super.getPort());
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(MongoCredential.createScramSha1Credential(super.getUsername(), super.getAuthenticationDatabase(),super.getPassword().toCharArray()));
        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList), super.getDatabase());*/
        // 无认证的初始化方法
        return new SimpleMongoDbFactory(new MongoClient(super.getHost(), super.getPort()), super.getDatabase());
    }

    @Override
    @Primary
    @Bean(name = "firstMongoTemplate")
    public MongoTemplate mongoTemplate(@Qualifier("firstMongoDbFactory") MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Override
    @Primary
    @Bean(name = "firstGridFsTemplate")
    public GridFsTemplate gridFsTemplate(@Qualifier("firstMongoDbFactory") MongoDbFactory mongoDbFactory,
                                         @Qualifier("firstMongoTemplate") MongoTemplate mongoTemplate) {
        return new GridFsTemplate(new GridFsMongoDbFactory(mongoDbFactory,super.getDatabase()), mongoTemplate.getConverter());
    }

    @Primary
    @Bean(name = "firstGridFSBucket")
    public GridFSBucket getGridFSBucket(@Qualifier("firstMongoDbFactory") MongoDbFactory mongoDbFactory) {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db);
    }
}