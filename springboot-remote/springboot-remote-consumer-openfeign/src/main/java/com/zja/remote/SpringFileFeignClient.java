/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 16:55
 * @Since:
 */
package com.zja.remote;

import com.zja.dto.UserDTO;
import feign.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * feign 常用 file 测试
 */
public interface SpringFileFeignClient {

    //文件上传

    /**
     * consume为： MULTIPART_FORM_DATA_VALUE，表明只接收FormData这个类型的数据
     * 必须添加：consumes = MediaType.MULTIPART_FORM_DATA_VALUE
     * 错误信息：No serializer found for class sun.nio.ch.ChannelInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.LinkedHashMap["file"]->org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile["inputStream"])
     */
    @PostMapping(value = "/post/upload/v1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file);

    @PostMapping(value = "/post/upload/v2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
    Object postFile(@RequestPart(value = "files") MultipartFile[] files);

    @PostMapping(value = "/post/upload/v3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
                    //RequestParam.value() was empty on parameter 1
//                    @ApiParam("新文件名称") @RequestParam String filename);
                    @ApiParam("新文件名称") @RequestParam("filename") String filename);

    @PostMapping(value = "/post/upload/v4", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传单文件和json对象", notes = "返回 true")
        //Body parameters cannot be used with form parameters
        //feign 不支持 @RequestPart与@RequestBody 共同使用
    Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file,
//                    @ApiParam("对象") @RequestBody UserDTO userDTO);
                    @ApiParam("对象") @RequestPart("jsondata") UserDTO userDTO);

    @PostMapping(value = "/post/upload/v5", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                    @ApiParam("新文件名称") @RequestParam("filename") String filename);

    @PostMapping(value = "/post/upload/v6", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    Object postFile(@RequestPart(value = "files") MultipartFile[] files,
                    @ApiParam("对象") @RequestPart("jsondata") UserDTO userDTO);


    //文件下载

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    String downloadfileURL(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam("filename") String filename);

    @GetMapping(value = "get/download/v2")
    @ApiOperation(value = "下载文件-文件流")
    Response downloadfileStream(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam("filename") String filename);

}
