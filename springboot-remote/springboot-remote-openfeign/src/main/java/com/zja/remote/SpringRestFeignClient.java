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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * feign 常用 rest api 测试
 * get、post、put、delete
 *
 * 注意：远程返回值，如果是字符串，则必须使用String接收，否则异常
 * 若返回的是对象、map、list等，则可以以Object接收
 */
public interface SpringRestFeignClient {

    //get

    @GetMapping(value = "/get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    String get();
//    Object get();  // Object 会遇到错误：Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.Object] and content type [text/plain;charset=UTF-8]

    @GetMapping(value = "/get/{path}")
    @ApiOperation(value = "get-路径参数")
    String getPath(@PathVariable("path") String path);

    @GetMapping(value = "/get/param/v1")
    @ApiOperation(value = "get-拼接参数")
    String getParam1(@RequestParam("path") String path);

    @GetMapping(value = "/get/param/v2")
    @ApiOperation(value = "get-对象参数", notes = "返回对象")
    String getParam2(UserDTO userDto);

    // cloud openfeign 支持设置 @SpringQueryMap
    // @GetMapping(value = "/get/param/v2")
    // @ApiOperation(value = "get-对象参数", notes = "返回对象")
    // String getParam3(@SpringQueryMap UserDTO userDto);

    @GetMapping(value = "/get/object/v1")
    @ApiOperation(value = "get-无参数", notes = "返回对象")
    Object getObject();

    @GetMapping(value = "/get/object/list/v1")
    @ApiOperation(value = "get-无参数", notes = "返回List")
    Object getObjectList();

    //post

    @PostMapping(value = "/post")
    @ApiOperation(value = "post-无参数", notes = "返回字符串")
    String post();

    @PostMapping(value = "/post/param")
    @ApiOperation(value = "post-多个参数", notes = "返回字符串")
    Object postObject(@ApiParam(value = "传参值：name") @RequestParam("name") String name,
                      @ApiParam(value = "传参值：age") @RequestParam("age") String age);

    @PostMapping(value = "/post/object/v1")
    @ApiOperation(value = "post-无参数", notes = "返回对象")
    Object postObject();

    @PostMapping(value = "/post/object/v2")
    @ApiOperation(value = "post-对象参数", notes = "返回对象")
    Object postObject(@RequestBody UserDTO userDto);

    //put

    @PutMapping(value = "/put")
    @ApiOperation(value = "put-无参数", notes = "返回字符串")
    String put();

    @PutMapping(value = "/put/path")
    @ApiOperation(value = "put-参数", notes = "返回字符串")
    String putPath(@RequestParam("path") String path);

    @PutMapping(value = "/put/param")
    @ApiOperation(value = "put-参数", notes = "返回字符串")
    String putParam(@RequestParam("name") String name);

    @PutMapping(value = "/put/object/v1")
    @ApiOperation(value = "put-无参数", notes = "返回对象")
    UserDTO putObject();

    @PutMapping(value = "/put/object/v2")
    @ApiOperation(value = "put-对象参数", notes = "返回对象")
    UserDTO putObject(@RequestBody UserDTO userDto);

    //delete

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "delete-无参数", notes = "返回字符串")
    String delete();

    @DeleteMapping(value = "/delete/{path}")
    @ApiOperation(value = "delete-路径参数")
    String deletePath1(@PathVariable("path") String path);

    @DeleteMapping(value = "/delete/param")
    @ApiOperation(value = "delete-拼接参数")
    String deletePath2(@RequestParam("path") String path);

    @DeleteMapping(value = "/delete/object/v1")
    @ApiOperation(value = "delete-无参数", notes = "返回对象")
    Object deleteObject();

}
