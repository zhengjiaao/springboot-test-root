/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-21 14:16
 * @Since:
 */
package com.zja.web;

import com.zja.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * http://localhost:19000/swagger-ui/index.html#/
 * https://localhost:19001/swagger-ui/index.html#/
 */
@CrossOrigin
@Api(value = "提供远程-文件上传测试接口")
@RestController
@RequestMapping
public class WebFileController {

    // 上传文件

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        return true;
    }

    @PostMapping(value = "/post/upload/v2")
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files) {
        if (files.length <= 0) {
            return "未选择要上传的文件！";
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        return true;
    }

    @PostMapping(value = "/post/upload/v3")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("新文件名称") @RequestParam String filename) {
        System.out.println("上传文件：" + filename + "  大小：" + file.getSize());
        return true;
    }

    @Deprecated // MultipartFile multipart/form-data 优先级高于 @RequestBody application/json，去掉 @RequestBody 就好
    @PostMapping(value = "/post/upload/v4")
    @ApiOperation(value = "post-上传单文件和json对象", notes = "返回 true")
    public Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                           @ApiParam("对象") /*@RequestBody*/ UserDTO userDTO) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        System.out.println(userDTO.toString());
        return true;
    }

    @PostMapping(value = "/post/upload/v5")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("新文件名称") @RequestParam String filename) {
        if (files.length <= 0) {
            return "未选择要上传的文件！";
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        System.out.println(filename);
        return true;
    }

    @PostMapping(value = "/post/upload/v6")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    public Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                           @ApiParam("对象") UserDTO userDTO) {
        if (files.length <= 0) {
            return "未选择要上传的文件！";
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        System.out.println(userDTO.toString());
        return true;
    }


    // 下载文件

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    public String downloadfile(@ApiParam(value = "filename", defaultValue = "test.jpg") @RequestParam String filename,
                               HttpServletRequest request) {
        String urlContextPath = getUrlContextPath(request);
        String fileUrl = urlContextPath + "/file/" + filename;
        return fileUrl;
    }

    @GetMapping(value = "get/download/v2")
    @ApiOperation(value = "下载文件-文件流")
    public void downloadFileByGet(HttpServletResponse response,
                                  @ApiParam(value = "filename", defaultValue = "test.jpg") @RequestParam String filename) throws IOException {
        downloadFile(response, filename);
    }

    @PostMapping(value = "post/download/v1")
    @ApiOperation(value = "下载文件-文件流")
    public void downloadFileByPost(HttpServletResponse response,
                                   @ApiParam(value = "filename", defaultValue = "test.jpg") @RequestParam String filename) throws IOException {
        downloadFile(response, filename);
    }

    @GetMapping("get/download/stream/v1")
    @ApiOperation(value = "下载大文件-采用文件流", notes = "确保文件在下载完成后被删除，以避免临时文件的累积和资源浪费")
    public ResponseEntity<StreamingResponseBody> downloadFileStreamV1(String filePath) {
        // 获取文件对象
        File downloadFile = new File(filePath);
        // 检查文件是否存在
        if (!downloadFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 设置响应头，指定文件名等信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(downloadFile.getName()).build());

        // 设置文件长度
        long fileSize = downloadFile.length();
        headers.setContentLength(fileSize);

        // 创建StreamingResponseBody + InputStream 减少内存占用
        StreamingResponseBody responseBody = response -> {
            try (InputStream inputStream = Files.newInputStream(downloadFile.toPath())) {
                byte[] buffer = new byte[4 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.write(buffer, 0, bytesRead);
                }
            } finally {
                // 下载完成后删除文件
                downloadFile.delete();
            }
        };

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    @GetMapping("get/download/stream/v2")
    @ApiOperation(value = "下载大文件-文件流-采用通道", notes = "确保文件在下载完成后被删除，以避免临时文件的累积和资源浪费")
    public ResponseEntity<StreamingResponseBody> downloadFileStreamV2(String filePath) {
        // 获取文件对象
        File downloadFile = new File(filePath);
        // 检查文件是否存在
        if (!downloadFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 设置响应头，指定文件名等信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(downloadFile.getName()).build());

        // 设置文件长度
        long fileSize = downloadFile.length();
        headers.setContentLength(fileSize);

        // 创建StreamingResponseBody + FileChannel 减少内存占用
        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream fis = new FileInputStream(downloadFile)) {
                FileChannel fileChannel = fis.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (fileChannel.read(buffer) != -1) {
                    buffer.flip();
                    outputStream.write(buffer.array(), 0, buffer.limit());
                    buffer.clear();
                }
            } finally {
                // 下载完成后删除文件
                downloadFile.delete();
            }
        };

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    public void downloadFile(HttpServletResponse response, String filename) throws IOException {

        // 打成jar时，无法读取流
        /*File file = null;
        try {
            file=ResourceUtils.getFile("classpath:file/"+filename);
        }catch (Exception e){
            throw new RuntimeException("资源文件不存在！");
        }*/

        // 支持读取jar中的文件流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file/" + filename);

        assert inputStream != null;

        byte[] bytes = toByteArray(inputStream);
//        byte[] bytes = toByteArray(new FileInputStream(file));
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
     * 获取URL路径
     *
     * @return http://localhost:19000 或 http://localhost:19000/web
     */
    private String getUrlContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
