package com.zja.util;

import com.zja.dto.FTPFileTreeInfo;
import com.zja.dto.FtpCfgDTO;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * FTP 工具类. 可以完成对目录创建的创建、修改、删除,对文件的上传下载等操作.
 */
public class FtpUtil {

    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";
    // FTP协议里面，规定文件名编码为iso-8859-1
    private static String SERVER_CHARSET = "ISO-8859-1";

    public static final int BINARY_FILE_TYPE = 2;

    public static void main(String[] args) {
        String hostName = "192.168.1.209";
        String userName = "public";
        String password = "passw0rd";
        // 连接的FTP路径
        String serverPath = "/";
        String ftpSystemType = "Windows_NT";
        String encoding = "GBK";
        String downloadPath = "CAD";
        try {
            FtpCfgDTO ftpConfig = new FtpCfgDTO(hostName, 21, userName, password, downloadPath, serverPath, ftpSystemType, encoding);
            //连接ftp服务器
            FTPClient ftpClient = connectServer(ftpConfig);
            //判断文件/文件夹是否存在
            boolean exists = isFileExists(ftpClient, "CAD/1.jpg");
            logger.info("文件是否存在：" + exists);
            // 相对于根目录的文件的地址
            String ftpFilePath = "CAD";
            // 本地保存地址
            //相对根路径的目标文件所在的文件夹

            String filePath = "D:\\doc\\" + downloadPath + "\\1.jpg";
            //boolean flag = download(ftpClient,ftpFilePath, filePath);

            //boolean flag = downloadFileFromFtp(ftpClient,ftpFilePath, filePath);
            //logger.info("文件下载是否成功:"+flag);

            downFtpFiles(ftpClient, "CAD", "D:\\doc\\qq\\");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



    /* ====================Start ftp操作=========================*/

    /**
     * 初始化客户端并完成对服务端的连接
     * @param ftpConfig ftp配置类
     * @throws SocketException
     * @throws IOException
     */
    public static FTPClient connectServer(FtpCfgDTO ftpConfig) {
        String server = ftpConfig.getServer();
        int port = ftpConfig.getPort();
        String user = ftpConfig.getUserName();
        String password = ftpConfig.getUserPwd();
        String location = ftpConfig.getServerPath();
        String ftpSystemType = ftpConfig.getFtpSystemType();
        String encoding = ftpConfig.getEncoding();

        return connectServer(server, port, user, password, location, ftpSystemType, encoding);
    }

    /**
     * 初始化客户端并完成对服务端的连接
     * @param server 服务端地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @param path 远程路径 值可以为空
     * @throws SocketException
     * @throws IOException
     */
    public static FTPClient connectServer(String server,
                                          int port,
                                          String username,
                                          String password,
                                          String path,
                                          String ftpSystemType,
                                          String encoding
    ) {
        FTPClient ftp = null;
        if (null == path) {
            path = "";
        }
        try {
            ftp = new FTPClient();
            //对系统进行判断
//            FTPClientConfig conf = new FTPClientConfig(ftpSystemType);
//            conf.setServerLanguageCode("zh");
//            ftp.setControlEncoding(encoding);
//            ftp.configure(conf);
            ftp.connect(server, port);
            // 设置被动模式
//            ftp.enterLocalPassiveMode();
//            ftp.setDataTimeout(120000);
//            // 设置缓冲区大小(提高下载速度)
//            ftp.setBufferSize(10 * 1024);

            setFTPClientDefaultConfig(ftp);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                logger.warn("拒绝连接 {}", server);
            }
            boolean flag = ftp.login(username, password);
            if (flag) {
                logger.info("FTP登录成功。");
            } else {
                logger.warn("FTP登录失败。");
            }

            if (!"".equals(path.trim())) {
                String[] pathes = path.split("/");
                for (String onepath : pathes) {
                    if (onepath == null || "".equals(onepath.trim())) {
                        continue;
                    }
                    if (!ftp.changeWorkingDirectory(onepath)) {
                        ftp.makeDirectory(onepath);
                        boolean flagDir = ftp.changeWorkingDirectory(onepath);
                        if (flagDir) {
                            logger.info("成功连接ftp目录：{}", path);
                        } else {
                            logger.warn("未能连接ftp目录：{}", path);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return ftp;
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
        //本地字符编码 GBK、UTF-8，解决读取到的目录或文件名称乱码
        ftpClient.setControlEncoding(LOCAL_CHARSET);
        //防止中文目录乱码，默 false
        ftpClient.setAutodetectUTF8(true);
        //字符集
        //ftpClient.setCharset(Charset.forName(SERVER_CHARSET));
    }

    /**
     * 设置ftp的文件传输类型
     * @param fileType 如:FTP.BINARY_FILE_TYPE
     * @throws IOException
     */
    public void setFileType(FTPClient ftp, int fileType) throws IOException {
        ftp.setFileType(fileType);
    }

    /**
     * 改变ftp的工作目录
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean changeDirectory(FTPClient ftp, String path) throws IOException {
        return ftp.changeWorkingDirectory(path);
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

    /* ====================End ftp操作=========================*/

    /**
     * 获取ftp子目录信息
     * @param relativeDir 相对目录
     * @param level 级别，level=0表示当前目录以及所有子目录内容；level=1表示获取一级目录内容
     * @throws IOException
     */
    public static List<FTPFileTreeInfo> getSubDirectory(
            FTPClient ftp,
            String relativeDir,
            int level) throws IOException {
        logger.info("relativeDir>>>>>>>>>>>>>>>>> {}", relativeDir);
        ftp.enterLocalPassiveMode();
        List<FTPFileTreeInfo> filePathList = new ArrayList<>();

        if (null == relativeDir) {
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
        boolean flag = changeMutiWorkingDirectory(ftp, relativeDir);
        logger.info("进入成功 {}", flag);
        if (!flag) {
            return filePathList;
        }
        FTPFile[] files = ftp.listFiles();
        for (FTPFile ftpFile : files) {
            logger.info("获取文件: {}", ftpFile.getName());
            String encoding = StringUtil.getEncoding(ftpFile.getName());
            ftpFile.setName(new String(ftpFile.getName().getBytes(encoding), encoding));
        }


        if (files == null || files.length == 0) {
            ftp.changeToParentDirectory();
            return filePathList;
        }
        for (int i = 0; i < files.length; i++) {


            FTPFileTreeInfo vo = new FTPFileTreeInfo();
            if (files[i].isFile()) {
                vo.setType("file");
                vo.setFullPath(relativeDir + files[i].getName());
                vo.setLabel(files[i].getName());
                vo.setExtension(files[i].getName().substring(files[i].getName().lastIndexOf('.') + 1));
                vo.setChildren(null);
                filePathList.add(vo);

            } else if (files[i].isDirectory()) {
                /**
                 * 排除linux 内核 环境搭建 出现目录中. 以及 .. 的 目录
                 * 请注意 ser-u 会出现这种情况 不知道 FileZilla 会不会 貌似是不会 ，看丁成文的代码
                 */

                if (files[i].getName().equals(".") || files[i].getName().equals("..")) {
                    continue;
                }
                vo.setType("folder");
                vo.setFullPath(relativeDir + files[i].getName());
                vo.setLabel(files[i].getName());
                if (0 == level) {
                    vo.setChildren(getSubDirectory(ftp, relativeDir + files[i].getName() + "/", level));
                }
                filePathList.add(vo);
            }
        }
        return filePathList;
    }

    /**
     * 切换目录：可以切换多级目录（处理切换多级目录）,ftp进入多层目录，无法一次进入
     * @param ftp
     * @param paths
     * @return
     * @throws IOException
     */
    public static boolean changeMutiWorkingDirectory(FTPClient ftp, String paths) throws IOException {
        boolean flag = true;
        String[] pathArr = paths.split("/");
        ftp.changeWorkingDirectory("/");
        logger.info("进入根目录 {}", "/");
        for (String path : pathArr) {
            if ("".equals(path) || "/".equals(path)) {
                continue;
            }
            flag = ftp.changeWorkingDirectory(nameCharset(path));
            logger.info("进入 {}", path);
        }
        return flag;
    }


    /************* Start 上传文件到ftp服务上***************/

    /**
     * 上传文件,并重命名.
     * @param localFile 上传的文件,本地绝对路径
     * @param remoteName 相对路径+新的文件名  /test/text.txt
     * @return 上传结果, 是否成功.
     * @throws IOException
     */
    public static boolean uploadFile(FTPClient ftp, String localFile, String remoteName)
            throws IOException {
        boolean flag = false;
        try (InputStream iStream = new FileInputStream(localFile);) {
            logger.info("ftp new file name {}", remoteName);

            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //将当前数据连接模式设置为PASSIVE_LOCAL_DATA_CONNECTION_MODE，即：被动模式
            ftp.enterLocalPassiveMode();
            //将流写到服务器
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

        logger.info("传入的文件：" + filePathName);

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
            ftp.setFileType(BINARY_FILE_TYPE);
            flag = ftp.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            logger.info("文件上传到FTP报错:", e);
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("上传文件到FTP所耗时间:" + (endTime - startTime) + "ms");
        return flag;
    }

    /********** End 上传文件到ftp服务上 ***********/


    /* &&&&&&&  Start 从ftp下载文件  &&&&&&*/

    /**
     * 下载文件
     * @param remoteFileName 远程文件名，路径开头不能以“/”或者“\\”开始
     * @param localFileName 本地文件
     * @return 返回操作结果
     * @throws IOException
     */
    public static boolean download(FTPClient ftp, String remoteFileName, String localFileName)
            throws IOException {

        boolean flag = false;
        File outfile = new File(localFileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outfile);
            ftp.setControlEncoding("GBK");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //设置被动模式  java中，内网用被动模式 ，外网连接时用主动模式
            ftp.enterLocalActiveMode();//这句重要，不行换enterRemoteActiveMode(主动模式) 看看
            //remoteFileName = new String(remoteFileName.getBytes("gbk"),"iso-8859-1");

            flag = ftp.retrieveFile(remoteFileName, oStream);
            if (flag) {
                logger.info("成功下载文件：" + localFileName);
            } else {
                logger.info("下载文件失败。文件：" + remoteFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            return flag;
        } finally {
            oStream.close();
        }
        return flag;
    }

    /**
     * 从ftp上下载文件到本地
     * @param ftpClient
     * @param remoteFilepath
     * @param localFilePath
     * @return
     */
    public static boolean downloadFileFromFtp(FTPClient ftpClient, String remoteFilepath, String localFilePath) {
        boolean flag = false;
        logger.info("remote file path is : " + remoteFilepath);
        logger.info("local file path is :" + localFilePath);
        Assert.notNull(ftpClient, "-------->ftpClient can not be null!");
        Assert.hasLength(remoteFilepath, "--------->remoteFilePath can not be empty or null!");
        Assert.hasLength(localFilePath, "--------->remoteFilePath can not be empty or null!");
        try {
            //判断本地文件是否已经存在，如果已经存在，则无需下载
            //FTPClient ftpClient = FtpUtil.connectServer(ftp);
            File localFile = new File(localFilePath);
            if (!localFile.exists()) {
                InputStream inputStream = downFile(ftpClient, remoteFilepath);
                if (inputStream != null) {
                    flag = SystemUtil.inputStreamToFile(inputStream, localFilePath);
                    result(flag, localFile.getName());
                }
            } else {
                if (localFile.length() == 0) {
                    logger.info("文件大小为0，即将为您重新下载！");
                    localFile.delete();
                    InputStream inputStream = downFile(ftpClient, remoteFilepath);
                    if (inputStream != null) {
                        flag = SystemUtil.inputStreamToFile(inputStream, localFilePath);
                        result(flag, localFile.getName());
                    }
                } else {
                    logger.info("本地文件已存在......." + localFile.getName());
                    flag = true;
                }
            }
            closeServer(ftpClient);
            return flag;
        } catch (Exception e) {
            flag = false;
            logger.error("从FTP下载文件到本地失败...");
            logger.error(String.valueOf(e));
            e.printStackTrace();
            return flag;
        }
    }

    /**
     * 下载文件,返回InputStream
     * @param sourceFileName 远程文件
     * @return InputStream
     * @throws IOException
     */
    public static InputStream downFile(FTPClient ftp, String sourceFileName) throws IOException {
        long beforeTime = System.currentTimeMillis();
        logger.info("FTP当前工作目录:" + ftp.printWorkingDirectory());
        InputStream inputStream = ftp.retrieveFileStream(sourceFileName);
        try {
            long afterTime = System.currentTimeMillis();
            if (inputStream != null) {
                logger.info("FTP文件下载耗时/毫秒:" + (afterTime - beforeTime));
                logger.info("文件流读取成功...");
                return inputStream;
            } else {
                logger.error("文件流读取失败，请检查FTP上是否存在该文件...");
                return inputStream;
            }
        } catch (Exception e) {
            logger.error("将FTP上的文件读取为InputStream失败..." + e);
            return null;
        }
    }

    /**
     * 将文件流转化为本地文件
     * @param inputStream ftp 流
     * @param localFilePath 本地路径地址
     * @return boolean
     * @throws IOException
     */
    public static boolean inputStreamToFile(InputStream inputStream, String localFilePath) throws IOException {
        File newDir = new File(localFilePath.replace("\\", "/").substring(0, localFilePath.lastIndexOf("/")));
        if (!newDir.exists()) newDir.mkdirs();
        FileOutputStream fs = new FileOutputStream(localFilePath);
        try {
            int bytesum = 0;
            int byteread = 0;

            byte[] buffer = new byte[1024];
            long startTime = System.currentTimeMillis();
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            long endTime = System.currentTimeMillis();
            logger.info("将文件流转化为本地文件耗时/毫秒:" + (endTime - startTime));
            return true;
        } catch (Exception e) {
            logger.error("将文件流转化为本地文件失败");
            e.printStackTrace();
            return false;
        } finally {
            fs.close();
        }
    }

    /* &&&&&&&&&&&&&&&&&&&&&&&& End 从ftp下载文件 &&&&&&&&&&&&&&&&&&&&&&&&&&&&*/


    /*###########################START文件目录操作########################*/

    /**
     * 在服务端创建目录
     * @param pathName 可以是相对目录或绝对目录
     * @return
     * @throws IOException
     */
    public static boolean createDirectory(FTPClient ftp, String pathName) throws IOException {
        return ftp.makeDirectory(pathName);
    }

    /**
     * 删除一个FTP服务器上的目录（如果为空）
     * @param path 目录路径
     * @return
     * @throws IOException
     */
    public static boolean removeDirectory(FTPClient ftp, String path) throws IOException {
        return ftp.removeDirectory(path);
    }

    /**
     * 删除一个FTP服务器上的目录
     * @param path 目录路径
     * @param isAll 是否删除所有子目录和文件,如果有
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(FTPClient ftp, String path, boolean isAll)
            throws IOException {

        if (!isAll) {
            return removeDirectory(ftp, path);
        }
        //遍历子目录和文件
        FTPFile[] ftpFileArr = ftp.listFiles(path);
        if (ftpFileArr == null || ftpFileArr.length == 0) {
            return removeDirectory(ftp, path);
        }

        for (int i = 0; i < ftpFileArr.length; i++) {
            FTPFile ftpFile = ftpFileArr[i];
            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                removeDirectory(ftp, path + "/" + name, true);
            } else if (ftpFile.isFile()) {
                deleteFile(ftp, path + "/" + name);
            } else if (ftpFile.isSymbolicLink()) {

            } else if (ftpFile.isUnknown()) {

            }
        }
        return removeDirectory(ftp, path);
    }

    /**
     * 删除ftp所有文件夹
     * @param ftp
     * @param pathname
     * @return
     */
    public static boolean removeAll(FTPClient ftp, String pathname) {
        try {
            ftp.removeDirectory(pathname);
            FTPFile[] files = ftp.listFiles(pathname);
            for (FTPFile f : files) {
                if (f.isDirectory()) {
                    removeAll(ftp, pathname + "/" + f.getName());
                    boolean isDelDir = ftp.removeDirectory(pathname);
                    logger.info("FTP目录是否已删除>>" + isDelDir);
                }
                if (f.isFile()) {
                    boolean isDelFile = ftp.deleteFile(pathname + "/" + f.getName());
                    ftp.removeDirectory(pathname);
                    logger.info("FTP文件是否已删除>>" + isDelFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*###########################END文件目录操作########################*/


    /****************************START 文件操作**************************************/
    /**
     * 判断文件或文件夹是否存在
     * @param ftp
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isFileExists(FTPClient ftp, String path) throws IOException {
        //判断返回列表大小，为0就表示没有
        FTPFile[] ftpFileArr = ftp.listFiles(path);
        if (ftpFileArr.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除文件
     * @param pathName 文件名
     * @return 删除结果, 是否成功.
     * @throws IOException
     */
    public static boolean deleteFile(FTPClient ftp, String pathName) throws IOException {
        return ftp.deleteFile(pathName);
    }

    /**
     * 返回给定目录下的文件
     * @param path
     * @return FTPFile组成的集合
     * @throws IOException
     */
    public static List getFileList(FTPClient ftp, String path) throws IOException {

        ftp.changeWorkingDirectory(path);//切换对应路径下
        logger.info("FTP当前目录:" + ftp.printWorkingDirectory());
        FTPFile[] ftpFiles = ftp.listFiles();
        List retList = new ArrayList();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (int i = 0; i < ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isFile()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    /**
     * 获取文件最后编辑时间
     * @param timeInMillis
     * @return
     */
    public static String getFileLastModified(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        return sdf.format(cal.getTime());
    }

    /**
     * 文件大小格式化处理
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位  16696832
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /****************************end 文件操作**************************************/

    /**
     * 递归遍历文件夹以及子文件夹的文件
     * @param pathName
     * @param filePathList
     * @throws IOException
     */
    public static void getFileListRecursion(FTPClient ftp, String pathName, List<String> filePathList) throws IOException {

        if (null == pathName) {
            pathName = "/";
        } else {
            pathName = pathName.replace("\\", "/");
        }
        if (!pathName.startsWith("/")) {
            pathName = "/" + pathName;
        }
        if (!pathName.endsWith("/")) {
            pathName = pathName + "/";
        }

        ftp.changeWorkingDirectory(pathName);
        FTPFile[] files = ftp.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                logger.info("得到文件:" + files[i].getName());
                logger.info("--------------");
                filePathList.add(ftp.printWorkingDirectory() + "/" + files[i].getName());
            } else if (files[i].isDirectory()) {
                getFileListRecursion(ftp, pathName + files[i].getName() + "/", filePathList);
            }
        }
    }

    /**
     *功能说明：1.通过递归实现ftp目录文件与本地文件同步更新
     * 			2. 下载ftp当前目录下所有的文件及文件夹(包括子目录)
     * @param ftpfilepath 当前ftp目录
     * @param localpath 当前本地目录
     */
    public static void downFtpFiles(FTPClient ftp, String ftpfilepath, String localpath) {
		/*if(null == ftpfilepath){
			ftpfilepath = "/";
		}else{
			ftpfilepath = ftpfilepath.replace("\\", "/");
		}
		if(!ftpfilepath.startsWith("/")) {
			ftpfilepath ="/"+ftpfilepath;
		}
		if(!ftpfilepath.endsWith("/")) {
			ftpfilepath = ftpfilepath + "/";
		}*/
        try {
            logger.info("ftpfilepath=" + ftpfilepath);
            FTPFile[] ff = ftp.listFiles(ftpfilepath);
            // 得到当前ftp目录下的文件列表
            if (ff != null) {
                for (int i = 0; i < ff.length; i++) {
                    logger.info("File名称：" + ff[i].getName());
                    String localfilepath = localpath + ff[i].getName();
                    File localFile = new File(localfilepath);
                    // 根据ftp文件生成相应本地文件
                    Date fflastModifiedDate = ff[i].getTimestamp().getTime();
                    // 获取ftp文件最后修改时间
                    Date localLastModifiedDate = new Date(localFile
                            .lastModified());
                    // 获取本地文件的最后修改时间
                    int result = localLastModifiedDate
                            .compareTo(fflastModifiedDate);
                    // result=0，两文件最后修改时间相同；result<0，本地文件的最后修改时间早于ftp文件最后修改时间；result>0，则相反
                    if (ff[i].isDirectory()) {
                        // 如果是目录
                        localFile.mkdir();
                        // 如果本地文件夹不存在就创建
                        String ftpfp = ftpfilepath + "/" + ff[i].getName() + "/";
                        // 转到ftp文件夹目录下
                        String localfp = localfilepath + "\\";
                        // 转到本地文件夹目录下
                        // 递归调用
                        downFtpFiles(ftp, ftpfp, localfp);
                    }
                    if (ff[i].isFile()) {
                        // 如果是文件
                        File lFile = new File(localpath);
                        lFile.mkdir();
                        // 如果文件所在的文件夹不存在就创建
                        if (!lFile.exists()) {
                            return;
                        }
                        if (ff[i].getSize() != localFile.length() || result < 0) {
                            // 如果ftp文件和本地文件大小不一样或者本地文件不存在或者ftp文件有更新，就进行创建、覆盖
                            String filepath = ftpfilepath + "/" + ff[i].getName();
                            // 目标ftp文件下载路径
                            InputStream inputStream = downFile(ftp, filepath);
                            if (inputStream != null) {
                                boolean flag = SystemUtil.inputStreamToFile(inputStream, localfilepath);
                                result(flag, localFile.getName());
                            }
                            inputStream.close();
                            ftp.completePendingCommand();
                        } else {
                            logger.info("两个文件相同！");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能说明：根据ftp文件30秒刷新一次本地文件
     *
     * @param ftpClient
     */
    public static void ftpRefresh(FTPClient ftpClient, String ftpfilepath, String localpath) {

        while (true) {
            try {
                downFtpFiles(ftpClient, ftpfilepath, localpath);
            } catch (Exception e) {
                logger.info("读取异常");
            }
            try {
                Thread.sleep(30 * 1000);
                logger.info("刷新成功：每30s 从ftp把文件刷新到本地");
            } catch (Exception e) {
                logger.info("刷新失败！");
            }
        }
    }

    /**
     * ftp文件下载结果
     * @param flag
     * @param fileNme
     */
    private static void result(boolean flag, String fileNme) {
        if (flag) {
            logger.info("[ " + fileNme + " ] 下载成功！");
        } else {
            logger.info("【 " + fileNme + " 】 下载失败！");
        }
        return;
    }

    /**
     * 编码转换  FTP协议里面，规定文件名编码为 iso-8859-1
     * @param name
     */
    private static String nameCharset(String name) throws UnsupportedEncodingException {
        return new String(name.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
    }
}
