package com.zja.controller;

import com.zja.dto.FTPDirDTO;
import com.zja.dto.FtpCfgDTO;
import com.zja.util.FtpFileZipUtls;
import com.zja.util.FtpUtil;
import com.zja.util.ZipUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/15 9:41
 */
@Api(tags = {"FtpController"},description = "FTP测试")
@RestController
@RequestMapping(value = "/ftp")
public class FtpController extends BaseController {

    @Autowired
    @Qualifier("default")
    private FtpCfgDTO ftpCfgDTO;

    @Autowired
    @Qualifier("cadFtp")
    private FtpCfgDTO cadFtpCfgDTO;

    @Autowired
    @Qualifier("topicFtp")
    private FtpCfgDTO topicFtpCfgDTO;

    @ApiOperation(value = "获取default-ftp目录",httpMethod = "GET")
    @RequestMapping(value = "v1/directory",method = RequestMethod.GET)
    public Object ftpCfgDTO(@ApiParam(value = "相对路径") @RequestParam String relativeDir,
                       @ApiParam(value = "级别 0表示当前目录以及所有子目录内容，1表示获取一级目录内容") @RequestParam int level) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
        List<FTPDirDTO> ftpDirDTOS =null;
        try {
            ftpDirDTOS = FtpUtil.getSubDirectory(ftpClient, relativeDir, level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpDirDTOS;
    }

    @ApiOperation(value = "获取cadFtp-ftp目录",httpMethod = "GET")
    @RequestMapping(value = "v2/directory",method = RequestMethod.GET)
    public Object cadFtpCfgDTO(@ApiParam(value = "相对路径") @RequestParam String relativeDir,
                         @ApiParam(value = "级别 0表示当前目录以及所有子目录内容，1表示获取一级目录内容") @RequestParam int level) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(cadFtpCfgDTO);
        System.out.println("Directory="+ftpClient.printWorkingDirectory());
        List<FTPDirDTO> ftpDirDTOS =null;
        try {
            ftpDirDTOS = FtpUtil.getSubDirectory(ftpClient, relativeDir, level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpDirDTOS;
    }

    @ApiOperation(value = "获取topicFtp-ftp目录",httpMethod = "GET")
    @RequestMapping(value = "v3/directory",method = RequestMethod.GET)
    public Object topicFtpCfgDTO(@ApiParam(value = "相对路径") @RequestParam String relativeDir,
                         @ApiParam(value = "级别 0表示当前目录以及所有子目录内容，1表示获取一级目录内容") @RequestParam int level) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(topicFtpCfgDTO);
        List<FTPDirDTO> ftpDirDTOS =null;
        try {
            ftpDirDTOS = FtpUtil.getSubDirectory(ftpClient, relativeDir, level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpDirDTOS;
    }

    @ApiOperation(value = "上传文件到default-ftp",httpMethod = "GET")
    @RequestMapping(value = "v1/uploadfile",method = RequestMethod.GET)
    public Object uploadFileFtpCfgDTO(@ApiParam(value = "上传的文件,包含目录的文件名") @RequestParam String filePathName,
                                 @ApiParam(value = "新的文件名") @RequestParam String newName) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
        boolean uploadFile = FtpUtil.uploadFile(ftpClient, filePathName, newName);
        return uploadFile;
    }

    @ApiOperation(value = "从default-ftp下载文件",httpMethod = "GET")
    @RequestMapping(value = "v1/downloadfile",method = RequestMethod.GET)
    public Object downloadFileFtpCfgDTO(@ApiParam(value = "远程文件名，路径开头不能以“/”或者“\\”开始") @RequestParam String remoteFilePath,
                                 @ApiParam(value = "本地文件") @RequestParam String localFileName) throws IOException {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
        boolean downloadFile = FtpUtil.download(ftpClient, remoteFilePath, localFileName);
        return downloadFile;
    }

    @ApiOperation(value = "从default-ftp下载多个文件",httpMethod = "GET")
    @RequestMapping(value = "v1/multiple/downloadfile",method = RequestMethod.GET)
    public Object downloadMultipleFileFtpCfgDTO() {

        String jpg = "CAD\\1.jpg,CAD\\2.jpg";
        String[] split = jpg.split(",");
        boolean downloadFile = false;
        for (String path : split){
            FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
            String localFileName ="D:\\doc\\"+path;
            downloadFile = FtpUtil.downloadFileFromFtp(ftpClient, path, localFileName);
        }
        return downloadFile;
    }

    @ApiOperation(value = "从default-ftp下载文件夹下所有的内容",httpMethod = "GET")
    @RequestMapping(value = "v1/download/folder",method = RequestMethod.GET)
    public void downloadFolder(@ApiParam(value = "远程文件夹名，路径开头不能以“/”或者“\\”开始") @RequestParam String remotePath,
                                 @ApiParam(value = "本地文件夹") @RequestParam String localPath) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
        FtpUtil.downFtpFiles(ftpClient,remotePath,localPath);
        FtpUtil.closeServer(ftpClient);
    }

    @ApiOperation(value = "从default-ftp压缩图片下载-将多个文件压缩",notes = "支持目录和文件同时下载",httpMethod = "GET")
    @RequestMapping(value = "v1/getZipFile",method = RequestMethod.GET)
    public Object getZipFile(@ApiParam(value = "文件路径:多个以 ',' 分割", required = true) @RequestParam(value = "filePaths") String[] filePaths)
            throws Exception {

        String fileId = UUID.randomUUID().toString();
        String httpPath = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/";

        String dirPath = request.getServletContext().getRealPath("/") + "ftpzip";
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        //文件服务器存储路径
        String zipFilePath = file.getAbsolutePath() + File.separator + fileId + ".zip";

        //返回给前端的压缩文件路径
        String returnPath = httpPath + "ftpzip/" + fileId + ".zip";

        List<String> fileNames = Lists.newArrayListWithCapacity(filePaths.length);
        List<InputStream> collect = Arrays.stream(filePaths).map(name -> {
            try {
                FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
                fileNames.add(StringUtils.substring(name, StringUtils.lastIndexOf(name, "/") + 1));
                return FtpUtil.downFile(ftpClient, name);
            } catch (IOException e) {
                throw new IllegalArgumentException("文件【" + name + "】不存在");
            }
        }).collect(Collectors.toList());

        //压缩文件集合
        ZipUtils.zipByInStream(zipFilePath, collect, fileNames);

        return returnPath;
    }

    @ApiOperation(value = "从default-ftp压缩FTP上指定文件或者文件夹并返回下载地址",httpMethod = "POST")
    @RequestMapping(value = "v1/getZipFileByPath",method = RequestMethod.POST)
    public Object getZipFileByPath(@ApiParam(value = "文件路径,参数传递例如['/CAD/1.jpg','/CAD/图片']", required = true) @RequestBody String[] filePaths)
            throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
        FtpUtil.changeMutiWorkingDirectory(ftpClient,"/CAD");
        String savePath = getSavePath("ftpzip");
        FtpFileZipUtls.zipFileByPaths(ftpClient, savePath, filePaths);

        String httpPath = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/";
        String returnPath = httpPath + "ftpzip/" + savePath.substring(savePath.lastIndexOf(File.separator) + 1);

        logger.info("文件保存路径：" + savePath);
        return returnPath;
    }

    /**
     * 获取压缩文件的路径
     * @param zipDirName 生成的压缩文件的保存文件夹名
     * @return
     */
    private String getSavePath(String zipDirName) {
        String fileId = UUID.randomUUID().toString();

        String dirPath = request.getServletContext().getRealPath("/") + zipDirName;
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        String zipFilePath = file.getAbsolutePath() + File.separator + fileId + ".zip";
        return zipFilePath;
    }


    @ApiOperation(value = "上传,断点续传到default-ftp-待完善",notes = "上传文件到FTP服务器，支持断点续传",httpMethod = "GET")
    @RequestMapping(value = "v1/uploadBreakpoints",method = RequestMethod.GET)
    public Object uploadBreakpointsFtpCfgDTO(@ApiParam(value = "本地文件名称，绝对路径") @RequestParam String localFile,
                                      @ApiParam(value = "远程文件路径,带文件后缀") @RequestParam String remoteFile) throws Exception {
        FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
       // FtpUtil.UploadStatus uploadStatus = FtpUtil.uploadBreakpoints(ftpClient, localFile, remoteFile);
        return null;
    }


}
