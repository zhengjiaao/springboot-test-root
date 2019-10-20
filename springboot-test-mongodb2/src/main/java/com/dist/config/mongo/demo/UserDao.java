package com.dist.config.mongo.demo;

import com.dist.dto.UserDTO;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yujx
 * @date 2019/02/16 10:08
 */
@Component
public class UserDao {

    @Autowired
    @Qualifier(value = "firstMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    private MongoTemplate mongoTemplate2;


    /**
     * 保存文档
     */
    public void saveUser(){
        UserDTO user = new UserDTO();
        user.setId("1");
        user.setUsername("数据源1");

        mongoTemplate.save(user);
    }

    public void saveUser2(){
        UserDTO user = new UserDTO();
        user.setId("2");
        user.setUsername("数据源2");

        mongoTemplate2.save(user);
    }


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    @Qualifier(value = "secondGridFsTemplate")
    private GridFsTemplate gridFsTemplate2;


    /**
     * 上传文件
     * @throws Exception
     */
    public void uploadFile() throws Exception {
        gridFsTemplate.store(new FileInputStream(new File("/Users/x5456/Desktop/数据源1")),"数据源1");
    }

    public void uploadFile2() throws Exception {
        gridFsTemplate2.store(new FileInputStream(new File("/Users/x5456/Desktop/数据源2")),"数据源2");
    }

    /**
     * 下载文件
     * @throws IOException
     */
    public void downloadFile() throws IOException {
        // TODO: 2019/2/16 真实使用的时候需要判断集合的大小
        GridFSFindIterable result = gridFsTemplate.find(Query.query(GridFsCriteria.whereFilename().is("数据源1")));
        GridFSFile gridFSFile = result.first();

        System.out.println("getFilename="+gridFSFile.getFilename());
        //gridFSDBFile.writeTo(new File("/Users/x5456/Downloads/数据源1"));
    }

    public void downloadFile2() throws IOException {
        GridFSFindIterable result = gridFsTemplate2.find(Query.query(GridFsCriteria.whereFilename().is("数据源2")));
        GridFSFile gridFSDBFile = result.first();
        System.out.println("gridFSDBFile="+gridFSDBFile.getFilename());
    }
}
