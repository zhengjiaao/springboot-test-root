package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zja.api.FeignTestService;
import com.zja.api.TelihuiService;
import com.zja.dto.UserDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Feign方式请求http接口
 *
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 13:27
 */
@Api(tags = "FeignController", description = "Feign调用远程接口")
@RestController
@RequestMapping(value = "rest/material/v2")
public class FeignController {

    @Autowired
    FeignTestService service;

    @Autowired
    TelihuiService telihuiService;

    @ApiOperation(value = "特力惠用户登录验证-传多参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "checkTelihuiLogin", method = RequestMethod.GET)
    public Object checkTelihuiLogin(@ApiParam(value = "loginName", required = true) @RequestParam String loginName,
                                    @ApiParam(value = "casCookie", required = true) @RequestParam String casCookie) {
        System.out.println("remote:loginName==" + loginName);
        System.out.println("remote:casCookie==" + casCookie);
        return telihuiService.checkTelihuiLogin(loginName, casCookie);
    }

    @ApiOperation(value = "特力惠用户登录验证-传参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "checkTelihuiLogin/ex", method = RequestMethod.GET)
    public Object checkTelihuiLogin(@ApiParam(value = "loginName", required = true) @RequestParam String loginName) {
        System.out.println("remote:loginName==" + loginName);
        Object o = telihuiService.checkTelihuiLogin(loginName);
        System.out.println(o);
        JSONObject jsonObj = (JSONObject) JSON.toJSON(o);
        System.out.println("jsonObj" + jsonObj);
        String authenticated = jsonObj.get("authenticated").toString();
        System.out.println(authenticated);
        if (authenticated.equals("false")) {
            System.out.println("用户未登录第三方特力惠平台");
        }
        return o;
    }

    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto", method = RequestMethod.GET)
    public Object getUserDTO() {
        System.out.println("remote:" + "进入调用方法");
        return service.getUserDTO();
    }

    @ApiOperation(value = "提供get方法测试-传参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2", method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认", defaultValue = "调用成功", required = true) @RequestParam String param) {
        System.out.println("remote:param==" + param);
        return service.getUserDTO(param);
    }

    @ApiOperation(value = "提供get方法测试-传参数-对象参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto3", method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：userDto") @Valid UserDTO userDto) {
        System.out.println("remote:userDto==" + userDto);
        return service.getUserDTO3(userDto.getId(), userDto.getName());
    }

    @ApiOperation(value = "提供post方法测试", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto", method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("remote:userDto==" + userDto);
        userDto.setDate(new Date());
        return service.postUserDTO(userDto);
    }

    @ApiOperation(value = "提供put方法测试", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto", method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("remote:userDto==" + userDto);
        userDto.setDate(new Date());
        return service.putUserDTO(userDto);
    }

    @ApiOperation(value = "提供delete方法测试", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto", method = RequestMethod.DELETE)
    public Object deleteUserDTO() {
        System.out.println("remote:" + "进入删除方法");
        return service.deleteUserDTO();
    }

    @ApiOperation(value = "提供lsit<UserDTO>方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtos", method = RequestMethod.GET)
    public List<UserDTO> getUserDTOS() {
        return service.getUserDTOS();
    }

    @ApiOperation(value = "提供listobject方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit", method = RequestMethod.GET)
    public Object getUserDTOList() {
        return service.getUserDTOList();
    }


    /*@ApiOperation(value = "调用接口测试",notes = "调用接口测试")
    @RequestMapping(value = "/fileretrieve",method = RequestMethod.GET)
    public Object test(@ApiParam(value = "传参值：默认root",defaultValue = "root") @RequestParam String dir){
        Object o = service.getFileRetrieveFromFtps("root");
        Map<String,Object> map = (Map<String, Object>) o;
        List<FoldersDTO> foldersDTOS = (List<FoldersDTO>) map.get("data");
        System.out.println("foldersDTOS=="+foldersDTOS);
        return o;
    }*/


    /*********************服务降级-start**********************/
    @HystrixCommand(fallbackMethod = "provider_TimeOut_Handler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    @ApiOperation(value = "提供服务降级测试", httpMethod = "GET")
    @RequestMapping(value = "/get/hystrix", method = RequestMethod.GET)
    public Object getHystrix() {
        return service.getHystrix();
    }

    private String provider_TimeOut_Handler() {
        return "线程池:  " + Thread.currentThread().getName() + ", 当前系统繁忙或者运行报错，请稍后再试！！！" + " o(╥﹏╥)o";
    }

    /*********************服务降级-end**********************/





    /*####################服务熔断-start###################*/


    /*####################服务熔断-start###################*/

}
