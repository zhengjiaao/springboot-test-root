package com.zja.config.mongo;

import com.mongodb.ClientSessionOptions;
import com.mongodb.DB;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.Assert;

/**
 * GridFs工厂类
 *
 */
public class GridFsMongoDbFactory implements MongoDbFactory {

    private final MongoDbFactory mongoDbFactory;
    private final String gridFsDatabase;

    GridFsMongoDbFactory(MongoDbFactory mongoDbFactory, String gridFsDatabase) {
        Assert.notNull(mongoDbFactory, "MongoDbFactory must not be null");
        Assert.notNull(gridFsDatabase, "GridFsDatabase must not be null");
        this.mongoDbFactory = mongoDbFactory;
        this.gridFsDatabase = gridFsDatabase;
    }

    @Override
    public MongoDatabase getDb() throws DataAccessException {
        Assert.notNull(gridFsDatabase, "GridFsDatabase must not be null");
        return this.mongoDbFactory.getDb(gridFsDatabase);
    }

    @Override
    public MongoDatabase getDb(String s) throws DataAccessException {
        return this.mongoDbFactory.getDb(s);
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return this.mongoDbFactory.getExceptionTranslator();
    }

    @Override
    public DB getLegacyDb() {
        Assert.notNull(gridFsDatabase, "GridFsDatabase must not be null");
        return (DB) this.mongoDbFactory.getDb(gridFsDatabase);
    }

    @Override
    public ClientSession getSession(ClientSessionOptions clientSessionOptions) {
        return null;
    }

    @Override
    public MongoDbFactory withSession(ClientSession clientSession) {
        return null;
    }
}
