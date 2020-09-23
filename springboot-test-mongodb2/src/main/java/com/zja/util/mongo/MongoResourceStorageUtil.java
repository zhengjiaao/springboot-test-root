package com.zja.util.mongo;

import com.zja.util.file.FileUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;

/**文件上传 / 文件下载  mongo工具类
 * mongo下载工具（仅适用与SpringBoot2.x，不适用与1.5x）
 */
@Component
public class MongoResourceStorageUtil{

    private static final Logger log = LoggerFactory.getLogger(MongoResourceStorageUtil.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    @Qualifier(value = "firstGridFSBucket")
    private GridFSBucket gridFSBucket;

    /**
     * 上传文件到文件服务
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否下载成功
     */
    public Boolean uploadFile(String localFilePath, String path) {

        File file = new File(localFilePath);

        if (file.exists() && file.length() > 0) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                return this.uploadFileStream(inputStream, path, FileUtil.getFileName(localFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.error("文件{}上传失败", localFilePath);
        throw new RuntimeException("传入的文件路径在本机不存在或文件大小不合法！");
    }

    /**
     * 上传文件流到文件服务
     *
     * @param inputStream 文件输入流
     * @param path        服务上存储的标识
     * @return 是否上传成功
     */
    public Boolean uploadFileStream(InputStream inputStream, String path) {
        return this.uploadFileStream(inputStream, path, null);
    }

    /**
     * 上传文件流到文件服务
     *
     * @param inputStream 文件输入流
     * @param path        服务上存储的标识
     * @param fileName    文件名【包括后缀】
     * @return 是否上传成功
     */
    public Boolean uploadFileStream(InputStream inputStream, String path, String fileName) {
        DBObject metadata = new BasicDBObject("docid", path);
        ObjectId store = gridFsTemplate.store(inputStream, fileName, metadata);
        if (store != null) {
            log.info("文件{}上传成功", path);
            return true;
        }

        throw new RuntimeException("文件上传失败！");
    }

    /**
     * 从文件服务下载文件
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否下载成功
     */
    public Boolean downloadFile(String localFilePath, String path) {

        if (new File(localFilePath).exists()) {
            new File(localFilePath).delete();
        }

        InputStream inputStream = this.downloadFileStream(path);

        File file = new File(localFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 保存文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, fileOutputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fileOutputStream);
            log.info("文件{}下载成功，本地文件路径为{}", path, localFilePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件{}下载失败", localFilePath);

            return false;
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件服务上获取文件流
     *
     * @param path 服务上存储的标识
     * @return 文件流
     */
    public InputStream downloadFileStream(String path) {

        GridFSFile result = getGridFSDBFiles(path);
        GridFsResource gridFsResource = convertGridFSFile2Resource(result);
        if (gridFsResource != null) {
            InputStream inputStream =null;
            try {
                inputStream = gridFsResource.getInputStream();
                log.info("{}的文件流获取成功！", path);
                return inputStream;
            } catch (IOException e) {
                log.error("获取流失败,{},{}", path, e);
            }
        }

        throw new RuntimeException("根据path查询到的文件为空");
    }

    /**
     * 通过path获取文件名
     *
     * @param path 服务上存储的标识
     * @return 文件名【包括后缀】
     */
    public String getFileNameByPath(String path) {
        GridFSFile result = getGridFSDBFiles(path);
        if (result != null) {
            return result.getFilename();
        }

        throw new RuntimeException("根据path查询到的文件为空");
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean deleteFile(String path) {
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData("docid");
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);
        try {
            gridFsTemplate.delete(query);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过条件获取mongo中的文件
     *
     * @param path
     * @return
     */
    private GridFSFile getGridFSDBFiles(String path) {
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData("docid");
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);

        return gridFsTemplate.findOne(query);
    }


    /**
     *
     * @param gridFsFile
     * @return
     */
    public GridFsResource convertGridFSFile2Resource(GridFSFile gridFsFile) {
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());
        return new GridFsResource(gridFsFile, gridFSDownloadStream);
    }
}
