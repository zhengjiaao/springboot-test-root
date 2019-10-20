package com.dist.utils.resourceStorage;

import com.dist.utils.file.FileUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.*;
import java.util.List;

/**
 * mongo下载工具（仅适用与SpringBoot1.5）
 *
 * @author yujx
 * @date 2019/03/23 15:04
 */
public class MongoResourceStorage implements IResourceStorage {

    private static final Logger log = LoggerFactory.getLogger(MongoResourceStorage.class);

    private GridFsTemplate gridFsTemplate;

//    /**
//     * 上传文件到文件服务
//     * 提供了默认的实现，那么除mongodb以外的类就不用实现该方法
//     *
//     * @param localFilePath 本地文件路径
//     * @param path          服务上存储的标识（在这里是没用的，为了和ftp版实现同一接口）
//     * @return 服务上存储的标识
//     */
//    @Override
//    public String uploadFile(String localFilePath, String path) {
//        File file = new File(localFilePath);
//
//        if (file.exists() && file.length() > 0) {
//            Resource fileSystemResource = new FileSystemResource(localFilePath);
//            try {
//                GridFSFile gridFSFile = gridFsTemplate.store(fileSystemResource.getInputStream(), fileSystemResource.getFilename());
//                log.info("文件{}上传成功", localFilePath);
//                return gridFSFile.getId().toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        log.error("文件{}上传失败", localFilePath);
//        throw new RuntimeException("传入的文件路径在本机不存在");
//    }
//
//
//    /**
//     * 从文件服务上获取文件流
//     *
//     * @param path 服务上存储的标识
//     * @return 文件流
//     */
//    @Override
//    public InputStream downloadFileStream(String path) {
//
//        List<GridFSDBFile> result = gridFsTemplate.find(Query.query(GridFsCriteria.where("_id").is(path)));
//
//        if (result != null && result.size() > 0) {
//            GridFSDBFile gridFSDBFile = result.get(0);
//
//            InputStream inputStream = null;
//            try {
//                inputStream = gridFSDBFile.getInputStream();
//
//                log.info("{}的文件流获取成功！", path);
//                return inputStream;
//            } finally {
//                try {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        throw new RuntimeException("根据path查询到的文件为空");
//    }

    /**
     * 上传文件到文件服务
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否下载成功
     */
    @Override
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
    @Override
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
    @Override
    public Boolean uploadFileStream(InputStream inputStream, String path, String fileName) {
        DBObject metadata = new BasicDBObject("docid", path);
//        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, metadata);
        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, fileName, metadata);
        if (gridFSFile.getId().toString() != null) {
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
    @Override
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
    @Override
    public InputStream downloadFileStream(String path) {

        List<GridFSDBFile> result = getGridFSDBFiles(path);

        if (result != null && result.size() > 0) {
            try (InputStream inputStream = result.get(0).getInputStream()) {
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
    @Override
    public String getFileNameByPath(String path) {
        List<GridFSDBFile> result = getGridFSDBFiles(path);

        if (result != null && result.size() > 0) {
            return result.get(0).getFilename();
        }

        throw new RuntimeException("根据path查询到的文件为空");
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    @Override
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
    private List<GridFSDBFile> getGridFSDBFiles(String path) {
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData("docid");
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);

        return gridFsTemplate.find(query);
    }

    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

}
