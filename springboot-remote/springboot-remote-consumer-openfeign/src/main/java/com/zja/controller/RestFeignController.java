package com.zja.controller;

import com.zja.dto.UserDTO;
import com.zja.remote.SpringRestFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/fegin/rest")
@Api(tags = {"RestFeignController"})
public class RestFeignController {

//    @Autowired
//    RestFeignClient restFeignClient;

    @Autowired
    SpringRestFeignClient restFeignClient;

    //get

    @GetMapping(value = "/get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    public Object get() {
        return restFeignClient.get();
    }

    @GetMapping(value = "/get/{path}")
    @ApiOperation(value = "get-路径参数")
    public Object getPath1(@PathVariable("path") String path) {
        return restFeignClient.getPath(path);
    }

    @GetMapping(value = "/get/param/v1")
    @ApiOperation(value = "get-拼接参数")
    public Object getPath2(@RequestParam("param") String param) {
        return restFeignClient.getParam1(param);
    }

    @GetMapping(value = "/get/param/v2")
    @ApiOperation(value = "get-拼接参数", notes = "对象属性参数，相当于 @RequestParam(required = false) 属性名")
    public Object getObject(UserDTO userDTO) {
        return restFeignClient.getParam2(userDTO);
    }

    @GetMapping(value = "/get/object/v1")
    @ApiOperation(value = "get-无参数", notes = "返回对象")
    public Object getObject() {
        return restFeignClient.getObject();
    }

    @GetMapping(value = "/get/object/list/v1")
    @ApiOperation(value = "get-无参数", notes = "返回List")
    public Object getObjectList() {
        return restFeignClient.getObjectList();
    }

    //post

    @PostMapping(value = "/post")
    @ApiOperation(value = "post-无参数", notes = "返回字符串")
    public Object post() {
        return restFeignClient.post();
    }

    @PostMapping(value = "/post/param")
    @ApiOperation(value = "post-多个参数", notes = "返回字符串")
    public Object postObject(@ApiParam(value = "传参值：name") @RequestParam String name,
                             @ApiParam(value = "传参值：age") @RequestParam String age) {
        return restFeignClient.postObject(name, age);
    }

    @PostMapping(value = "/post/object/v1")
    @ApiOperation(value = "post-无参数", notes = "返回对象")
    public Object postObject() {
        return restFeignClient.postObject();
    }

    @PostMapping(value = "/post/object/v2")
    @ApiOperation(value = "post-对象参数", notes = "返回对象")
    public Object postObject(@RequestBody UserDTO userDto) {
        return restFeignClient.postObject(userDto);
    }

    //put

    @PutMapping(value = "/put")
    @ApiOperation(value = "put-无参数", notes = "返回字符串")
    public Object put() {
        return restFeignClient.put();
    }

    @PutMapping(value = "/put/path/{path}")
    @ApiOperation(value = "put-路径参数", notes = "返回字符串")
    public Object putPath(@PathVariable("path") String path) {
        return restFeignClient.putPath(path);
    }

    @PutMapping(value = "/put/param")
    @ApiOperation(value = "put-拼接参数", notes = "返回字符串")
    public Object putParam(@RequestParam String param) {
        return restFeignClient.putParam(param);
    }

    @PutMapping(value = "/put/object/v1")
    @ApiOperation(value = "put-无参数", notes = "返回对象")
    public Object putObject() {
        return restFeignClient.putObject();
    }

    @PutMapping(value = "/put/object/v2")
    @ApiOperation(value = "put-对象参数", notes = "返回对象")
    public UserDTO putObject(@RequestBody UserDTO userDto) {
        return restFeignClient.putObject(userDto);
    }

    //delete

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "delete-无参数", notes = "返回字符串")
    public Object delete() {
        return restFeignClient.delete();
    }

    @DeleteMapping(value = "/delete/path/{path}")
    @ApiOperation(value = "delete-路径参数")
    public Object deletePath1(@PathVariable("path") String path) {
        return restFeignClient.deletePath1(path);
    }

    @DeleteMapping(value = "/delete/param")
    @ApiOperation(value = "delete-拼接参数")
    public Object deletePath2(@RequestParam("param") String param) {
        return restFeignClient.deletePath2(param);
    }

    @DeleteMapping(value = "/delete/object/v1")
    @ApiOperation(value = "delete-无参数", notes = "返回对象")
    public Object deleteObject() {
        return restFeignClient.deleteObject();
    }

}
