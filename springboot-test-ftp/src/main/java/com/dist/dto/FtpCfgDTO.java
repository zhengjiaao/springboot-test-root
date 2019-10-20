package com.dist.dto;

import java.io.Serializable;

/**
 * ftp配置
 */
public class FtpCfgDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器
     */
    private String server;
    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPwd;
    /**
     * 下载路径，相对路径，相对appHome
     */
    private String downloadPath;
    /**
     * ftp服务器路径
     */
    private String serverPath;
    /**
     * ftp 系统内核类型
    * */
    private String ftpSystemType;
    /**
     * ftp 系统编码
     * */
    private String encoding;
    /**
     * 配置应用程序部署的根目录
     * (在linux 中需要进行配置，因为linux ftp操作中 /
     * 无法直接进入ftp服务器根目录)
     */
    private String appHome;

    public FtpCfgDTO() {
        super();
    }

    public String getFtpSystemType() {
        return ftpSystemType;
    }

    public void setFtpSystemType(String ftpSystemType) {
        this.ftpSystemType = ftpSystemType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public FtpCfgDTO(String server, int port, String userName, String userPwd,
                     String downloadPath, String serverPath, String ftpSystemType, String encoding) {
        super();
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.userPwd = userPwd;
        this.downloadPath = downloadPath;
        this.serverPath = serverPath;
        this.encoding=encoding;
        this.ftpSystemType=ftpSystemType;

    }


    public String getAppHome() {
        return appHome;
    }

    public void setAppHome(String appHome) {
        this.appHome = appHome;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String severPath) {
        this.serverPath = severPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
