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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * http://localhost:19000/swagger-ui/index.html#/
 * https://localhost:19001/swagger-ui/index.html#/
 */
@CrossOrigin
@Api(value = "提供远程-文件上传测试接口")
@RestController
@RequestMapping
public class WebFileController {

    //上传文件

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

    @Deprecated //MultipartFile multipart/form-data 优先级高于 @RequestBody application/json，去掉 @RequestBody 就好
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


    //下载文件

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    public String downloadfile(@ApiParam(value = "filename", defaultValue = "jpg.jpg") @RequestParam String filename,
                               HttpServletRequest request) {
        String urlContextPath = getUrlContextPath(request);
        String fileUrl= urlContextPath+"/file/"+filename;
        return fileUrl;
    }

    @GetMapping(value = "get/download/v2")
    @ApiOperation(value = "下载文件-文件流")
    public void downloadfile(HttpServletResponse response,
                             @ApiParam(value = "filename", defaultValue = "jpg.jpg") @RequestParam String filename) throws IOException {

        //打成jar时，无法读取流
        /*File file = null;
        try {
            file=ResourceUtils.getFile("classpath:file/"+filename);
        }catch (Exception e){
            throw new RuntimeException("资源文件不存在！");
        }*/

        //支持读取jar中的文件流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file/"+filename);

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
     * @return http://localhost:19000 或 http://localhost:19000/web
     */
    private String getUrlContextPath(HttpServletRequest request){
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
