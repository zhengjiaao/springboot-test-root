package com.zja.common;

import com.alibaba.fastjson.JSONObject;
import com.zja.utils.http.HttpUtil;
import com.zja.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 将文件存储在远程tomcat
 *
 *  @author yinxp@dist.com.cn
 */
@Slf4j
@Component
public class RemoteTomcatService {

    @Value("${spring.file_server_url}")
    private String fileServerUrl;

    /**
     *  上传文件到文件服务器
     * @param multipartFile
     * @param fileName
     * @param suffix
     * @return
     */
    public boolean transter(MultipartFile multipartFile, String fileName, String suffix) {
        String path = fileName + "." + suffix;
        Map<String, Object> params = new HashMap<>();
        params.put("fileName", path);
        try {
            String response =  HttpUtil.HttpMultipartFile(fileServerUrl, multipartFile, params);
            JSONObject json = JSONObject.parseObject(response);
            if (Result.RESULT_STATUS_SUCCESS.equals(json.get("status"))) {
                return true;
            }
        } catch (IOException e) {
           log.error("向远程tomcat上传文件失败：",e);
        }
        return false;
    }

    /**
     * 删除远程tomcat上的资源
     * @param fileName
     * @param fileSuffix
     */
    public void deleteRemote(String fileName,String fileSuffix) {
        Map<String, String> params = new HashMap<>();
        params.put("fileName", fileName);
        params.put("fileSuffix", fileSuffix);
        try {
            String response =  HttpUtil.doDelete(fileServerUrl,params);
            JSONObject json = JSONObject.parseObject(response);
        } catch (IOException e) {
            log.error("从远程tomcat删除文件失败：",e);
        }
    }

}
