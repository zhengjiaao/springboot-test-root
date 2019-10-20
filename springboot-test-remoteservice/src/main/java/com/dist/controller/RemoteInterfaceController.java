package com.dist.controller;

import com.dist.dto.EpoitParamsDTO;
import com.dist.dto.UserDTO;
import com.dist.response.ResponseData;
import com.dist.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**提供远程接口
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 16:29
 */
@Api(value = "提供远程测试接口",description = "提供测试接口给remote")
@RequestMapping(value = "rest/v1")
@RestController
public class RemoteInterfaceController {

    @ApiOperation(value ="提供get方法测试-不传参数",notes = "不传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto",method = RequestMethod.GET)
    public Object getUserDTO(){
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO=="+userDTO);
        return userDTO;
    }

    @ApiOperation(value ="提供get方法测试-传参数",notes = "带传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2",method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认",defaultValue = "调用成功",required = true) @RequestParam String param){
        System.out.println("service:param: "+param);
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO=="+userDTO);
        return userDTO;
    }

    @ApiOperation(value ="提供get方法测试-不传参数",notes = "不传参数",httpMethod = "GET")
    @RequestMapping(value = "/get/userdto3",method = RequestMethod.GET)
    public ResponseData getUserDTO3(){
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO=="+userDTO);
        return ResponseUtil.success(userDTO);
    }

    @ApiOperation(value ="提供post方法测试",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto",method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto",required = true) @RequestBody UserDTO userDto){
        System.out.println("service:userDto=="+userDto);
        return userDto;
    }

    @ApiOperation(value ="提供post方法测试",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto5",method = RequestMethod.POST)
    public Object postUserDTO5(@ApiParam(value = "传参值：EpoitParamsDTO",required = true) @RequestBody EpoitParamsDTO epoitParamsDTO){
        System.out.println("service:epoitParamsDTO=="+epoitParamsDTO);
        return epoitParamsDTO;
    }

    @ApiOperation(value ="提供post方法测试",notes = "不传参",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto2",method = RequestMethod.POST)
    public Object postUserDTO2(){
        System.out.println("service:"+"不传参成功");
        return "不传参成功";
    }

    @ApiOperation(value ="提供post方法测试",notes = "传多个参数",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto3",method = RequestMethod.POST)
    public Object postUserDTO3(@ApiParam(value = "传参值：name") @RequestParam String name,
                               @ApiParam(value = "传参值：age") @RequestParam String age){
        System.out.println("service:"+"传参成功");
        String result= "名字："+name+"年龄："+age;
        return result;
    }

    @ApiOperation(value ="提供post方法测试",httpMethod = "POST")
    @RequestMapping(value = "/post/userdto4",method = RequestMethod.POST)
    public ResponseData postUserDTO4(@ApiParam(value = "传参值：userDto",required = true) @RequestBody UserDTO userDto){
        System.out.println("service:userDto=="+userDto);
        return ResponseUtil.success(userDto);
    }

    @ApiOperation(value ="提供put方法测试",notes = "传多个参数",httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto2",method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：name") @RequestParam String name,
                             @ApiParam(value = "传参值：age") @RequestParam String age){
        System.out.println("service:"+"传参成功");
        String result= "名字："+name+"年龄："+age;
        return result;
    }

    @ApiOperation(value ="提供put方法测试",httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto",method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：userDto",required = true) @RequestBody UserDTO userDto){
        System.out.println("service:修改/更新成功"+userDto);
        return userDto;
    }

    @ApiOperation(value ="提供delete方法测试",httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto",method = RequestMethod.DELETE)
    public Object deleteUserDTO(){
        System.out.println("service:删除成功");
        return true;
    }

    @ApiOperation(value ="提供delete方法测试-带参数",httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto2",method = RequestMethod.DELETE)
    public Object deleteUserDTO2(@ApiParam(value = "传参值：name") @RequestBody String name){
        System.out.println("service:删除成功");
        String result= "名字："+name+"结果："+"true";
        return result;
    }

    @ApiOperation(value ="提供lsit<UserDTO>方法测试",httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit",method = RequestMethod.GET)
    public List<UserDTO> getUserDTOS(){
        UserDTO userDTO = getUserDto();
        UserDTO userDTO1 = getUserDto();
        UserDTO userDTO2 = getUserDto();
        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(userDTO);
        dtoList.add(userDTO1);
        dtoList.add(userDTO2);
        return dtoList;
    }

    @ApiOperation(value ="提供lsit<UserDTO>方法测试",httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit2",method = RequestMethod.GET)
    public ResponseData getUserDTOS2(){
        UserDTO userDTO = getUserDto();
        UserDTO userDTO1 = getUserDto();
        UserDTO userDTO2 = getUserDto();
        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(userDTO);
        dtoList.add(userDTO1);
        dtoList.add(userDTO2);
        return ResponseUtil.success(dtoList);
    }

    /**
     * 获取userdto
     * @return
     */
    private UserDTO getUserDto(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setName("Zhangsan");
        userDTO.setDate(new Date());
        return userDTO;
    }
}
