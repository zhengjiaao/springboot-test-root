package com.dist.controller;

import com.dist.utils.response.ResponseData;
import com.dist.utils.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/1 10:57
 */
@RequestMapping(value = "rest/filedata")
@RestController
@Api(tags = {"FileDataController"}, description = "http大文件快速下载")
public class FileDataController extends BaseController {


    //待解决：分片给前端
    @ApiOperation(value = "下载离线数据包", httpMethod = "GET")
    @RequestMapping(value = "v1/offlinepackage/downloadOffLine", method = RequestMethod.GET)
    public ResponseData downloadOffLineData(@ApiParam(value = "数据包相对路径", required = true) @RequestParam("path") String path) throws Exception {
        path = path.replace("\\", "/").replace("//", "/");
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        byte[] buffer = null;
        OutputStream os = null;
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            this.response.setContentType("application/force-download");
            this.response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

            long begin = System.currentTimeMillis();

            //this.response.setContentLength(buffer.length);

            os = this.response.getOutputStream();
            downloadFile("D:\\FileTest\\移动电子底图.tpk", os);
            //os.write(buffer, 0, buffer.length);
            os.flush();

            long end = System.currentTimeMillis();

            System.out.println("用时= " + (end - begin));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseUtil.success(null);
    }

    public void downloadFile(String filePath, OutputStream os) throws Exception {
        byte[] buffer = null;
/*
        try (FileInputStream inputStream = new FileInputStream(filePath);) {
            buffer = new byte[(int) new File(filePath).length()];
            int count = 0;
            while ((count = inputStream.read(buffer)) != -1) {
                System.out.println("文件下载中。。。。");
                // 一次向数组中写入数组长度的字节
                os.write(buffer, 0, count);
                System.out.println("文件已下载");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/


        try(FileInputStream inputStream = new FileInputStream(filePath);) {
            buffer = new byte[(int)new File(filePath).length()];
            int count=0;
            while((count= inputStream.read(buffer))>0){
                // 一次向数组中写入数组长度的字节
                System.out.println("文件下载中。。。。");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //return buffer;
    }
}
