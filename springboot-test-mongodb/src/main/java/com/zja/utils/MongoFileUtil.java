package com.zja.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-22 10:20
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Mongo 操作文件工具类
 */
@Slf4j
public class MongoFileUtil {

    private GridFsTemplate gridFsTemplate;

    private GridFSBucket gridFSBucket;

    /**
     * 预览前缀：例 http://127.0.0.1:8080/dgp-web/public/file/
     */
    private String baseURL;

    /**
     * 本地存储绝对路径：例 D：\\Temp\\mg
     */
    private String localFileMongoPath;

    /**
     * 路径前缀：mg标识存储mongo文件
     */
    private static final String MONGO_PATH_PREFIX = "mg";

    /**
     * 自定义ID key
     */
    private static final String fileKey = "path";


    //----------------------------------------------

    /**
     * 注入bean
     * @param gridFsTemplate
     * @param gridFSBucket
     * @param localFilePath  本地存储路径    例: C:\\Temp\\storage
     * @param baseURL        基础URL访问路径 例：http://127.0.0.1:80/public/file
     */
    public MongoFileUtil(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket, String localFilePath, String baseURL) {
        checkBeanParams(localFilePath, baseURL);
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
        this.localFileMongoPath = localFilePath + File.separator + MONGO_PATH_PREFIX;
        this.baseURL = baseURL + "/" + MONGO_PATH_PREFIX;
    }


    /**
     * 从本地上传文件到 mongo
     * @param path 自定义ID {@link com.zja.utils.id.IdUtil#mgdbId()}
     * @param file 文件对象
     */
    public void uploadByFile(String path, File file) {
        uploadByFile(path, null, file);
    }

    /**
     * 从本地上传文件到 mongo ：支持自定义文件名称
     * @param path  自定义ID {@link com.zja.utils.id.IdUtil#mgdbId()}
     * @param filename 新的文件名称，mongo中存储的文件名
     * @param file
     */
    public void uploadByFile(String path, String filename, File file) {
        checkPath(path);
        if (!file.exists()) {
            log.error("fileAbsolutePath: {}", file.getName(), file.getAbsolutePath());
            throw new RuntimeException("上传的文件file不存在！");
        }
        if (!file.isFile()) {
            log.error("fileAbsolutePath: {}", file.getName(), file.getAbsolutePath());
            throw new RuntimeException("上传的文件file不是文件类型！");
        }
        /*if (file.length() <= 0) {
            log.error("fileAbsolutePath: {}", file.getName(), file.getAbsolutePath());
            throw new RuntimeException("上传的文件file大小必须大于0！");
        }*/
        if (null == filename) {
            filename = file.getName();
        }

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            uploadByStream(path, filename, inputStream);
        } catch (FileNotFoundException e) {
            log.error("从本地文件上传到mongo失败,文件绝对路径：{}", file.getAbsolutePath());
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("文件流关闭失败 {}", file.getAbsoluteFile());
                e.printStackTrace();
            }
        }
    }

    /**
     * 存储文件
     * @param inputStream
     * @param path 自定义ID {@link com.zja.utils.id.IdUtil#mgdbId()}
     * @param fileName 文件名称(带后缀)
     * @return 上传失败，则抛异常
     */
    public void uploadByStream(String path, String fileName, InputStream inputStream) {
        checkPath(path);
        if (null == fileName) {
            throw new RuntimeException("上传的文件fileName不能为空！");
        }

        DBObject metadata = new BasicDBObject(fileKey, path);
        //mongoFileId 返回文件ID 注意，返回的不是自定义path
        String mongoFileId = gridFsTemplate.store(inputStream, fileName, metadata).toString();

        if (mongoFileId == null) {
            throw new RuntimeException("文件上传失败！");
        }
    }

    /**
     * 获取文件预览url地址
     * @param path
     * @return 返回文件URL网址
     */
    public String getFileURL(String path) {
        return getFileURL(path, null);
    }

    /**
     * 获取文件预览url地址，并且重命名
     * @param path
     * @param newFileName 预览文件的新名称
     * @return 返回文件URL网址
     */
    public String getFileURL(String path, String newFileName) {
        String fileName = null;
        GridFSFile fileInfo = getGridFSFile(path);

        if (null == fileInfo) {
            log.error("根据path=[{}]在mongo中没有查询到file文件！", path);
            throw new RuntimeException("在mongo中没有查询到存在的文件！");
        }

        if (null == baseURL) {
            log.error("previewURL 必须配置，参考：MongoFileUtil(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket, String localFilePath, String previewPrefix) ,previewPrefix not is null！");
            throw new RuntimeException("previewURL is null,config MongoFileUtil(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket, String localFilePath, String previewPrefix)！");
        }

        fileName = fileInfo.getFilename();

        if (null != newFileName) {
            fileName = newFileName;
        }

        if (null == fileName) {
            fileName = path;
        }

        //只保留后6位，避免名称相同的文件会覆盖
        String uniqueness = "";
        if (path.length() > 6) {
            uniqueness = path.substring(path.length() - 6);
        } else {
            uniqueness = path;
        }

        File file = new File(localFileMongoPath + File.separator + uniqueness + File.separator + fileName);
        if (!file.exists()) {
            downloadFileByPath(path, file.getAbsolutePath());
        }

        String fileUrl = baseURL + "/" + uniqueness + "/" + fileName;
        return fileUrl;
    }

    /**
     * 从mongo中下载文件到本地
     * @param path
     * 注意：需要配置默认地址：MongoFileUtil.localFileMongoPath
     * @return 返回文件保存路径
     */
    public String downloadFile(String path) {
        checkPath(path);
        if (null == localFileMongoPath) {
            log.error("需要配置默认路径: localFileMongoPath , 文件path=[{}]下载失败！", path);
            throw new RuntimeException("需要配置默认路径：localFileMongoPath");
        }
        String fileName = getFileName(path);
        if (null == fileName) {
            fileName = path;
        }

        String filePath = localFileMongoPath + File.separator + fileName;
        downloadFileByPath(path, filePath);
        return filePath;
    }

    /**
     * 从mongo中下载文件到本地 并且文件重命名
     * @param path
     * @param newFileName 下载本地后的文件新名称,例 test.txt
     * @return 返回文件保存路径
     */
    public String downloadFile(String path, String newFileName) {
        if (null == localFileMongoPath) {
            log.error("需要配置默认路径: localFileMongoPath , 文件path=[{}]下载失败！", path);
            throw new RuntimeException("需要配置默认路径：localFileMongoPath");
        }

        String filePath = localFileMongoPath + File.separator + newFileName;
        downloadFileByPath(path, filePath);
        return filePath;
    }

    /**
     * 从mongo中下载文件到本地服务器上
     * @param localFilePath 本地服务器绝对路径,例 C:\\Temp\\test.txt
     * @param path
     */
    public void downloadFileByPath(String path, String localFilePath) {
        checkPath(path);
        if (new File(localFilePath).exists()) {
            new File(localFilePath).delete();
        }

        File file = new File(localFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = getInputStream(path);
            IOUtils.copy(inputStream, fileOutputStream);
            log.debug("从mongo下载文件成功，localFilePath: {}", localFilePath);
        } catch (IOException e) {
            log.error("从mongo下载文件失败，path：{} ，localFilePath: {}", path, localFilePath);
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("从mongo下载文件失败，path：{}", path);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从mongo中获取文件流
     * @param path
     * @return InputStream 直接返回mongo文件流对象，使用时注意关闭流
     */
    public InputStream getInputStream(String path) throws IOException {
        GridFSFile gridFSFile = getGridFSFile(path);
        GridFsResource gridFsResource = gridFSFileToGridFsResource(gridFSFile);
        return gridFsResource.getInputStream();
    }

    /**
     * 检查mongo中是否存在文件
     * @param path
     * @return true 存在
     */
    public boolean existFileByPath(String path) {
        GridFSFile gridFSFile = getGridFSFile(path);
        if (gridFSFile == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件对象信息
     * @param path
     * @return 文件对象
     */
    public GridFSFile getGridFSFile(String path) {
        checkPath(path);
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData(fileKey);
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        return gridFSFile;
    }

    /**
     * 获取mogon中存储的文件名称
     * @param path
     * @return 返回文件名称
     */
    public String getFileName(String path) {
        GridFSFile result = getGridFSFile(path);
        if (result == null) {
            log.error("根据path=[{}]在mongo中没有查询到file文件！", path);
            throw new RuntimeException("在mongo中没有查询到存在的文件！");
        }
        //确认文件上传时，是否设置文件名称
        if (null == result.getFilename()) {
            log.error("根据path=[{}]在mongo中没有查询到file文件名称！", path);
            return null;
        }

        return result.getFilename();
    }

    /**
     * 删除mongo中存储的文件
     * @param path
     * @return 返回删除结果 true 成功
     */
    public boolean deleteFile(String path) {
        checkPath(path);
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData(fileKey);
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);
        try {
            gridFsTemplate.delete(query);
            log.debug("从mongo中删除文件成功！");
        } catch (Exception e) {
            log.error("从mongo中删除文件失败，path：{} ，ExceptionMessage：{}", path, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 转换器
     * @param gridFsFile
     * @return GridFsResource
     */
    private GridFsResource gridFSFileToGridFsResource(GridFSFile gridFsFile) {
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());
        return new GridFsResource(gridFsFile, gridFSDownloadStream);
    }

    /**
     * 校验pdth
     * @param path
     */
    private void checkPath(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("[path] not is null!");
        }
    }

    /**
     * 校验bean 注入的参数
     * @param localFilePath
     * @param baseURL
     */
    private void checkBeanParams(String localFilePath, String baseURL) {
        if (StringUtils.isEmpty(localFilePath)) {
            throw new RuntimeException("[localFilePath] not is null！");
        }
        if (StringUtils.isEmpty(baseURL)) {
            throw new RuntimeException("[baseURL] not is null！");
        }
    }
}
