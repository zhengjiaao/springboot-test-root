/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 16:57
 * @Since:
 */
package com.zja.controller;

import com.zja.model.dto.UserDTO;
import com.zja.model.request.FileUploadRequest;
import com.zja.remote.SpringFileFeignClient;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;


@Api("Feign 文件上传")
@RestController
@RequestMapping("/feign/file/v2")
public class SpringFileFeignClientController {

    @Autowired
    private SpringFileFeignClient fileFeignClient;

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) {
        fileFeignClient.postFile(file);
        return true;
    }

    @PostMapping(value = "/post/upload/v2")
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files) {
        fileFeignClient.postFile(files);
        return true;
    }

    @PostMapping(value = "/post/upload/v3")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("新文件名称") @RequestParam String filename) {
        fileFeignClient.postFile(file, filename);
        return true;
    }

    @PostMapping(value = "/post/upload/v3/2")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    public Object postFileV3(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("新文件名称") @RequestParam String filename) {
        fileFeignClient.postFile(FileUploadRequest.builder().file(file).filename(filename).build());
        return true;
    }

    @PostMapping(value = "/post/upload/v4")
    @ApiOperation(value = "post-上传单文件和json对象", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("对象") @RequestBody UserDTO userDTO) {
        fileFeignClient.postFile(file, userDTO);
        return true;
    }

    @PostMapping(value = "/post/upload/v5")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("新文件名称") @RequestParam String filename) {
        fileFeignClient.postFile(files, filename);
        return true;
    }

    @PostMapping(value = "/post/upload/v6")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("对象") @RequestBody UserDTO userDTO) {
        fileFeignClient.postFile(files, userDTO);
        return true;
    }

    //下载文件

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    public String downloadfileURL(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam String filename) throws Exception {

        String url = fileFeignClient.downloadfileURL(filename);

        URL httpurl = new URL(url);
        String urlFileName = getFileNameFromUrl(url);
        System.out.println(urlFileName);
        File file = new File("D:\\picture\\temp\\" + urlFileName);
        FileUtils.copyURLToFile(httpurl, file);

        return url;
    }

    @GetMapping(value = "get/download/v2")
    @ApiOperation(value = "下载文件-文件流")
    public void downloadfileStream(HttpServletResponse response,
                                   @ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam String filename) throws Exception {
        // feign文件下载
        Response feignResponse = fileFeignClient.downloadfileStream(filename);
        Response.Body body = feignResponse.body();
        byte[] bytes = toByteArray(body.asInputStream());
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(filename, "UTF-8"));
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();
    }

    /**
     * InputStream 转换成byte[]
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 获取URL中的文件名称
     */
    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }
}
