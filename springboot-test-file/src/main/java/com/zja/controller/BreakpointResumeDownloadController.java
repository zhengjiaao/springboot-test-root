package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLEncoder;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-04-24 16:11
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：断点续传下载
 */
@RequestMapping("rest/v1")
@RestController
@Api(tags = {"BreakpointResumeDownloadController"}, description = "断点续传下载")
public class BreakpointResumeDownloadController {

    @ApiOperation(value = "提供断点续传下载离线包文件接口", notes = "将Range存到Header中，例如 Range=0-5, 0是起始位置，5是结束位置", httpMethod = "GET")
    @RequestMapping(value = {"v1/offlinepackage/breakpoint/resume/download"}, method = {RequestMethod.GET})
    public void provideBreakpointResumeDownloadFile(@ApiParam(value = "离线包名称", required = true) @RequestParam("offlinePackageName") String offlinePackageName,
                                                    HttpServletResponse response,
                                                    HttpServletRequest request) throws Exception {

        //String filePath = this.offlinePackageFile + offlinePackageName;
        String filePath= "c:"+File.separator+offlinePackageName;
        File file = new File(filePath);
        if (!file.exists()) {
            //throw new FileDownloadException("文件路径不存在：" + filePath);
            System.out.println("文件路径不存在：" + filePath);
        }
        if (file.isDirectory()) {
            //throw new IllegalParameterException("不能传文件夹路径, 请传文件路径！");
            System.out.println("不能传文件夹路径, 请传文件路径！");
        }

        //Range=0-5，Range=6-10
        String range = request.getHeader("Range");

        String startPosition = range.substring(0, range.lastIndexOf("-"));
        String endPosition = range.substring(range.lastIndexOf("-") + 1);
        int byteSize = this.getByteSize(startPosition, endPosition);

        //获取多少字节
        byte[] buffer = new byte[byteSize];
        RandomAccessFile ras = new RandomAccessFile(file, "r");
        ras.seek(Integer.valueOf(startPosition).intValue());
        ras.read(buffer);
        ras.close();

        String fileName = offlinePackageName.substring(offlinePackageName.lastIndexOf("/") + 1);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(fileName, "UTF-8"));
        response.setContentLength(buffer.length);
        ServletOutputStream outputStream = response.getOutputStream();
        //log.info(fileName + " - buffer.length: " + buffer.length);
        System.out.println(fileName + " - buffer.length: " + buffer.length);
        outputStream.write(buffer);
        outputStream.close();
        //log.info("离线包下载成功：-" + fileName);
        System.out.println("离线包下载成功：-" + fileName);
    }

    /**
     * 获取需要截取的Byte大小
     *
     * @param startPosition 开始指针位置
     * @param endPosition   结束指针位置
     * @return 截取ByteSize 大小
     */
    private int getByteSize(String startPosition, String endPosition) {
        int a = Integer.valueOf(startPosition).intValue();
        int b = Integer.valueOf(endPosition).intValue();
        return b - a;
    }

}
