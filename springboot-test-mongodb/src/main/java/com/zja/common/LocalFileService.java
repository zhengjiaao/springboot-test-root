package com.zja.common;

import com.zja.utils.ContextPathUtil;
import com.zja.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地文件存储
 *
 *  @author yinxp@dist.com.cn
 */
@Slf4j
@Component
public class LocalFileService {

    @Value("${spring.tomcat_file_temp}")
    private String fileTemp;

    @Resource
    private MongoService mongoService;


    /**
     * 文件上传时，在web层就将图片存入tomcat目录下
     *
     * @param file
     * @return
     */
    public Map<String,String> save(String fileName, byte[] file, HttpServletRequest request) {

        String url = ContextPathUtil.getBaseURL(request)+ fileTemp + "/"+ fileName;
        String filePath = ContextPathUtil.getContextPath(fileTemp,request)+ File.separator + fileName;
        File existFile = new File(filePath);
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        result.put("path", filePath);
        if (existFile.exists()) {
            return result;
        }

        FileUtil.byte2File(file, filePath);
        return result;
    }

    /**
     * 将文件从mongo转移到本地
     * @param inFilePath
     * @param request
     */
    public String transfer(String inFilePath, HttpServletRequest request) {
        String url = ContextPathUtil.getBaseURL(request)+ fileTemp + "/"+ inFilePath;
        String filePath =  ContextPathUtil.getContextPath(fileTemp,request)+ File.separator + inFilePath;
        File existFile = new File(filePath);
        if (existFile.exists()) {
            return url;
        }

        String mongoPath = inFilePath.substring(0,inFilePath.indexOf("."));
        byte[] file = mongoService.getFileByPath(mongoPath);
        if (file == null || file.length < 1) {
            return null;
        }
        FileUtil.byte2File(file, filePath);
        return url;

    }

    /**
     * 删除指定文件名的资源
     * @param fileName
     * @param request
     * @return
     */
    public boolean deleteByName(String fileName,HttpServletRequest request) {
        String filePath = ContextPathUtil.getContextPath(fileTemp,request)+ File.separator + fileName;
        return deleteByPath(filePath);
    }


    /**
     * 删除指定路径下资源
     * @param filePath
     * @return
     */
    public boolean deleteByPath(String filePath) {
        File existFile = new File(filePath);
        if (existFile.exists() && existFile.isFile()) {
            try {
                existFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
