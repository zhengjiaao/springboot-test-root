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
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import feign.form.FormData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * feign 常用 file 测试
 */
public interface FileFeignClient {

    //文件上传

    /**
     * consume为： MULTIPART_FORM_DATA_VALUE，表明只接收FormData这个类型的数据
     * 必须添加：consumes = MediaType.MULTIPART_FORM_DATA_VALUE
     * 错误信息：No serializer found for class sun.nio.ch.ChannelInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.LinkedHashMap["file"]->org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile["inputStream"])
     */
    @RequestLine("POST /post/upload/v1")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
//    Object postFile(@Param(value = "file") MultipartFile file); //不支持
//    String postFile(@Param("file") File file);
    Object postFile(@Param("file") FormData formData);

    @RequestLine("POST /post/upload/v2")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传多文件", notes = "返回 true")
//    Object postFile(@Param(value = "files") MultipartFile[] files); //不支持
//    Object postFile(@Param(value = "files") File[] files);
    String postFile(@Param("files") List<File> files);

    @RequestLine("POST /post/upload/v3")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传单文件和字符串", notes = "返回 true")
    Object postFile(@ApiParam("上传文件") @Param("file") FormData formData,
                    @ApiParam("新文件名称") @Param("filename") String filename);

    @RequestLine("POST /post/upload/v4")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传单文件和json对象", notes = "返回 true")
        //Body parameters cannot be used with form parameters
        //feign 不支持 @Param与@RequestBody 共同使用
    Object postFile(@ApiParam("上传文件") @Param(value = "file") MultipartFile file,
//                    @ApiParam("对象") @RequestBody UserDTO userDTO);
                    @ApiParam("对象") @Param("jsondata") UserDTO userDTO);

    @RequestLine("POST /post/upload/v5")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    Object postFile(@Param(value = "files") List<File> files,
                    @ApiParam("新文件名称") @Param("filename") String filename);

    @RequestLine("POST /post/upload/v6")
    @Headers("Content-Type: multipart/form-data")
    @ApiOperation(value = "post-上传多文件和字符串", notes = "返回 true")
    Object postFile(@Param(value = "files") List<File> files,
                    @ApiParam("对象") @Param("jsondata") UserDTO userDTO);


    //文件下载

    @RequestLine("GET /get/download/v1?filename={filename}")
    @ApiOperation(value = "下载文件-文件URL")
    String downloadfileURL(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @Param("filename") String filename);

    @RequestLine("GET /get/download/v2?filename={filename}")
    @ApiOperation(value = "下载文件-文件流")
    Response downloadfileStream(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @Param("filename") String filename);

}
