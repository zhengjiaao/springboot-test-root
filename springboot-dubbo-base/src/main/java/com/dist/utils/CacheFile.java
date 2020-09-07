package com.dist.utils;

import com.mongodb.gridfs.GridFSDBFile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author 韩国瑞
 * @date 2018/9/5 16:40
 * @description dubbo的service和web层之间无法传输流(Stream)，采用非关系数据库来缓存文件信息。
 */
@Data
public class CacheFile {

//TODO:添加定时删除临时数据的任务，可以根据MongoDB中的时间进行删除（还需要考虑数据量）


    private String defaultTempFileName = "TempFile";

    @Autowired
    private GridFsTemplate gridFsTemplate;

    /**
     * 存储文件
     * @param in 数据流
     * @return 文件ID
     */
    public String uploadMongoDB(InputStream in){
        return gridFsTemplate.store(in,defaultTempFileName).getId().toString();
    }

    /**
     * 存储文件
     * @param fileName 文件名称
     * @param in 数据流
     * @return 文件ID
     */
    public String uploadMongoDB(String fileName,InputStream in){
        return gridFsTemplate.store(in,fileName).getId().toString();
    }

    /**
     * 获取文件
     * @param fileId 文件ID
     * @return 文件流
     */
    public InputStream downloadMongoDB(String fileId){
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if(gridFSDBFile == null)
            return null;
        return gridFSDBFile.getInputStream();
    }

    @Data
    public static class NamedFile implements Serializable {
        private String name;
        private transient InputStream in;

        public NamedFile() {
        }

        public NamedFile(String name, InputStream in) {
            this.name = name;
            this.in = in;
        }
    }

    /**
     * 根据FileId获取mongoDB中的文件名称和文件流
     * @param fileId MongoDB中文件唯一标示
     * @return 文件名称和文件流
     */
    public NamedFile downloadMongoDBNamedFile(String fileId){
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if(gridFSDBFile == null)
            return null;
        return new NamedFile(gridFSDBFile.getFilename(),gridFSDBFile.getInputStream());
    }
}
