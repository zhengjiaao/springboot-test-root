/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 17:34
 * @Since:
 */
package com.zja.controller;

import com.zja.dto.UserDTO;
import com.zja.remote.RemoteInterfaceFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(tags = "RemoteInterfaceController", description = "调用远程接口")
@RestController
@RequestMapping("/rest")
public class RemoteInterfaceController {

    @Autowired
    RemoteInterfaceFeignClient service;

    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto", method = RequestMethod.GET)
    public Object getUserDTO() {
        System.out.println("remote:" + "进入调用方法");
        return service.getUserDTO();
    }

    //未成功，get 不允许传 body，delete允许传body
    @ApiOperation(value = "提供get方法测试-传body参数", notes = "传body参数", httpMethod = "GET")
    @RequestMapping(value = "/get/body", method = RequestMethod.GET)
    public Object getBody(UserDTO userDTO) {
        System.out.println("remote:" + "进入调用方法");
        return service.getBody(userDTO);
    }

    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/token", method = RequestMethod.GET)
    public Object getToken(String token) {
        System.out.println("remote:" + "进入调用方法");
        return service.getToken(token);
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

    @ApiOperation(value = "提供delete body方法测试", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/body", method = RequestMethod.DELETE)
    public Object deleteBody(@RequestBody UserDTO userDTO) {
        System.out.println("remote:" + "进入删除方法");
        return service.deleteBody(userDTO);
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
//    @HystrixCommand(fallbackMethod = "provider_TimeOut_Handler", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
//    })
//    @ApiOperation(value = "提供服务降级测试", httpMethod = "GET")
//    @RequestMapping(value = "/get/hystrix", method = RequestMethod.GET)
//    public Object getHystrix() {
//        return service.getHystrix();
//    }
//
//    private String provider_TimeOut_Handler() {
//        return "线程池:  " + Thread.currentThread().getName() + ", 当前系统繁忙或者运行报错，请稍后再试！！！" + " o(╥﹏╥)o";
//    }

    /*********************服务降级-end**********************/





    /*####################服务熔断-start###################*/


    /*####################服务熔断-start###################*/

}
