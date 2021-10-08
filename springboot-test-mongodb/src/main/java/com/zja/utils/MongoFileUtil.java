package com.zja.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.zja.utils.id.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-22 10:20
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Mongo 操作文件工具类
 * @Since: mongo 2.x
 */
@Slf4j
public class MongoFileUtil {

    private final GridFsTemplate gridFsTemplate;

    private final GridFSBucket gridFSBucket;

    /**
     * 预览前缀：例 http://127.0.0.1:8080/dgp-web/public/file/
     */
    private final String baseURL;

    /**
     * 本地存储绝对路径：例 D：\\Temp\\mg
     */
    private final String localFileMongoPath;

    /**
     * 路径前缀：mg标识存储mongo文件
     */
    private static final String PATH_PREFIX = "mg";

    /**
     * 自定义ID key
     */
    private static final String fileKey = "path";


    //----------------------------------------------bean 需要注入才能使用，此类的所有方法

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
        this.localFileMongoPath = localFilePath + File.separator + PATH_PREFIX;
        this.baseURL = baseURL + "/" + PATH_PREFIX;
        initLocalFileMongoPath();
    }


    //---------------------------------------------所有操作方法

    /**
     * 从本地上传文件到 mongo
     * @param file 文件对象
     */
    public String uploadByFile(File file) {
        String mgdbId = IdUtil.mgdbId();
        uploadByFile(mgdbId, file);
        return mgdbId;
    }

    /**
     * 从本地上传文件到 mongo
     * @param path 自定义ID {@link com.zja.utils.id.IdUtil#mgdbId()}
     * @param file 文件对象
     */
    public void uploadByFile(String path, File file) {
        //注意 path 不能重复，多个相同path只取第一个元素(文件)
        uploadByFile(path, null, file);
    }

    /**
     * 从本地上传文件到 mongo ：支持自定义文件名称
     * @param path  自定义ID {@link com.zja.utils.id.IdUtil#mgdbId()}
     * @param newFileName 新的文件名称，mongo中存储的文件名
     * @param file
     */
    public void uploadByFile(String path, String newFileName, File file) {
        if (!file.exists()) {
            log.error("fileAbsolutePath: {}", file.getAbsolutePath());
            throw new RuntimeException("上传的文件file不存在！");
        }
        if (!file.isFile()) {
            log.error("fileAbsolutePath: {}", file.getAbsolutePath());
            throw new RuntimeException("上传的文件file不是文件类型！");
        }
        if (null == newFileName) {
            newFileName = file.getName();
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            uploadByStream(path, newFileName, inputStream);
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

        GridFSFile fileInfo = getGridFSFile(path);
        if (null == fileInfo) {
            log.error("根据path=[{}]在mongo中没有查询到file文件！", path);
            throw new RuntimeException("在mongo中没有查询到存在的文件！");
        }

        String fileName = fileInfo.getFilename();
        if (null != newFileName) {
            fileName = newFileName;
        }
        if (null == fileName) {
            log.error("未发现文件名称,若无法正常打开文件,请尝试调用 downloadFile(String path, String newFileName) or downloadFile(String path, String localPath, String fileName) 方法.");
            fileName = path;
        }

        File file = new File(localCacheUniquePath(path) + File.separator + fileName);
        if (!file.exists()) {
            downloadFile(path, localCacheUniquePath(path), fileName);
        }

        String fileUrl = baseURL + "/" + localUniqueId(path) + "/" + fileName;
        return fileUrl;
    }

    /**
     * 获取文件本地缓存路径
     * @param path
     * @return 返回本地缓存路径
     */
    public String getFileLocalCachePath(String path) {
        GridFSFile fileInfo = getGridFSFile(path);
        if (null == fileInfo) {
            log.error("根据path=[{}]在mongo中没有查询到file文件！", path);
            throw new RuntimeException("在mongo中没有查询到存在的文件！");
        }

        String fileName = fileInfo.getFilename();
        if (null == fileName) {
            log.error("未发现文件名称,若无法正常打开文件,请尝试调用 downloadFile(String path, String newFileName) or downloadFile(String path, String localPath, String fileName) 方法.");
            fileName = path;
        }

        File file = new File(localCacheUniquePath(path) + File.separator + fileName);
        if (!file.exists()) {
            return downloadFile(path, localCacheUniquePath(path), fileName);
        }
        return file.getAbsolutePath();
    }

    /**
     * 从mongo中下载文件到本地
     * @param path
     * 注意：需要配置默认地址：MongoFileUtil.localFileMongoPath
     * @return 返回文件保存路径
     */
    public String downloadFile(String path) {
        String fileName = getFileName(path);
        if (null == fileName) {
            log.error("未发现文件名称,若无法正常打开文件,请尝试调用 downloadFile(String path, String newFileName) or downloadFile(String path, String localPath, String fileName) 方法.");
            fileName = path;
        }
        return downloadFile(path, localCacheUniquePath(path), fileName);
    }

    /**
     * 从mongo中下载文件到本地 并且文件重命名
     * @param path
     * @param newFileName 下载文件的新名称 例：test.txt
     * @return 返回文件保存路径
     */
    public String downloadFile(String path, String newFileName) {
        return downloadFile(path, localCacheUniquePath(path), newFileName);
    }

    /**
     * 从mongo中下载文件
     * @param path
     * @param localPath 本地路径 例：C:\\Temp
     * @param fileName  文件名称 例：test.txt
     * @return 返回文件保存路径
     */
    public String downloadFile(String path, String localPath, String fileName) {
        checkPath(path);
        String localFilePath = localPath + File.separator + fileName;
        File localfile = new File(localFilePath);
        if (localfile.exists()) {
            localfile.delete();
        }
        if (!localfile.getParentFile().exists()) {
            localfile.getParentFile().mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localfile);
            InputStream inputStream = getInputStream(path);
            IOUtils.copy(inputStream, fileOutputStream);
            log.debug("从mongo下载文件成功，localFilePath: {}", localFilePath);
            return localFilePath;
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
        return null;
    }

    /**
     * 从mongo中获取文件流
     * @param path
     * @return InputStream 直接返回mongo文件流对象，使用时注意关闭流
     */
    public InputStream getInputStream(String path) throws IOException {
        GridFSFile gridFSFile = getGridFSFile(path);
        if (gridFSFile == null) {
            log.error("根据path=[{}]在mongo中没有查询到file文件！", path);
            throw new RuntimeException("在mongo中没有查询到存在的文件！");
        }
        GridFsResource gridFsResource = gridFSFileToGridFsResource(gridFSFile);
        return gridFsResource.getInputStream();
    }

    /**
     * 检查mongo中是否存在文件
     * @param path
     * @return true 存在
     */
    public boolean existFile(String path) {
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
        //注意：path是唯一的，如果存在多个path，只取第一个文件
        return gridFsTemplate.findOne(query);
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
        if (null == result.getFilename()) {
            log.error("根据path=[{}]在mongo中没有查询到file文件名称！", path);
            return null;
        }
        return result.getFilename();
    }

    /**
     * 删除文件(同时删除本地缓存文件与mongo文件)
     * @param path
     * @return 返回删除结果 true 成功
     */
    public boolean deleteFile(String path) {
        checkPath(path);
        //1、清理本地缓存文件
        cleanLocalCache(path);
        //2、删除mongo中的文件
        GridFsCriteria gridFsCriteria = GridFsCriteria.whereMetaData(fileKey);
        gridFsCriteria.is(path);
        Query query = new Query();
        query.addCriteria(gridFsCriteria);
        try {
            gridFsTemplate.delete(query);
            log.debug("从mongo中删除文件成功！");
            return true;
        } catch (Exception e) {
            log.error("从mongo中删除文件失败，path：{} ，ExceptionMessage：{}", path, e.getMessage());
        }
        return false;
    }

    /**
     * 清理本地缓存文件, 不会删除mongo存储的文件
     * @param path
     * @return true 清理成功
     */
    public boolean cleanLocalCache(String path) {
        try {
            return deleteLocalDir(localCacheUniquePath(path));
        } catch (Exception e) {
            log.error("本地缓存清理失败:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 清理本地所有缓存, 不会删除mongo存储的文件
     * 删除 localFileMongoPath 目录下的所有资源 例 C:\\Temp\\mg
     * @return true 清理成功
     */
    public boolean cleanLocalAllCache() {
        try {
            return deleteLocalDir(localFileMongoPath);
        } catch (Exception e) {
            log.error("清理所有本地缓存失败:{}", e.getMessage());
        }
        return false;
    }


    //------------------------------------------- 内部方法

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
     * 本地缓存唯一路径
     * @param path
     * @return 单个文件的本地缓存唯一路径
     */
    private String localCacheUniquePath(String path) {
        String localUniqueId = localUniqueId(path);
        return localFileMongoPath + File.separator + localUniqueId;
    }

    /**
     * 本地缓存存储文件的唯一ID
     * @param path
     * @return 返回 本地缓存存储文件的唯一ID
     */
    private static String localUniqueId(String path) {
        //只保留后6位，避免名称相同的文件会覆盖
        String uniqueId = "";
        if (path.length() > 6) {
            uniqueId = path.substring(path.length() - 6);
        } else {
            uniqueId = path;
        }
        return uniqueId;
    }

    /**
     * 初始化 mongo本地缓存路径
     */
    private void initLocalFileMongoPath() {
        if (!new File(localFileMongoPath).exists()) {
            try {
                Files.createDirectories(Paths.get(localFileMongoPath));
            } catch (IOException e) {
                log.error("初始化mongo本地缓存路径失败：{}", localFileMongoPath);
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除目录（同时级联删除目录下所有资源文件）
     * @param localDirPath 本地目录路径 例：C:\\Temp\\mg
     * @return true 删除成功
     */
    private static boolean deleteLocalDir(String localDirPath) {
        File f = new File(localDirPath);
        if (!f.exists()) {
            return true;
        }
        if (f.isDirectory()) {
            File[] listFiles = f.listFiles();
            for (File fs : listFiles) {
                deleteLocalDir(fs.toString());
            }
        }
        return f.delete();
    }

    /**
     * 校验 pdth
     * @param path
     */
    private static void checkPath(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("[path] not is null!");
        }
    }

    /**
     * 校验bean 注入的参数
     * @param localFilePath
     * @param baseURL
     */
    private static void checkBeanParams(String localFilePath, String baseURL) {
        if (StringUtils.isEmpty(localFilePath)) {
            throw new RuntimeException("[localFilePath] not is null！");
        }
        if (StringUtils.isEmpty(baseURL)) {
            throw new RuntimeException("[baseURL] not is null！");
        }
    }
}
