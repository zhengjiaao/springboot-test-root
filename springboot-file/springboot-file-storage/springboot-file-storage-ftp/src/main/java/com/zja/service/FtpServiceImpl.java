package com.zja.service;

import com.zja.config.FtpServerProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
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
            System.err.println("未找到服务器配置: " + serverKey);
            return null;
        }

        FTPClient ftpClient = new FTPClient();
        try {
            // 连接服务器
            ftpClient.connect(config.getHost(), config.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                System.err.println("FTP 服务器连接失败: " + config.getHost() + ":" + config.getPort());
                return null;
            }

            // 登录
            boolean loginSuccess = ftpClient.login(config.getUsername(), config.getPassword());
            if (!loginSuccess) {
                ftpClient.disconnect();
                System.err.println("FTP 登录失败: " + config.getUsername() + "@" + config.getHost());
                return null;
            }

            // 设置文件类型为二进制，避免文本文件传输时出现问题
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 设置被动模式或主动模式。被动模式通常更常用，特别是在客户端有防火墙时 [[29]]
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode(); // 设置为被动模式 [[29]]
            } else {
                ftpClient.enterLocalActiveMode();  // 设置为主动模式
            }

            // 可选：设置编码，解决中文文件名乱码问题（取决于服务器配置）
            // ftpClient.setControlEncoding("UTF-8");
            // FTPClient.setControlEncoding("UTF-8"); // 有时需要设置静态方法

            System.out.println("成功连接到 FTP 服务器: " + serverKey);
            return ftpClient;

        } catch (IOException e) {
            System.err.println("连接或登录 FTP 服务器时发生异常: " + e.getMessage());
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
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
                System.out.println("已断开 FTP 连接");
            } catch (IOException e) {
                System.err.println("断开 FTP 连接时发生异常: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean uploadFile(String serverKey, String remotePath, File file) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            // 使用 storeFile 方法上传文件
            success = ftpClient.storeFile(remotePath, inputStream);
            if (success) {
                System.out.println("文件上传成功: " + remotePath);
            } else {
                System.err.println("文件上传失败: " + remotePath);
            }
        } catch (IOException e) {
            System.err.println("上传文件时发生异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean uploadFile(String serverKey, String remotePath, MultipartFile multipartFile) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            success = ftpClient.storeFile(remotePath, inputStream);
            if (success) {
                System.out.println("文件上传成功: " + remotePath);
            } else {
                System.err.println("文件上传失败: " + remotePath);
            }
        } catch (IOException e) {
            System.err.println("上传 MultipartFile 时发生异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean downloadFile(String serverKey, String remotePath, File localFile) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(localFile);
            // 使用 retrieveFile 方法下载文件
            success = ftpClient.retrieveFile(remotePath, outputStream);
            if (success) {
                System.out.println("文件下载成功: " + remotePath + " -> " + localFile.getAbsolutePath());
            } else {
                System.err.println("文件下载失败: " + remotePath);
            }
        } catch (IOException e) {
            System.err.println("下载文件到本地时发生异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            disconnect(ftpClient);
        }
        return success;
    }

    @Override
    public boolean downloadFile(String serverKey, String remotePath, OutputStream outputStream) {
        FTPClient ftpClient = connect(serverKey);
        if (ftpClient == null) {
            return false;
        }

        boolean success = false;
        try {
            success = ftpClient.retrieveFile(remotePath, outputStream);
            if (success) {
                System.out.println("文件下载成功: " + remotePath);
            } else {
                System.err.println("文件下载失败: " + remotePath);
            }
        } catch (IOException e) {
            System.err.println("下载文件到 OutputStream 时发生异常: " + e.getMessage());
            e.printStackTrace();
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
            success = ftpClient.deleteFile(remotePath);
            if (success) {
                System.out.println("文件删除成功: " + remotePath);
            } else {
                System.err.println("文件删除失败: " + remotePath);
            }
        } catch (IOException e) {
            System.err.println("删除文件时发生异常: " + e.getMessage());
            e.printStackTrace();
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
            // 使用 listFiles 方法检查文件是否存在
            FTPFile[] files = ftpClient.listFiles(remotePath);
            exists = files != null && files.length == 1 && !files[0].isDirectory();
            // 注意：listFiles 传入文件路径时，如果文件存在会返回一个元素的数组，如果是目录或不存在则不同
            // 更精确的做法是获取文件所在目录，然后遍历查找文件名
            // 这里是简化版，对于精确判断，可能需要先获取父目录再查找
        } catch (IOException e) {
            System.err.println("检查文件是否存在时发生异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            disconnect(ftpClient);
        }
        return exists;
    }
}