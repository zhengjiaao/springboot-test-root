package com.dist.controller;

import com.dist.dto.UserDTO;
import com.dist.server.rest.RestProxyService;
import com.dist.response.ResponseData;
import com.dist.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 16:49
 */
@RestController
@Api(tags = {""},description = "动态代理")
@RequestMapping(value = "cglib/v1")
@DependsOn(value = {"appContextUtil"}) //让 appContextUtil先注入到bean，也就是让restProxyService先注入bean中
public class RestProxyController {

    @Autowired
    private RestProxyService restProxyService;

    @ApiOperation(value ="提供get方法测试-不传参数",notes = "不传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto3",method = RequestMethod.GET)
    public ResponseData getUserDTO(){
        return ResponseUtil.success(restProxyService.getUserDTO3());
    }

    @ApiOperation(value ="提供post方法测试-传参数",notes = "传参数",httpMethod = "POST")
    @RequestMapping(value = "post/userdto4",method = RequestMethod.POST)
    public ResponseData saveUserDTO(@RequestBody UserDTO userDTO){
        return ResponseUtil.success(restProxyService.getUserDTO4(userDTO));
    }

    @ApiOperation(value ="提供lsit<UserDTO>方法测试",httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit2",method = RequestMethod.GET)
    public ResponseData getUserDTOS2(){
        return ResponseUtil.success(restProxyService.getUserDTOS2());
    }
}
