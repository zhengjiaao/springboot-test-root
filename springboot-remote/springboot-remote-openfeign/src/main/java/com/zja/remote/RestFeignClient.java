/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 16:52
 * @Since:
 */
package com.zja.remote;

import com.zja.model.dto.UserDTO;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import io.swagger.annotations.ApiOperation;

/**
 * feign 常用 rest api 测试
 * get、post、put、delete
 *
 * 注意：远程返回值，如果是字符串，则必须使用String接收，否则异常
 * 若返回的是对象、map、list等，则可以以Object接收
 */
public interface RestFeignClient {

    //get

    @RequestLine("GET /get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    String get();   // 注意：Fegin使用json解码器后，若返回String类型，使用String或Object类型接受数据都会报错无法解析

    @RequestLine("GET /get/{path}")
    @ApiOperation(value = "get-路径参数")
    String getPath(@Param("path") String path);

    @RequestLine("GET /get/param/v1?param={param}")
    @ApiOperation(value = "get-拼接参数")
    String getParam1(@Param("param") String param);

    @RequestLine("GET /get/param/v2")
    @ApiOperation(value = "get-对象参数", notes = "返回对象")
    String getParam2(@QueryMap UserDTO userDto);

    @RequestLine("GET /get/object/v1")
    @ApiOperation(value = "get-无参数", notes = "返回对象")
    UserDTO getObject(); //注意 Object或UserDTO对象 必须要注册Fegin的json解码器，不配置报错：class com.zja.dto.UserDTO is not a type supported by this decoder

    @RequestLine("GET get/object/list/v1")
    @ApiOperation(value = "get-无参数", notes = "返回List")
    Object getObjectList();

    //post

    @RequestLine("POST /post")
    @ApiOperation(value = "post-无参数", notes = "返回字符串")
    String post();

    @RequestLine("POST /post/param?name={name}&age={age}")
    @ApiOperation(value = "post-多个参数", notes = "返回字符串")
    String postObject(@Param("name") String name,
                      @Param("age") String age);

    @RequestLine("POST /post/object/v1")
    @ApiOperation(value = "post-无参数", notes = "返回对象")
    Object postObject();

    @RequestLine("POST /post/object/v2")
    @Headers("Content-Type: application/json")
    @ApiOperation(value = "post-对象参数", notes = "返回对象")
    Object postObject(UserDTO userDto);

    //put

    @RequestLine("PUT /put")
    @ApiOperation(value = "put-无参数", notes = "返回字符串")
    String put();

    @RequestLine("PUT /put/path/{path}")
    @ApiOperation(value = "put-参数", notes = "返回字符串")
    String putPath(@Param("path") String path);

    @RequestLine("PUT /put/param?param={param}")
    @ApiOperation(value = "put-参数", notes = "返回字符串")
    String putParam(@Param("param") String param);

    @RequestLine("PUT /put/object/v1")
    @ApiOperation(value = "put-无参数", notes = "返回对象")
    UserDTO putObject();

    @RequestLine("PUT /put/object/v2")
    @Headers("Content-Type: application/json")
    @ApiOperation(value = "put-对象参数", notes = "返回对象")
    UserDTO putObject(UserDTO userDto);

    //delete

    @RequestLine("DELETE /delete")
    @ApiOperation(value = "delete-无参数", notes = "返回字符串")
    String delete();

    @RequestLine("DELETE /delete/path/{path}")
    @ApiOperation(value = "delete-路径参数")
    String deletePath1(@Param("path") String path);

    @RequestLine("DELETE /delete/param?param={param}")
    @ApiOperation(value = "delete-拼接参数")
    String deletePath2(@Param("param") String param);

    @RequestLine("DELETE /delete/object/v1")
    @ApiOperation(value = "delete-无参数", notes = "返回对象")
    Object deleteObject();

}
