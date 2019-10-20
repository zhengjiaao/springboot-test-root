package com.dist.utils.resourceStorage;

import java.io.InputStream;

/**
 * 从服务（mongo/ftp）获取文件资源接口
 *
 * @author yujx
 * @date 2019/03/23 14:36
 */
public interface IResourceStorage {

    /**
     * 上传文件到文件服务
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否上传成功
     */
    Boolean uploadFile(String localFilePath, String path);


    /**
     * 上传文件流到文件服务
     *
     * @param inputStream 文件输入流
     * @param path        服务上存储的标识
     * @return 是否上传成功
     */
    Boolean uploadFileStream(InputStream inputStream, String path);


    /**
     * 上传文件流到文件服务
     *
     * @param inputStream 文件输入流
     * @param path        服务上存储的标识
     * @param fileName    文件名【包括后缀】
     * @return 是否上传成功
     */
    Boolean uploadFileStream(InputStream inputStream, String path, String fileName);

    /**
     * 从文件服务下载文件
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否下载成功
     */
    Boolean downloadFile(String localFilePath, String path);


    /**
     * 从文件服务上获取文件流
     *
     * @param path 服务上存储的标识
     * @return 文件流
     */
    InputStream downloadFileStream(String path);


    /**
     * 通过path获取文件名
     *
     * @param path 服务上存储的标识
     * @return 文件名【包括后缀】
     */
    String getFileNameByPath(String path);


    /**
     * 从文件服务的一个位置copy到另一个位置【本方法只适合源path和目标path在同一个数据库中】
     *
     * @param srcPath    源文件的标识符
     * @param targetPath 目标文件的标识符
     * @return 是否copy成功
     */
    default Boolean copyFile(String srcPath, String targetPath) {
        return this.uploadFileStream(this.downloadFileStream(srcPath), targetPath);
    }


    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    boolean deleteFile(String path);
}
