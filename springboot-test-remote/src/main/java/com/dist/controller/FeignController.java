package com.dist.controller;

import com.dist.api.FeignTestService;
import com.dist.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**Feign方式请求http接口
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 13:27
 */
@Api(tags = "FeignController",description = "Feign调用远程接口")
@RestController
@RequestMapping(value = "rest/material/v2")
public class FeignController {

    @Autowired
    FeignTestService service;

    @ApiOperation(value ="提供get方法测试-不传参数",notes = "不传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto",method = RequestMethod.GET)
    public Object getUserDTO(){
        System.out.println("remote:"+"进入调用方法");
        return service.getUserDTO();
    }

    @ApiOperation(value ="提供get方法测试-传参数",notes = "带传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2",method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认",defaultValue = "调用成功",required = true) @RequestParam String param){
        System.out.println("remote:param=="+param);
        return service.getUserDTO(param);
    }

    @ApiOperation(value ="提供post方法测试",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto",method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto",required = true) @RequestBody UserDTO userDto){
        System.out.println("remote:userDto=="+userDto);
        userDto.setDate(new Date());
        return service.postUserDTO(userDto);
    }

    @ApiOperation(value ="提供put方法测试",httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto",method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：userDto",required = true) @RequestBody UserDTO userDto){
        System.out.println("remote:userDto=="+userDto);
        userDto.setDate(new Date());
        return service.putUserDTO(userDto);
    }

    @ApiOperation(value ="提供delete方法测试",httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto",method = RequestMethod.DELETE)
    public Object deleteUserDTO(){
        System.out.println("remote:"+"进入删除方法");
        return service.deleteUserDTO();
    }

    @ApiOperation(value ="提供lsit<UserDTO>方法测试",httpMethod = "GET")
    @RequestMapping(value = "/get/userdtos",method = RequestMethod.GET)
    public List<UserDTO> getUserDTOS(){
        return service.getUserDTOS();
    }

    @ApiOperation(value ="提供listobject方法测试",httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit",method = RequestMethod.GET)
    public Object getUserDTOList(){
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



}
