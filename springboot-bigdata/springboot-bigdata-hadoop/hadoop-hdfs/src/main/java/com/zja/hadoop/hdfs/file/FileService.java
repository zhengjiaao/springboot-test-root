/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-22 10:54
 * @Since:
 */
package com.zja.hadoop.hdfs.file;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/02/22 10:54
 */
public interface FileService {

    /**
     * hdfs 文件系统
     */
    FileSystem fileSystem();

    /**
     * 上传对象
     *
     * @param objectName 对象名称，若相同，则会被覆盖，例：a.jpg or b.jpg
     * @param file
     */
    void uploadObject(String objectName, MultipartFile file) throws Exception;

    /**
     * 上传对象
     *
     * @param objectName 对象名称，若相同，则会被覆盖，例：a.jpg or b.jpg
     * @param filename   本地文件，例：D:\temp\a.jpg
     */
    void uploadObject(String objectName, String filename) throws Exception;

    /**
     * 异步上传对象
     *
     * @param objectName 对象名称，若相同，则会被覆盖，例：a.jpg or b.jpg
     * @param filename   本地文件，例：D:\temp\a.jpg
     * @return objectName
     */
    String uploadObjectAsync(String objectName, String filename);

    /**
     * 下载对象
     *
     * @param objectName 对象名称
     * @param filename   本地文件，例：D:\temp\a.jpg
     */
    void downloadObject(String objectName, String filename) throws Exception;

    /**
     * 获取对象(对象所有信息)
     *
     * @param objectName 对象名称
     * @return InputStream
     */
    InputStream getObjectInputStream(String objectName) throws Exception;

    /**
     * 校验是否存在对象
     *
     * @param objectName 对象名称
     * @return boolean
     */
    boolean existObject(String objectName) throws Exception;

    /**
     * 删除对象
     *
     * @param objectName 对象名称
     */
    void deleteObject(String objectName) throws Exception;
}
