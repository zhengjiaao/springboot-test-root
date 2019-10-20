package com.dist.controller;

import com.dist.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/12 13:50
 */
@Api(tags = {"UploadFileController"}, description = "普通文件上传")
@RestController
@RequestMapping(value = "rest/file")
public class UploadFileController {

    @Value("${rest.resource}")
    private String localTemporaryDir;

    @ApiOperation(value = "单文件上传", httpMethod = "POST")
    @PostMapping(value = "/v1/upload/singleFile")
    public Object uploadSingleFile(HttpServletRequest request,
                                       @ApiParam(value = "选择单个文件") @RequestParam("file") MultipartFile multipartFile){

        String fileName = multipartFile.getOriginalFilename();
        // 文件暂存本地tomcat
        String tomcatPath = getContextPath(localTemporaryDir, request);
        long currentTimeMillis = System.currentTimeMillis();
        String tomcatPathWithTimestamp = tomcatPath + File.separator + currentTimeMillis;
        FileUtil.createDirs(tomcatPathWithTimestamp);
        String temporarySavePath = tomcatPathWithTimestamp + File.separator + fileName;
        try {
            File temporarySaveFile = new File(temporarySavePath);
            multipartFile.transferTo(temporarySaveFile);
        } catch (IOException e) {
            System.out.println("文件暂存tomcat失败" + e);
            throw new RuntimeException();
        }
        System.out.println("上传成功："+temporarySavePath);
        return temporarySavePath;
    }

    @ApiOperation(value = "多文件上传", notes = "多文件上传swagger测试不了，使用Postman测试",httpMethod = "POST")
    @PostMapping(value = "/v1/upload/multipleFiles")
    public Object uploadMultipleFiles(HttpServletRequest request,
                                 @ApiParam(value = "可选着多个文件") @RequestParam("file") MultipartFile[] multipartFiles){
        if (multipartFiles == null || multipartFiles.length == 0) {
            return ("请选择要上传的文件！");
        }

        // 文件暂存本地tomcat
        String tomcatPath = getContextPath(localTemporaryDir, request);
        String path = uuid6();
        String tomcatPathUUid6 = tomcatPath+File.separator+path;
        FileUtil.createDirs(tomcatPathUUid6);
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String temporarySavePath = tomcatPathUUid6 + File.separator + fileName;
            try {
                File temporarySaveFile = new File(temporarySavePath);
                multipartFile.transferTo(temporarySaveFile);
            } catch (IOException e) {
                System.out.println("文件暂存tomcat失败" + e);
                throw new RuntimeException();
            }
            filePaths.add(temporarySavePath);
        }

        System.out.println("上传成功："+filePaths);
        return filePaths;
    }


    /**
     * 获取上下文物理路径
     * @param relativePath
     * @return
     */
    public static String getContextPath(String relativePath,HttpServletRequest request){

        if (null == request) {
            return null;
        }
        String path = request.getServletContext().getRealPath(relativePath);
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 随机产生6位数字
     * @return
     */
    public static String uuid6() {
        int randNum = 1 + (int)(Math.random() * ((999999 - 1) + 1));
        return String.valueOf(randNum);
    }

}
