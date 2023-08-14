package com.zja.controller;

import com.zja.dto.UserDTO;
import com.zja.remote.FileFeignClient;
import feign.Response;
import feign.form.FormData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/fegin/file")
@Api(tags = {"FileFeginController"})
public class FileFeginController {

    @Autowired
    FileFeignClient fileFeignClient;

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) throws IOException {

        //File
//        File destFile = new File(file.getOriginalFilename());
//        FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
//        return fileFeignClient.postFile(destFile);

        //FormData
        FormData formData = new FormData(file.getContentType(), file.getOriginalFilename(), IOUtils.toByteArray(file.getInputStream()));
        return fileFeignClient.postFile(formData);
    }

    @PostMapping(value = "/post/upload/v2")
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files) throws IOException {

      /*  File[] filesFormData = new File[files.length];
        int i = 0;
        for (MultipartFile file : files) {
            File destFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            filesFormData[i] = destFile;
            i++;
        }
        return fileFeignClient.postFile(filesFormData);*/

        List<File> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            File destFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            fileList.add(destFile);
        }

        return fileFeignClient.postFile(fileList);
    }

    @PostMapping(value = "/post/upload/v3")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("新文件名称") @RequestParam String filename) throws IOException {
        FormData formData = new FormData(file.getContentType(), file.getOriginalFilename(), IOUtils.toByteArray(file.getInputStream()));
        return fileFeignClient.postFile(formData, filename);
    }

    //不可以
    /*@Deprecated //MultipartFile multipart/form-data 优先级高于 @RequestBody application/json，去掉 @RequestBody 就好
    @PostMapping(value = "/post/upload/v4")
    @ApiOperation(value = "post-上传单文件和json对象", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("对象") @RequestBody UserDTO userDTO) throws IOException {
    }*/

    @PostMapping(value = "/post/upload/v5")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("新文件名称") @RequestParam String filename) throws IOException {

        List<File> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            File destFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            fileList.add(destFile);
        }

        return fileFeignClient.postFile(fileList, filename);
    }

    ////MultipartFile multipart/form-data 优先级高于 @RequestBody application/json，去掉 @RequestBody 就好
    @PostMapping(value = "/post/upload/v6")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("对象")/* @RequestBody*/ UserDTO userDTO) throws IOException {

        List<File> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            File destFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            fileList.add(destFile);
        }

        return fileFeignClient.postFile(fileList, userDTO);
    }

    //下载文件

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    public String downloadfileURL(@ApiParam(value = "filename", defaultValue = "jpg.jpg") @RequestParam String filename) throws Exception {

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
                                   @ApiParam(value = "filename", defaultValue = "jpg.jpg") @RequestParam String filename) throws Exception {

        Response stream = fileFeignClient.downloadfileStream(filename);
        InputStream inputStream = stream.body().asInputStream();
        byte[] bytes = toByteArray(inputStream);

        // feign文件下载
//        Response feignResponse = fileFeignClient.downloadfileStream(filename);
//        Response.Body body = feignResponse.body();
//        byte[] bytes = toByteArray(body.asInputStream());
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
