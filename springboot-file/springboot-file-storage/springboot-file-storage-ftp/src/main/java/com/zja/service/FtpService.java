package com.zja.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;

/**
 * @Author: zhengja
 * @Date: 2025-09-08 20:06
 */
public interface FtpService {

    /**
     * 上传文件
     * @param serverKey 服务器标识 (如 "vsftpd", "windows")
     * @param remotePath 远程文件路径 (包含文件名)
     * @param file 本地文件对象
     * @return 上传是否成功
     */
    boolean uploadFile(String serverKey, String remotePath, File file);

    /**
     * 上传 MultipartFile (通常用于 Web 上传)
     * @param serverKey 服务器标识
     * @param remotePath 远程文件路径 (包含文件名)
     * @param multipartFile Spring MultipartFile 对象
     * @return 上传是否成功
     */
    boolean uploadFile(String serverKey, String remotePath, MultipartFile multipartFile);

    /**
     * 下载文件到本地文件
     * @param serverKey 服务器标识
     * @param remotePath 远程文件路径 (包含文件名)
     * @param localFile 本地目标文件
     * @return 下载是否成功
     */
    boolean downloadFile(String serverKey, String remotePath, File localFile);

    /**
     * 下载文件到 OutputStream
     * @param serverKey 服务器标识
     * @param remotePath 远程文件路径 (包含文件名)
     * @param outputStream 输出流
     * @return 下载是否成功
     */
    boolean downloadFile(String serverKey, String remotePath, OutputStream outputStream);

    /**
     * 删除远程文件
     * @param serverKey 服务器标识
     * @param remotePath 远程文件路径 (包含文件名)
     * @return 删除是否成功
     */
    boolean deleteFile(String serverKey, String remotePath);

    /**
     * 检查远程文件是否存在
     * @param serverKey 服务器标识
     * @param remotePath 远程文件路径 (包含文件名)
     * @return 是否存在
     */
    boolean fileExists(String serverKey, String remotePath);
}