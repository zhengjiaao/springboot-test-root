/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-05 16:14
 * @Since:
 */
package com.zja.util;

import com.zja.dto.FTPFileTreeInfo;
import com.zja.dto.FtpCfgDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * FtpClient 工具类
 *
 * java中，内网用被动模式 ，外网连接时用主动模式
 */
@Slf4j
public class FtpClientUtil {

    public static void main(String[] args) throws IOException {
        String path = "/";
        FTPClient ftpClient = createFtpClient("192.168.1.209", 21, "public", "passw0rd", path, "Windows_NT", "GBK");
        System.out.println(ftpClient.getAutodetectUTF8());
        System.out.println(ftpClient.getBufferSize());
        System.out.println(ftpClient.getControlKeepAliveReplyTimeout());
        System.out.println(ftpClient.getControlKeepAliveTimeout());
        System.out.println(ftpClient.getDataConnectionMode());
        System.out.println(ftpClient.getListHiddenFiles());
//        System.out.println(ftpClient.getModificationTime());
        System.out.println("获取被动主机=" + ftpClient.getPassiveHost());
        System.out.println(ftpClient.getPassiveLocalIPAddress());
        System.out.println(ftpClient.getReceiveDataSocketBufferSize());
        System.out.println(ftpClient.getRestartOffset());
        System.out.println(ftpClient.getSendDataSocketBufferSize());
        System.out.println("获取状态=" + ftpClient.getStatus());
        System.out.println(ftpClient.getSystemType());
        System.out.println("Charset=" + ftpClient.getCharset());
        System.out.println("ControlEncoding=" + ftpClient.getControlEncoding());
        System.out.println(ftpClient.getConnectTimeout());
        System.out.println("获取回复字符串=" + ftpClient.getReplyString());
        System.out.println(ftpClient.getServerSocketFactory());
        System.out.println(ftpClient.getTcpNoDelay());

        String driPath = "/培训分享";
        List<FTPFileTreeInfo> subDirectory = getSubDirectory(ftpClient, driPath, 1);
        System.out.println(subDirectory);

//        downloadFile(ftpClient, "/培训分享/离线安装harbor.docx", "D:\\离线安装harbor.docx");

    }

    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    // FTP协议里面，规定文件名编码为iso-8859-1
    private static String SERVER_CHARSET = "ISO-8859-1";

    public static FTPClient initFtpClient(FtpCfgDTO ftpConfig) {
        String server = ftpConfig.getServer();
        int port = ftpConfig.getPort();
        String user = ftpConfig.getUserName();
        String password = ftpConfig.getUserPwd();
        String serverPath = ftpConfig.getServerPath();
        String ftpSystemType = ftpConfig.getFtpSystemType();
        String encoding = ftpConfig.getEncoding();

        return createFtpClient(server, port, user, password, serverPath, ftpSystemType, encoding);
    }

    public static FTPClient createFtpClient(String server,
                                            int port,
                                            String username,
                                            String password,
                                            String serverPath,
                                            String ftpSystemType,
                                            String encoding) {
        FTPClient ftpClient = new FTPClient();
        try {
            //连接
            ftpClient.connect(server, port);
            //校验连接是否完成
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                log.error("FTP拒绝连接: {}", server);
            }
            //登录
            boolean flag = ftpClient.login(username, password);
            if (flag) {
                log.info("FTP登录成功。");
            } else {
                log.error("FTP登录失败,用户名或密码错误。");
            }

            //设置FTP客户端 默认配置
            setFTPClientDefaultConfig(ftpClient);

            //创建连接的工作目录
            createWorkingDirectory(ftpClient, serverPath);

            return ftpClient;
        } catch (SocketException e) {
            e.printStackTrace();
            log.error("FTPClient IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            log.error("FTPClient 创建连接失败");
            e.printStackTrace();
        }
        return ftpClient;
    }


    /**
     * 设置FTP客户端 默认配置
     * @param ftpClient
     */
    public static void setFTPClientDefaultConfig(FTPClient ftpClient) throws IOException {
        // 设置被动模式
        ftpClient.enterLocalPassiveMode();
        ftpClient.setDataTimeout(120000);
        // 设置缓冲区大小(提高下载速度)
        ftpClient.setBufferSize(10 * 1024);
        //系统判断 默 FTPClientConfig.SYST_UNIX ，若改为 windwos 则设置为："Windows_NT"
        FTPClientConfig clientConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        clientConfig.setServerLanguageCode("zh");
        ftpClient.configure(clientConfig);

        //校验ftp服务器端是否支持UTF-8，若支持就用UTF-8编码，否则使用本地编码(GBK) LOCAL_CHARSET
        if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
                "OPTS UTF8", "ON"))) {
            LOCAL_CHARSET = "UTF-8";
        }
        //本地字符编码 GBK、UTF-8，解决读取到的目录或文件名称显示乱码
        ftpClient.setControlEncoding(LOCAL_CHARSET);
        //防止中文目录乱码，默 false
        ftpClient.setAutodetectUTF8(true);
        //字符集
        //ftpClient.setCharset(Charset.forName(SERVER_CHARSET));
    }

    /**
     * 获取ftp子目录信息
     * @param relativeDir 相对目录
     * @param level 级别，level=0表示当前目录以及所有子目录内容；level=1表示获取一级目录内容
     */
    public static List<FTPFileTreeInfo> getSubDirectory(
            FTPClient ftpClient,
            String relativeDir,
            int level) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //路径转换
        String relativeDirPath = relativeDirConver(relativeDir);
        //进入目录
        boolean flag = changeMutiWorkingDirectory(ftpClient, relativeDirPath);
        if (!flag) {
            throw new RuntimeException("目录可能不存在或编码不正确：" + relativeDirPath);
        }

        List<FTPFileTreeInfo> filePathList = new ArrayList<>();
        FTPFile[] files = ftpClient.listFiles();
        for (FTPFile ftpFile : files) {
            log.debug("获取文件: {}", ftpFile.getName());
            String encoding = StringUtil.getEncoding(ftpFile.getName());
            ftpFile.setName(new String(ftpFile.getName().getBytes(encoding), encoding));
        }

        if (null == files || files.length == 0) {
            ftpClient.changeToParentDirectory();
            return filePathList;
        }
        for (int i = 0; i < files.length; i++) {
            FTPFileTreeInfo vo = new FTPFileTreeInfo();
            if (files[i].isFile()) {
                vo.setType("file");
                vo.setFullPath(relativeDirPath + files[i].getName());
                vo.setLabel(files[i].getName());
                vo.setExtension(files[i].getName().substring(files[i].getName().lastIndexOf('.') + 1));
                vo.setChildren(null);
                filePathList.add(vo);

            } else if (files[i].isDirectory()) {
                //排除linux 内核 环境搭建 出现目录中. 以及 .. 的 目录
                //请注意 ser-u 会出现这种情况 不知道 FileZilla 会不会 貌似是不会
                if (files[i].getName().equals(".") || files[i].getName().equals("..")) {
                    continue;
                }
                vo.setType("folder");
                vo.setFullPath(relativeDirPath + files[i].getName());
                vo.setLabel(files[i].getName());
                if (0 == level) {
                    vo.setChildren(getSubDirectory(ftpClient, relativeDirPath + files[i].getName() + "/", level));
                }
                filePathList.add(vo);
            }
        }
        return filePathList;
    }

    /**
     * 切换目录：可以切换多级目录（处理切换多级目录）,ftp进入多层目录，无法一次进入
     * @param ftpClient
     * @param relativeDirPath 相对路径 /a/b/c
     */
    public static boolean changeMutiWorkingDirectory(FTPClient ftpClient, String relativeDirPath) {
        //路径转换
        relativeDirPath = relativeDirConver(relativeDirPath);
        boolean result = false;
        try {
            String[] pathArr = relativeDirPath.split("/");
            ftpClient.changeWorkingDirectory("/");
            for (String path : pathArr) {
                if ("".equals(path) || "/".equals(path)) {
                    continue;
                }
                result = ftpClient.changeWorkingDirectory(controlEncoding(path));
            }
        } catch (IOException e) {
            log.error("进入目录失败：{}", relativeDirPath);
            e.printStackTrace();
        }
        log.info("进入目录：{}, 结果：{}", relativeDirPath, result);
        return result;
    }

    /**
     * 创建工作目录
     * @param ftpClient
     * @param relativeDirPath  示例 /a/b/c
     */
    public static void createWorkingDirectory(FTPClient ftpClient, String relativeDirPath) throws IOException {
        relativeDirPath = relativeDirConver(relativeDirPath);
        if (!"".equals(relativeDirPath.trim())) {
            String[] pathes = relativeDirPath.split("/");
            ftpClient.changeWorkingDirectory("/");
            for (String path : pathes) {
                if ("".equals(path) || "/".equals(path)) {
                    continue;
                }
                path = controlEncoding(path);
                if (!ftpClient.changeWorkingDirectory(path)) {
                    boolean makeDirectory = ftpClient.makeDirectory(path);
                    log.info("创建目录：{} , 结果：{}", relativeDirPath, makeDirectory);
                    boolean flagDir = ftpClient.changeWorkingDirectory(path);
                    if (flagDir) {
                        log.info("成功连接ftp目录：{}", relativeDirPath);
                    } else {
                        log.warn("未能连接ftp目录：{}", relativeDirPath);
                    }
                }
            }
        }
    }

    /**
     * 路径转换 转换 "\\" -->  "/" 等
     */
    private static String relativeDirConver(String relativeDir) {
        if (!StringUtils.hasLength(relativeDir)) {
            relativeDir = "/";
        } else {
            relativeDir = relativeDir.replace("\\", "/");
        }
        if (!relativeDir.startsWith("/")) {
            relativeDir = "/" + relativeDir;
        }
        if (!relativeDir.endsWith("/")) {
            relativeDir = relativeDir + "/";
        }
        return relativeDir;
    }

    /************* Start 上传文件到ftp服务上***************/

    /**
     * 上传文件,并重命名.
     * @param localFile 上传的文件,本地绝对路径
     * @param remoteName 相对路径+新的文件名  /test/text.txt
     * @return 上传结果, 是否成功.
     * @throws IOException
     */
    public static boolean uploadFile(FTPClient ftp, String localFile, String remoteName) {
        boolean flag = false;
        try (InputStream iStream = new FileInputStream(localFile);) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.storeFile(new String(localFile.getBytes("GBK"), "iso-8859-1"), iStream);
            flag = ftp.storeFile(remoteName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        }
        return flag;
    }

    /**
     * 上传文件
     * @param filePathName 上传的文件,包含目录的文件名
     * @return 上传结果, 是否成功.  上传速度大概为  1M/s
     * @throws IOException
     */
    public static boolean uploadFile(FTPClient ftp, String filePathName) throws IOException {

        log.info("传入的文件：" + filePathName);

        return uploadFile(ftp, filePathName, new File(filePathName).getName());
    }

    /**
     * 上传文件,从InputStream
     * @param iStream 文件流
     * @param newName 新的文件名
     * @return 上传结果, 是否成功.
     * @throws IOException
     */
    public static boolean uploadFile(FTPClient ftp, InputStream iStream, String newName) throws IOException {
        boolean flag = false;
        long startTime = System.currentTimeMillis();
        try {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            flag = ftp.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            log.info("文件上传到FTP报错:", e);
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("上传文件到FTP所耗时间:" + (endTime - startTime) + "ms");
        return flag;
    }

    /********** End 上传文件到ftp服务上 ***********/

    /* -------------从ftp下载文件--------------- */

    /**
     * 下载文件
     * @param ftpClient
     * @param remoteFileName  /a/b/c.txt
     * @param localFileName   d:\\a\\c.txt
     */
    public static boolean downloadFile(FTPClient ftpClient, String remoteFileName, String localFileName) throws IOException {
        return ftpClient.retrieveFile(controlEncoding(remoteFileName), new FileOutputStream(localFileName));
    }

    /**
     * 读取流
     * @param ftpClient
     * @param remoteFileName /a/b/c.txt
     */
    public static InputStream downloadFile(FTPClient ftpClient, String remoteFileName) throws IOException {
        return ftpClient.retrieveFileStream(controlEncoding(remoteFileName));
    }

    /**
     * 关闭ftp连接
     * @throws IOException
     */
    public static void closeServer(FTPClient ftp) throws IOException {
        if (ftp != null && ftp.isConnected()) {
            ftp.logout();
            ftp.disconnect();
        }
    }

    /**
     * ftp文件下载结果
     * @param flag
     * @param fileNme
     */
    private static void result(boolean flag, String fileNme) {
        if (flag) {
            log.info("[ " + fileNme + " ] 下载成功！");
        } else {
            log.info("【 " + fileNme + " 】 下载失败！");
        }
        return;
    }

    /**
     * 编码转换  FTP协议里面，规定文件名编码为 iso-8859-1
     * @param str
     */
    private static String controlEncoding(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes(LOCAL_CHARSET), FTP.DEFAULT_CONTROL_ENCODING);
    }
}
