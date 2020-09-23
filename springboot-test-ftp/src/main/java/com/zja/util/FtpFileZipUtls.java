package com.zja.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-07-19 17:10
 */
public class FtpFileZipUtls {

    private final static Logger logger = LoggerFactory.getLogger(FtpFileZipUtls.class);

    /**
     * 加载一个非空文件夹
     */
    private static void loadFile(FTPClient ftp, String dirPath, ZipOutputStream zos) throws IOException {

        FTPFile[] ftpFiles = ftp.listFiles();
        // length小于0用于区分非空文件夹与空文件夹和文件的区别
        // 退到上层再进入
        if (ftpFiles.length <= 0 && ftp.changeToParentDirectory()) {
            logger.info("退回到: " + ftp.printWorkingDirectory());
            dirPath = dirPath.replaceAll("\\\\", "/");
            boolean b = ftp.changeWorkingDirectory(getFileName(dirPath.substring(dirPath.lastIndexOf("/") + 1)));
            logger.info("进入: " + ftp.printWorkingDirectory());
            // 是一个空文件夹
            if (b && (ftpFiles == null || ftpFiles.length <= 0)) {
                zos.putNextEntry(new ZipEntry(getFileName(dirPath) + File.separator));
                return;
            }
        }

        for (FTPFile f : ftpFiles) {
            if (f.isFile()) {
                InputStream in = ftp.retrieveFileStream(f.getName());
                zos.putNextEntry(new ZipEntry(getFileName(dirPath) + File.separator + f.getName()));
                addToZOS(in, zos);
                ftp.completePendingCommand();
            } else {
                if (ftp.changeWorkingDirectory(f.getName())) {
                    loadFile(ftp, dirPath + File.separator + f.getName(), zos);
                    // ftp指针移动到上一层
                    ftp.changeToParentDirectory();
                }

            }
        }
    }

    /**
     * 将文件夹中的 .替换为^^
     * @param sb
     * @return
     */
    private static void replaceFileName(StringBuilder sb, String onepath) {
        sb.replace(sb.indexOf(onepath), sb.indexOf(onepath) + onepath.length(), onepath.replaceAll("\\.", "^^"));
    }
    /**
     * 将文件夹路径中的 ^^ 替换成 .
     * @param sb
     * @return
     */
    private static String getFileName(String sb) {
        StringBuilder result = new StringBuilder(sb);
        while (result.indexOf("^^") > -1) {
            result.replace(result.indexOf("^^"), result.indexOf("^^") + 2, ".");
        }
        return result.toString();
    }

    /**
     * 切换目录 返回切换的层级数
     * @param sb
     * @param ftp
     * @return 切换的层级数
     * @throws IOException
     */
    private static int exchageDir(StringBuilder sb, FTPClient ftp) throws IOException {
        // 计入进入的文件夹的次数，方便回退
        int level = 0;
        String[] pathes = sb.toString().split("/");
        for (int i = 0, len = pathes.length; i < len; i++) {
            String onepath = pathes[i];
            if (onepath == null || "".equals(onepath.trim())) {
                continue;
            }
            //文件排除
            boolean flagDir = ftp.changeWorkingDirectory(onepath);
            if (flagDir) {
                level++;
                if (onepath.contains(".")) {
                    //把文件夹中的.用^^代替
                    replaceFileName(sb, onepath);
                }
                logger.info("成功连接ftp目录：" + ftp.printWorkingDirectory());
            } else {
                logger.warn("连接ftp目录失败：" + ftp.printWorkingDirectory());
            }
        }

        return level;
    }


    /**
     * 处理单个文件和空文件夹的情况  处理了返回true 未处理返回false
     * 注意：这里是指数组中给的路径直接是文件或者空文件夹的情况
     * @param ftp
     * @param path 需要压缩的文件（文件夹）路径（相对根）
     * @param zos
     * @return
     * @throws IOException
     */
    public static boolean delFile(FTPClient ftp, StringBuilder path, ZipOutputStream zos) throws IOException {

        int times = exchageDir(path, ftp);

        String fileRelativePaht = path.toString();
        String[] split = fileRelativePaht.split("/");
        // 是否处理过
        boolean isDel = false;
        //有父级目录
        if (split != null && split.length > 0) {

            String lastSegmeng = split[split.length - 1];

            //path直接是文件
            if (split[split.length - 1].contains(".")) {
                zos.putNextEntry(new ZipEntry(fileRelativePaht));
                addToZOS(ftp.retrieveFileStream(lastSegmeng), zos);
                ftp.completePendingCommand();
                isDel = true;
            } else if (ftp.listFiles().length <= 0) {
                // 空文件夹
                zos.putNextEntry(new ZipEntry(fileRelativePaht + File.separator));
                isDel = true;
            }
            // 如果被处理过（空文件夹或者直接给的文件的路径），退回到根路径（以免污染下次循环的ftp指针）
            if (isDel) {
                for (int i = 0; i < times; i++) {
                    ftp.changeToParentDirectory();
                }
            }
        }
        return isDel;
    }

    /**
     * 添加文件到压缩流
     * @param in  输入流（一般就直接是从FTP中获取的）
     * @param zos 压缩输出流
     * @throws IOException
     */
    public static void addToZOS(InputStream in, ZipOutputStream zos) throws IOException {

        byte[] buf = new byte[1024];
        //BufferedOutputStream bzos = new BufferedOutputStream(zos);
        try {
            for (int len; (len = (in.read(buf))) != -1; ) {
                zos.write(buf, 0 , len);
            }
        } catch (IOException e) {
            System.out.println("流转换异常");
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("关流异常");
                    e.printStackTrace();
                }
            }
        }
    }


    public static FTPClient getFtpClient(String path) throws Exception {

        String server = "127.0.0.1";
        int port = 21;
        String username = "test";
        String password = "test";
//        path = "/FTPStation/";
        FTPClient ftpClient = connectServer(server, port, username, password, path);
        return ftpClient;
    }

    /**
     *
     * @param server
     * @param port
     * @param username
     * @param password
     * @param path 连接的节点（相对根路径的文件夹）
     * @return
     */
    public static FTPClient connectServer(String server, int port, String username, String password, String path) throws IOException {
        path = path == null ? "" : path;
        FTPClient ftp = new FTPClient();

        //下面四行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
        // 如果使用serv-u发布ftp站点，则需要勾掉“高级选项”中的“对所有已收发的路径和文件名使用UTF-8编码”
        ftp.setControlEncoding("GBK");
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");
        ftp.configure(conf);

        // 判断ftp是否存在
        ftp.connect(server, port);
        ftp.setDataTimeout(2 * 60 * 1000);
        if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            ftp.disconnect();
            System.out.println(server + "拒绝连接");
        }
        //登陆ftp
        boolean login = ftp.login(username, password);
        if (logger.isDebugEnabled()) {
            if (login) {
                logger.debug("登陆FTP成功! ip: " + server);
            } else {
                logger.debug("登陆FTP失败! ip: " + server);
            }
        }

        //根据输入的路径，切换工作目录。这样ftp端的路径就可以使用相对路径了
        exchageDir(new StringBuilder(path), ftp);

        return ftp;
    }

    public static void main(String[] args) throws Exception {

        String[] paths = {"/CAD/你好.txt", "/CAD/1.第一层"};
        String savePath = "e:/temp/zz.zip";
        //连接到的节点
        String rootDir = "/CAD";
        FTPClient ftp = getFtpClient(rootDir);
        zipFileByPaths(ftp, savePath, paths);
    }

    /**
     * 多FTP路径压缩
     * @param ftp
     * @param savePath
     * @param filePaths
     * @throws Exception
     */
    public static void zipFileByPaths(FTPClient ftp, String savePath, String[] filePaths) throws Exception {

        try (ZipOutputStream zos = ZipUtils.getOutPutStream(savePath)) {
            for (String path : filePaths) {
                StringBuilder sb = new StringBuilder(path);
                //delFile时进入的文件夹的路径（当前需要遍历的文件的路径）
                String before = ftp.printWorkingDirectory();
                if (delFile(ftp, sb, zos)) {
                    continue;
                }
                loadFile(ftp, sb.toString(), zos);
                // 文件加载完毕后，需要将ftp的指针退到开始状态
                String after = ftp.printWorkingDirectory();
                backPath(before, after, ftp);
            }
            zos.flush();
        } catch (Exception e) {
            logger.error("多路径文件压缩失败");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * ftp 指针回退
     * @param end
     * @param start
     * @param ftp
     * @throws IOException
     */
    private static void backPath(String end, String start, FTPClient ftp) throws IOException {
        long startTime = System.currentTimeMillis();
        do {
            if (!end.equals(start) && ftp.changeToParentDirectory()) {
                start = ftp.printWorkingDirectory();
            }
            // 防止意外出现空循环 30s等待时间
            if (System.currentTimeMillis() - startTime > 30 * 1000) {
                break;
            }
        } while (!end.equals(start));
    }
}
