package com.dist.controller;

import com.dist.config.mongo.demo.UserDao;
import com.dist.util.exception.ResourceException;
import com.dist.pojo.FolderStructure;
import com.dist.util.MongoResourceStorageUtil;
import com.dist.utils.ContextPathUtil;
import com.dist.utils.IdUtil;
import com.dist.utils.file.FileUtil;
import com.dist.utils.file.FolderStructureUtil;
import com.dist.utils.zip.ZipDeCompressUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/12 11:02
 */
@RestController
@RequestMapping(value = "/mongo/v1")
@Api(tags = {"MongoController"},description = "mogondb双数据源测试")
public class MongoController {

    @Autowired
    UserDao userDao;

    @Value("${report_tmp_zip_dir}")
    private String localTemporaryZipDir;

    @Autowired
    MongoResourceStorageUtil mongoResourceStorageUtil;

    @ApiOperation(value = "保存数据到test1Mongo",httpMethod = "GET")
    @RequestMapping(value = "saveuser",method = RequestMethod.GET)
    public void saveUser(){
      userDao.saveUser();
    }

    @ApiOperation(value = "保存数据到test2Mongo",httpMethod = "GET")
    @RequestMapping(value = "saveuser2",method = RequestMethod.GET)
    public void saveUser2(){
        //userDao.saveUser2();
    }

    @ApiOperation(value = "上传文件到test1Mongo",httpMethod = "POST")
    @RequestMapping(value = "uploadfile",method = RequestMethod.POST)
    public Object uploadFile(HttpServletRequest request,
                            @ApiParam(value = "上传文件") @RequestParam("file") MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        // 文件暂存本地tomcat
        String tomcatPath = ContextPathUtil.getContextPath(localTemporaryZipDir, request);
        long currentTimeMillis = System.currentTimeMillis();
        String tomcatPathWithTimestamp = tomcatPath + File.separator + currentTimeMillis;
        File dirWithTimestamp = new File(tomcatPathWithTimestamp);
        if (!dirWithTimestamp.exists()) {
            dirWithTimestamp.mkdirs();
        }
        String temporarySavePath = tomcatPathWithTimestamp + File.separator + fileName;
        try {
            File temporarySaveFile = new File(temporarySavePath);
            multipartFile.transferTo(temporarySaveFile);
        } catch (IOException e) {
            System.out.println("文件暂存tomcat失败" + e);
            throw new RuntimeException();
        }
        // 解压zip文件并返回解压之后的路径
        String destDirPath = ZipDeCompressUtil.unZip(temporarySavePath);

        // 递归解析zip文件结构
        FolderStructure dirTree = FolderStructureUtil.getDirTree(destDirPath);

        // 将zip文件在mongo中的标识符
        String path = IdUtil.uuid6();

        // 将文件上传到mongo中
        if (!mongoResourceStorageUtil.uploadFile(temporarySavePath, path)) {
            throw new ResourceException("文件上传mongo失败！");
        }

        dirTree.setGuid(path);

        // 删除缓存的zip文件
        FileUtil.deleteFile(temporarySavePath);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("destDirPath",destDirPath);
        result.put("path",path);
        result.put("dirTree", dirTree);
        return result;
    }

    @ApiOperation(value = "下载文件test1Mongo",httpMethod = "GET")
    @RequestMapping(value = "downloadfile",method = RequestMethod.GET)
    public Object downloadFile(@ApiParam(value = "文件在mongo的id") @RequestParam String path,
                               @ApiParam(value = "文件下载地址") @RequestParam String localFilePath){
        String fileNameByPath = mongoResourceStorageUtil.getFileNameByPath(path);
        System.out.println("fileNameByPath="+fileNameByPath);
        Boolean downloadFile = mongoResourceStorageUtil.downloadFile(localFilePath, path);
        Map map = new HashMap();
        map.put("fileNameByPath",fileNameByPath);
        map.put("downloadFile",downloadFile);
        return map;
    }

    @ApiOperation(value = "删除文件test1Mongo",httpMethod = "GET")
    @RequestMapping(value = "deletefile",method = RequestMethod.GET)
    public Object deleteFile(@ApiParam(value = "文件在mongo的id") @RequestParam String path){
        Boolean deleteFile = mongoResourceStorageUtil.deleteFile(path);
        return deleteFile;
    }
}
