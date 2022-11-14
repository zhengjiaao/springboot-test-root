package com.zja.controller;

import com.zja.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "rest/template/file")
@Api(tags = {"RestTemplateFileController"}, description = "RestTemplate 调用远程REST接口")
public class RestTemplateFileController {

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) throws IOException {

        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                return file.getSize();
            }
        };

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("file", fileAsResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject("/post/upload/v1", files, Boolean.class);
    }

    @PostMapping(value = "/post/upload/v2")
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files) throws IOException {

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }

                @Override
                public long contentLength() {
                    return file.getSize();
                }
            };

            multipartRequest.add("files", fileAsResource);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntityFiles = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject("/post/upload/v2", httpEntityFiles, Boolean.class);
    }

    @PostMapping(value = "/post/upload/v3")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("新文件名称") @RequestParam String filename) throws IOException {
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                return file.getSize();
            }
        };

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("file", fileAsResource);
        multipartRequest.add("filename", filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject("/post/upload/v3", files, Boolean.class);
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
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }

                @Override
                public long contentLength() {
                    return file.getSize();
                }
            };

            multipartRequest.add("files", fileAsResource);
        }

        multipartRequest.add("filename", filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntityFiles = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject("/post/upload/v5", httpEntityFiles, Boolean.class);
    }

    ////MultipartFile multipart/form-data 优先级高于 @RequestBody application/json，去掉 @RequestBody 就好
    @PostMapping(value = "/post/upload/v6")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("对象")/* @RequestBody*/ UserDTO userDTO) throws IOException {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }

                @Override
                public long contentLength() {
                    return file.getSize();
                }
            };

            multipartRequest.add("files", fileAsResource);
        }

        multipartRequest.add("id", userDTO.getId());
        multipartRequest.add("name", userDTO.getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntityFiles = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject("/post/upload/v6", httpEntityFiles, Boolean.class);
    }

    //下载文件

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    public String downloadfileURL(@ApiParam(value = "filename", defaultValue = "jpg.jpg") @RequestParam String filename) throws Exception {

        String url = restTemplate.getForObject("/get/download/v1?filename=" + filename, String.class);

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

        byte[] bytes = restTemplate.getForObject("/get/download/v2?filename=" + filename, byte[].class);

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
