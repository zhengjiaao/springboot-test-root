package com.zja.service;

import com.zja.config.FtpServerProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * FTP服务实现类
 *
 * @Author: zhengja
 * @Date: 2025-09-08 20:06
 */
@Service
public class FtpServiceImpl implements FtpService {

    private static final Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

    @Autowired
    private FtpServerProperties ftpServerProperties;

    /**
     * 获取并连接到指定的 FTP 服务器
     *
     * @param serverKey 服务器标识
     * @return 连接成功的 FTPClient 实例，或 null
     */
    private FTPClient connect(String serverKey) {
        FtpServerProperties.ServerConfig config = ftpServerProperties.getServers().get(serverKey);
        if (config == null) {
            logger.error("未找到服务器配置: {}", serverKey);
            return null;
        }

        FTPClient ftpClient = new FTPClient();
        try {
            // 设置连接超时时间
            ftpClient.setConnectTimeout(5000);
            ftpClient.setDataTimeout(30000);

            // 连接服务器
            logger.debug("正在连接FTP服务器 {}:{}", config.getHost(), config.getPort());
            ftpClient.connect(config.getHost(), config.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                logger.error("FTP 服务器连接失败: {}:{}，响应码: {}", config.getHost(), config.getPort(), replyCode);
                return null;
            }

            // 登录
            logger.debug("正在登录FTP服务器 {}:{}", config.getHost(), config.getPort());
            boolean loginSuccess = ftpClient.login(config.getUsername(), config.getPassword());
            if (!loginSuccess) {
                ftpClient.disconnect();
                logger.error("FTP 登录失败: {}@{}", config.getUsername(), config.getHost());
                return null;
            }

            // 设置文件类型为二进制，避免文本文件传输时出现问题
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            logger.debug("已设置FTP文件类型为二进制");

            // 设置被动模式或主动模式。被动模式通常更常用，特别是在客户端有防火墙时
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();
                logger.debug("已设置FTP为被动模式");
            } else {
                ftpClient.enterLocalActiveMode();
                logger.debug("已设置FTP为主动模式");
            }

            // 设置编码，解决中文文件名乱码问题
            // ftpClient.setControlEncoding("UTF-8");

            logger.info("成功连接到 FTP 服务器: {}", serverKey);
            return ftpClient;

        } catch (IOException e) {
            logger.error("连接或登录 FTP 服务器时发生异常: {}:{}", config.getHost(), config.getPort(), e);
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioEx) {
                    logger.warn("断开FTP连接时发生异常", ioEx);
                }
            }
            return null;
        }
    }

    /**
     * 断开 FTP 连接
     *
     * @param ftpClient FTPClient 实例
     */
    private void disconnect(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                logger.debug("已断开 FTP 连接");
            } catch (IOException e) {
                logger.warn("断开 FTP 连接时发生异常", e);
            }
        }
    }

    @Override
    public boolean uploadFile(String serverKey, String remotePath, File file) {
        if (file == null || !file.exists()) {
            logger.error("上传文件不存在: {}", file != null ? file.getAbsolutePath() : "null");
            return false;
        }

        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            logger.debug("开始上传文件: {} -> {}", file.getName(), remotePath);
            success = ftpClient.storeFile(remotePath, inputStream);
            if (success) {
                logger.info("文件上传成功: {} -> {}", file.getName(), remotePath);
            } else {
                logger.error("文件上传失败: {} -> {}", file.getName(), remotePath);
            }
        } catch (IOException e) {
            logger.error("上传文件时发生异常: {} -> {}", file.getName(), remotePath, e);
        } finally {
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean uploadFile(String serverKey, String remotePath, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            logger.error("上传的MultipartFile为空");
            return false;
        }

        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            logger.debug("开始上传MultipartFile: {} -> {}", multipartFile.getOriginalFilename(), remotePath);
            success = ftpClient.storeFile(remotePath, inputStream);
            if (success) {
                logger.info("MultipartFile上传成功: {} -> {}", multipartFile.getOriginalFilename(), remotePath);
            } else {
                logger.error("MultipartFile上传失败: {} -> {}", multipartFile.getOriginalFilename(), remotePath);
            }
        } catch (IOException e) {
            logger.error("上传 MultipartFile 时发生异常: {} -> {}", multipartFile.getOriginalFilename(), remotePath, e);
        } finally {
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean downloadFile(String serverKey, String remotePath, File localFile) {
        if (localFile == null) {
            logger.error("本地文件对象为空");
            return false;
        }

        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        // 确保父目录存在
        File parentDir = localFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs() && !parentDir.exists()) {
                logger.error("无法创建本地目录: {}", parentDir.getAbsolutePath());
                return false;
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
            logger.debug("开始下载文件: {} -> {}", remotePath, localFile.getAbsolutePath());
            success = ftpClient.retrieveFile(remotePath, outputStream);
            if (success) {
                logger.info("文件下载成功: {} -> {}", remotePath, localFile.getAbsolutePath());
            } else {
                logger.error("文件下载失败: {} -> {}", remotePath, localFile.getAbsolutePath());
                // 下载失败时删除可能创建的空文件
                if (localFile.exists()) {
                    if (!localFile.delete()) {
                        logger.warn("删除本地文件失败: {}", localFile.getAbsolutePath());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("下载文件到本地时发生异常: {} -> {}", remotePath, localFile.getAbsolutePath(), e);
            // 异常时删除可能创建的文件
            if (localFile.exists()) {
                if (!localFile.delete()) {
                    logger.warn("删除本地文件失败: {}", localFile.getAbsolutePath());
                }
            }
        } finally {
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean downloadFile(String serverKey, String remotePath, OutputStream outputStream) {
        if (outputStream == null) {
            logger.error("输出流对象为空");
            return false;
        }

        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        try {
            logger.debug("开始下载文件到输出流: {}", remotePath);
            success = ftpClient.retrieveFile(remotePath, outputStream);
            if (success) {
                logger.info("文件下载成功: {}", remotePath);
            } else {
                logger.error("文件下载失败: {}", remotePath);
            }
        } catch (IOException e) {
            logger.error("下载文件到 OutputStream 时发生异常: {}", remotePath, e);
        } finally {
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean deleteFile(String serverKey, String remotePath) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        try {
            logger.debug("开始删除文件: {}", remotePath);
            success = ftpClient.deleteFile(remotePath);
            if (success) {
                logger.info("文件删除成功: {}", remotePath);
            } else {
                logger.error("文件删除失败: {}", remotePath);
            }
        } catch (IOException e) {
            logger.error("删除文件时发生异常: {}", remotePath, e);
        } finally {
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean fileExists(String serverKey, String remotePath) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean exists = false;
        try {
            logger.debug("检查文件是否存在: {}", remotePath);
            // 更精确的做法是获取文件所在目录，然后遍历查找文件名
            int lastSeparatorIndex = remotePath.lastIndexOf("/");
            if (lastSeparatorIndex > 0) {
                String directory = remotePath.substring(0, lastSeparatorIndex);
                String fileName = remotePath.substring(lastSeparatorIndex + 1);

                FTPFile[] files = ftpClient.listFiles(directory);
                if (files != null) {
                    for (FTPFile file : files) {
                        if (file.getName().equals(fileName) && !file.isDirectory()) {
                            exists = true;
                            break;
                        }
                    }
                }
            } else {
                // 根目录情况
                FTPFile[] files = ftpClient.listFiles(remotePath);
                exists = files != null && files.length == 1 && !files[0].isDirectory();
            }

            logger.debug("文件 {} 存在性检查结果: {}", remotePath, exists);
        } catch (IOException e) {
            logger.error("检查文件是否存在时发生异常: {}", remotePath, e);
        } finally {
            disconnect(ftpClient);
        }
        return exists;
    }
}
