package com.dist.utils.resourceStorage;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * ftp远程操作工具
 *
 * @author yujx
 * @src weifj
 * @date 2019/03/25 09:10
 */
public class FTPResourceStorage implements IResourceStorage {

    private final static Logger log = LoggerFactory.getLogger(FTPResourceStorage.class);

    private static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;

    /**
     * 上传文件到文件服务
     *
     * @param localFilePath 本地文件路径
     * @param path          服务上存储的标识
     * @return 是否上传成功
     */
    @Override
    public Boolean uploadFile(String localFilePath, String path) {

        this.connectServer();

        InputStream iStream = null;
        try {
            System.out.println("ftp new file name : " + path);
            // 如果包含不存在的目录，则创建，否则直接使用
            createDirecroty(ftp, path);
            ftp.setControlEncoding("GBK");
            iStream = new FileInputStream(localFilePath);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.storeFile(new String(localFilePath.getBytes("GBK"), "iso-8859-1"), iStream);
            return ftp.storeFile(path, iStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.closeServer();
        }
        log.error("文件{}上传失败", localFilePath);
        return false;
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
        try {
            ftp.setFileType(BINARY_FILE_TYPE);
            createDirecroty(ftp, path);
            return ftp.storeFile(path, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传文件失败，文件的path为{}", path);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 上传文件流到文件服务
     *
     * @param inputStream 文件输入流
     * @param path        服务上存储的标识
     * @param fileName    文件名
     * @return 是否上传成功
     */
    @Override
    public Boolean uploadFileStream(InputStream inputStream, String path, String fileName) {
        throw new UnsupportedOperationException();
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

        this.connectServer();

        File outfile = new File(localFilePath);
        OutputStream oStream = null;
        try {
            if (!outfile.getParentFile().exists()) {
                outfile.getParentFile().mkdirs();
            }
            oStream = new FileOutputStream(outfile);
            ftp.setControlEncoding("GBK");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            Boolean flag = ftp.retrieveFile(path, oStream);
            if (flag) {
                log.info("成功下载文件，本地路径为：{}", localFilePath);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oStream != null) {
                try {
                    oStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.closeServer();
        }

        log.error("文件{}下载失败", path);
        return false;
    }

    /**
     * 从文件服务上获取文件流
     *
     * @param path 服务上存储的标识
     * @return 文件流
     */
    @Override
    public InputStream downloadFileStream(String path) {

        this.connectServer();

        try {
            ftp.setFileType(BINARY_FILE_TYPE);
            return ftp.retrieveFileStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.closeServer();
        }

        log.error("{}的文件流获取失败", path);
        return null;
    }

    /**
     * 通过path获取文件名
     *
     * @param path 服务上存储的标识
     * @return 文件名【包括后缀】
     */
    @Override
    public String getFileNameByPath(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除文件
     *
     * @param path
     *
     * @return
     */
    @Override
    public boolean deleteFile(String path) {
        throw new UnsupportedOperationException();
    }


    private FTPClient ftp;

    private FTPProperties ftpProperties;

    /**
     * 初始化客户端并完成对服务端的连接
     */
    private void connectServer() {

        if (ftpProperties != null) {
            String server = ftpProperties.getServer();
            int port = ftpProperties.getPort();
            String username = ftpProperties.getUserName();
            String password = ftpProperties.getUserPwd();
            String path = ftpProperties.getServerPath();

            if (null == path) {
                path = "";
            }
            try {
                ftp = new FTPClient();
                // 下面四行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
                // 如果使用serv-u发布ftp站点，则需要勾掉“高级选项”中的“对所有已收发的路径和文件名使用UTF-8编码”
                ftp.setControlEncoding("GBK");
                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                conf.setServerLanguageCode("zh");
                ftp.configure(conf);
                ftp.connect(server, port);
                // 设置被动模式
                // ftp.enterLocalActiveMode();
                ftp.enterLocalPassiveMode();
                ftp.setDataTimeout(120000);

                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    ftp.disconnect();
                    System.out.println(server + " 拒绝连接");
                }
                boolean flag = ftp.login(username, password);
                if (flag) {
                    System.out.println("FTP登录成功。");
                } else {
                    System.out.println("FTP登录失败。");
                }
                System.out.println(ftp.printWorkingDirectory());

                if (path.length() != 0) {
                    // String str = new String(path.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
                    if (!"".equals(path.trim())) {
                        String[] pathes = path.split("/");
                        for (String onepath : pathes) {
                            if (onepath == null || "".equals(onepath.trim())) {
                                continue;
                            }
                            // onepath=new String(onepath.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING);
                            if (!ftp.changeWorkingDirectory(onepath)) {
                                ftp.makeDirectory(onepath);
                                boolean flagDir = ftp.changeWorkingDirectory(onepath);
                                System.out.println(ftp.printWorkingDirectory());
                                if (flagDir) {
                                    System.out.println("成功连接ftp目录：" + path);
                                } else {
                                    System.out.println("未能连接ftp目录：" + path);
                                }
                            }
                        }
                    }
                }
                System.out.println(ftp.printWorkingDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        throw new RuntimeException("ftp的配置为空！");
    }

    /**
     * 关闭ftp连接
     *
     * @throws IOException
     */
    private void closeServer() {
        try {
            if (ftp != null && ftp.isConnected()) {
                ftp.logout();
                ftp.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     *
     * @param ftp
     * @param remoteFilePath
     * @return
     * @throws IOException
     */
    private boolean createDirecroty(FTPClient ftp, String remoteFilePath) throws IOException {
        int reply;
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
        }
        ftp.changeWorkingDirectory("/");// 转移到FTP服务器目录

        boolean success = true;
        String directory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory), ftp)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            StringBuilder paths = new StringBuilder();
            while (true) {
                String subDirectory = new String(remoteFilePath.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path, ftp)) {
                    if (makeDirectory(subDirectory, ftp)) {
                        changeWorkingDirectory(subDirectory, ftp);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory, ftp);
                    }
                } else {
                    changeWorkingDirectory(subDirectory, ftp);
                }

                paths.append("/").append(subDirectory);
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return true;
    }

    /**
     * 改变目录路径
     *
     * @param directory
     * @param ftp
     * @return
     */
    private static boolean changeWorkingDirectory(String directory, FTPClient ftp) {
        boolean flag = true;
        try {
            flag = ftp.changeWorkingDirectory(directory);
            if (flag) {
                System.out.println("进入文件夹" + directory + " 成功！");
            } else {
                System.out.println("进入文件夹" + directory + " 失败！");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }


    /**
     * 创建目录
     *
     * @param dir
     * @param ftp
     * @return
     */
    private static boolean makeDirectory(String dir, FTPClient ftp) {
        boolean flag = true;
        try {
            flag = ftp.makeDirectory(dir);
            if (flag) {
                System.out.println("创建文件夹" + dir + " 成功！");
            } else {
                System.out.println("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断ftp服务器文件是否存在
     *
     * @param path
     * @param ftp
     * @return
     * @throws IOException
     */
    private static boolean existFile(String path, FTPClient ftp) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftp.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }


    public void setFtpProperties(FTPProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }
}
