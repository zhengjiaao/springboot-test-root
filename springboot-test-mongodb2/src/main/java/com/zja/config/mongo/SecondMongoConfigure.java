package com.zja.config.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 如果需要使用SpringDataMongo的Repositories则需要将注释打开
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb.second")
@ConditionalOnExpression("${spring.data.mongodb.second.enabled}")
public class SecondMongoConfigure extends MongoProperties {

    @Bean(name = "secondMongoDbFactory")
    @Override
    public MongoDbFactory mongoDbFactory() {
        // 有认证的初始化方法
        ServerAddress serverAddress = new ServerAddress(super.getHost(), super.getPort());
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(MongoCredential.createScramSha1Credential(super.getUsername(), super.getAuthenticationDatabase(),super.getPassword().toCharArray()));
        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList), super.getDatabase());
        // 无认证的初始化方法
       //return new SimpleMongoDbFactory(new MongoClient(super.getHost(), super.getPort()), super.getDatabase());
    }

    @Bean(name = "secondMongoTemplate")
    @Override
    public MongoTemplate mongoTemplate(@Qualifier("secondMongoDbFactory") MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean(name = "secondGridFsTemplate")
    @Override
    public GridFsTemplate gridFsTemplate(@Qualifier("secondMongoDbFactory") MongoDbFactory mongoDbFactory,
                                         @Qualifier("secondMongoTemplate") MongoTemplate mongoTemplate) {
        return new GridFsTemplate(new GridFsMongoDbFactory(mongoDbFactory, super.getDatabase()), mongoTemplate.getConverter());
    }

    @Bean(name = "secondGridFSBucket")
    public GridFSBucket getGridFSBucket(@Qualifier("secondMongoDbFactory") MongoDbFactory mongoDbFactory) {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db);
    }

}