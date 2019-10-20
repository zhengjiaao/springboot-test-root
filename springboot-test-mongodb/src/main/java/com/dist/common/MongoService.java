package com.dist.common;

import com.dist.utils.ObjectUtil;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储mongo服务
 * @date 2019/6/25
 */
@Slf4j
@Component
public class MongoService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 存储文件并返回存储路径
     * @param fileName
     * @param suffix
     * @param file
     * @return
     */
    public String save(String fileName,String suffix, byte[] file) {
        InputStream inputStream = null;
        String path = "";
        try {
            inputStream = new ByteArrayInputStream(file);
            path = gridFsTemplate.store(inputStream, fileName, suffix).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return path;
    }

    /**
     * 删除指定path下的文件
     * @param path
     * @return
     */
    public boolean delete(String path) {
        if (ObjectUtil.isNull(path)) {
            return false;
        }
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(path)));
        return true;
    }

    /**
     * 获取指定path下的文件二进制形式
     * 通过gridFSBucket方式读取文件
     * @param filePath
     * @return
     */
    public byte[] getFileByPath(String filePath) {
        GridFSDownloadStream downloadStream = null;
        try {
            MongoDatabase mongoDatabase = mongoTemplate.getDb();
            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
            downloadStream = gridFSBucket.openDownloadStream(new ObjectId(filePath));
            GridFSFile gridFSFile = downloadStream.getGridFSFile();

            //如果一次性读取所有字节，大于chunk size的可能会出现乱序，导致文件损坏
            int size = gridFSFile.getChunkSize();
            int len = (int)gridFSFile.getLength();
            int readSize = Math.min(len, size);
            byte[] returnBytes = new byte[len];
            int offset = 0;
            while (len > 0) {
                int tmp;
                if (len > readSize) {
                    tmp = downloadStream.read(returnBytes, offset, readSize);
                    offset += tmp;
                } else {
                    tmp = downloadStream.read(returnBytes, offset, len);
                }
                len -= tmp;
            }
            return returnBytes;
        } catch (Exception e) {
            log.error("获取指定path下的文件二进制形式失败：入参filePath{}",filePath);
        } finally {
            if (null != downloadStream) {
                downloadStream.close();
            }
        }
        return new byte[0];
    }


}
