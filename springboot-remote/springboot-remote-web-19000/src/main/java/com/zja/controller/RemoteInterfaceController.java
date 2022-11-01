package com.zja.controller;

import com.zja.dto.As;
import com.zja.dto.EpoitParamsDTO;
import com.zja.dto.UserDTO;
import com.zja.response.ResponseData;
import com.zja.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**提供远程接口
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 16:29
 */
@Api(value = "提供远程测试接口", description = "提供测试接口给remote")
@RequestMapping(value = "rest/v1")
@RestController
public class RemoteInterfaceController {

    @ApiOperation(value = "get-不传参数", notes = "返回对象", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto", method = RequestMethod.GET)
    public Object getUserDTO() {
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO==" + userDTO);
        return userDTO;
    }

    @ApiOperation(value = "get-不传参数", notes = "返回对象", httpMethod = "GET")
    @RequestMapping(value = "/get/token", method = RequestMethod.GET)
    public Object getUserDTO(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println("token= " + token);
        return token;
    }

    @ApiOperation(value = "get-传参数", notes = "返回对象", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2", method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认", defaultValue = "调用成功", required = true) @RequestParam String param) {
        System.out.println("service:param: " + param);
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO==" + userDTO);
        return userDTO;
    }

    @ApiOperation(value = "get-传参数-对象接收", notes = "返回对象", httpMethod = "GET")
    @RequestMapping(value = "/get/user/object", method = RequestMethod.GET)
    public Object checkTelihuiLogin(@ApiParam(value = "传参值：userDto", required = true) UserDTO userDto) {
        System.out.println("service:userDto==" + userDto);
        return userDto;
    }

    @ApiOperation(value = "get-不传参数", notes = "返回对象", httpMethod = "GET")
    @RequestMapping(value = "/get/v1", method = RequestMethod.GET)
    public ResponseData getUserDTO3() {
        UserDTO userDTO = getUserDto();
        System.out.println("service:userDTO==" + userDTO);
        return ResponseUtil.success(userDTO);
    }

    @ApiOperation(value = "post-传参", notes = "返回对象", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto", method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("service:userDto==" + userDto);
        return userDto;
    }

    @ApiOperation(value = "post-传参-对象接收", notes = "返回对象", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto5", method = RequestMethod.POST)
    public Object postUserDTO5(@ApiParam(value = "传参值：EpoitParamsDTO", required = true) @RequestBody EpoitParamsDTO epoitParamsDTO) {
        System.out.println("service:epoitParamsDTO==" + epoitParamsDTO);
        return epoitParamsDTO;
    }

    @ApiOperation(value = "post-不传参", notes = "返回字符串", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto2", method = RequestMethod.POST)
    public String postUserDTO2() {
        System.out.println("service:" + "不传参成功");
        return "不传参成功！";
    }

    @ApiOperation(value = "post-传多参-string接收", notes = "返回对象字符串", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto3", method = RequestMethod.POST)
    public Object postUserDTO3(@ApiParam(value = "传参值：name") @RequestParam String name,
                               @ApiParam(value = "传参值：age") @RequestParam String age) {
        System.out.println("service:" + "传参成功");
        String result = "名字：" + name + "  ，年龄：" + age;
        return result;
    }

    @ApiOperation(value = "post-传参-对象接收", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto4", method = RequestMethod.POST)
    public ResponseData postUserDTO4(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("service:userDto==" + userDto);
        return ResponseUtil.success(userDto);
    }

    @ApiOperation(value = "post-上传单文件", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v1", method = RequestMethod.POST)
    public ResponseData uploadMongo(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        return ResponseUtil.success(true);
    }

    @ApiOperation(value = "post-上传单文件-支持重命名", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v2", method = RequestMethod.POST)
    public ResponseData uploadMongoV2(
            @ApiParam(value = "上传的文件", required = false) @RequestParam(value = "file", required = false) MultipartFile file,
            @ApiParam(value = "文件名字(带后缀)", required = false) @RequestParam(value = "fileName", required = false) String fileName) {
        System.out.println("上传文件：" + fileName + "  大小：" + file.getSize());
        return ResponseUtil.success(true);
    }

    @ApiOperation(value = "post-上传多文件", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v3", method = RequestMethod.POST)
    public ResponseData uploadMongo(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "files", required = false) MultipartFile[] files) {
        if (files.length <= 0) {
            return ResponseUtil.fail("请选择要上传的文件！");
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        return ResponseUtil.success(true);
    }

    @ApiOperation(value = "put-无参", httpMethod = "PUT")
    @RequestMapping(value = "/put/v1", method = RequestMethod.PUT)
    public Object putUserDTO() {
        System.out.println("service:" + "传参成功");
        return "put 无参请求成功！";
    }

    @ApiOperation(value = "put-传多参-string接收", notes = "传多个参数", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto2", method = RequestMethod.PUT)
    public Object putUserDTO2(@ApiParam(value = "传参值：name") @RequestParam String name,
                              @ApiParam(value = "传参值：age") @RequestParam String age) {
        System.out.println("service:" + "传参成功");
        String result = "名字：" + name + "  ,年龄：" + age;
        return result;
    }

    @ApiOperation(value = "put-传多参-对象接收", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto", method = RequestMethod.PUT)
    public Object putUserDTO3(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("service:修改/更新成功" + userDto);
        return userDto;
    }

    @ApiOperation(value = "提供delete方法测试", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto", method = RequestMethod.DELETE)
    public Object deleteUserDTO() {
        System.out.println("service:删除成功");
        return true;
    }

    @ApiOperation(value = "提供delete方法测试-带参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto2", method = RequestMethod.DELETE)
    public Object deleteUserDTO2(@ApiParam(value = "传参值：name") @RequestParam String name) {
        System.out.println("service:删除成功");
        String result = "名字：" + name + "结果：" + "true";
        return result;
    }

    @ApiOperation(value = "提供lsit<UserDTO>方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit", method = RequestMethod.GET)
    public List<UserDTO> getUserDTOS() {
        UserDTO userDTO = getUserDto();
        UserDTO userDTO1 = getUserDto();
        UserDTO userDTO2 = getUserDto();
        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(userDTO);
        dtoList.add(userDTO1);
        dtoList.add(userDTO2);
        return dtoList;
    }

    @ApiOperation(value = "提供lsit<UserDTO>方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit2", method = RequestMethod.GET)
    public ResponseData getUserDTOS2() {
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
    private UserDTO getUserDto() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setName("Zhangsan");
        userDTO.setDate(new Date());
        return userDTO;
    }

    @ApiOperation(value = "提供服务降级测试", httpMethod = "GET")
    @RequestMapping(value = "/get/hystrix", method = RequestMethod.GET)
    public ResponseData getHystrix() {
        String msg = "调用成功！";
        System.out.println("service: " + msg);

        //模拟出异常
        //int age = 10/0;

        //模拟请求超时
        /*try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return ResponseUtil.success((Object) msg);
    }


    @ApiOperation(value = "get测试-传多个参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/login", method = RequestMethod.GET)
    public Object checkTelihuiLogin(@ApiParam(value = "loginName", required = true) @RequestParam String loginName,
                                    @ApiParam(value = "casCookie", required = true) @RequestParam String casCookie) {
        System.out.println("service:loginName: " + loginName);
        System.out.println("service:casCookie: " + casCookie);
        return loginName;
    }

    @ApiOperation(value = "get测试-传参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/login/ex", method = RequestMethod.GET)
    public Object checkTelihuiLogin(@ApiParam(value = "loginName", required = true) @RequestParam String loginName) {
        System.out.println("service:loginName: " + loginName);
        As as = new As();
        as.setAuthenticated(false);
        return as;
    }

    //特殊传参，示例 get、delete 传 body 数据
    //未成功，get不允许传 body
    @ApiOperation(value = "get测试-传body参数", notes = "带传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/body", method = RequestMethod.GET)
    public Object getBody(@RequestBody UserDTO userDto) {
        System.out.println("userDto: " + userDto);
        return userDto;
    }

    //成功，delete允许传body
    @ApiOperation(value = "delete测试-传body参数", notes = "带传参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/body", method = RequestMethod.DELETE)
    public Object deleteBody(@RequestBody UserDTO userDto) {
        System.out.println("userDto: " + userDto);
        return userDto;
    }

}
