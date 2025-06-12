package com.zja.nfs.nfs;

import java.io.InputStream;
import java.util.List;

/**
 * NFS客户端接口
 *
 * @Author: zhengja
 * @Date: 2025-06-12 13:41
 */
public interface NfsClient {

    // 桶相关操作

    /**
     * 创建桶
     *
     * @param bucket 桶名称
     */
    String createBucket(String bucket) throws NfsException;

    /**
     * 删除桶（必须为空）
     */
    boolean deleteBucket(String bucket) throws NfsException;

    /**
     * 桶是否存在
     */
    boolean existsBucket(String bucket) throws NfsException;

    /**
     * 桶是否为空
     */
    boolean isBucketEmpty(String bucket) throws NfsException;

    /**
     * 获取所有桶列表
     */
    List<String> listBuckets() throws NfsException;

    /**
     * 重命名桶
     */
    boolean renameBucket(String oldName, String newName) throws NfsException;


    // 文件相关操作

    /**
     * 存储文件到指定的桶（对应目录）
     *
     * @param bucket      桶名称
     * @param fileName    文件名
     * @param inputStream 文件输入流
     */
    void upload(String bucket, String fileName, InputStream inputStream) throws NfsException;

    /**
     * 从指定的桶中获取文件的输入流
     */
    InputStream downloadStream(String bucket, String fileName) throws NfsException;

    /**
     * 将桶中的文件下载到本地路径
     *
     * @param bucket    桶名称
     * @param fileName  文件名
     * @param localPath 本地保存路径（含文件名）
     */
    void downloadToFile(String bucket, String fileName, String localPath) throws NfsException;

    /**
     * 检查指定桶中是否存在指定文件
     *
     * @param bucket   桶名称
     * @param fileName 文件名
     * @return 如果存在返回 true，否则 false
     */
    boolean existsFile(String bucket, String fileName) throws NfsException;

    /**
     * 删除指定桶中的文件
     */
    boolean delete(String bucket, String fileName) throws NfsException;

    /**
     * 列出指定桶下的所有文件（含目录和文件）
     */
    List<String> listFilesInBucket(String bucket) throws NfsException;

    /**
     * 列出指定桶下的所有子桶（仅目录）
     */
    List<String> listBucketsInBucket(String bucket) throws NfsException;

    /**
     * 列出指定桶下的所有文件（仅文件，不含目录）
     */
    List<String> listFilesInBucketOnly(String bucket) throws NfsException;

    /**
     * 列出指定桶下的所有文件和子目录信息（含目录和文件）
     */
    List<FileInfo> listFilesWithInfo(String bucket) throws NfsException;
}