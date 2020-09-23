package com.zja.controller;

import com.zja.entity.UserEntity;
import com.zja.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:36
 * @Author: Mr.Zheng
 * @Description:
 */
@Controller("UserController")
@RequestMapping("/user")
@Api(tags = {"UserController"}, description = "用户服务")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "添加用户信息",notes = "UserService",httpMethod = "POST")
    @RequestMapping(value = "addUserEntity",method = RequestMethod.POST)
    public UserEntity addUserEntity(@ApiParam(value = "参考：Mobile") @RequestBody UserEntity userEntity){
        userEntity.setCreateTime(new Date());
        return userService.addUserEntity(userEntity);
    }

    @ApiOperation(value = "更新用户信息",notes = "UserService",httpMethod = "PUT")
    @RequestMapping(value = "updateUserEntity",method = RequestMethod.PUT)
    public UserEntity updateUserEntity(@ApiParam(value = "参考：Mobile") @RequestBody UserEntity userEntity){
        return userService.updateUserEntityById(userEntity);
    }

    @ApiOperation(value = "获取所有用户",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getUserList",method = RequestMethod.GET)
    public List<UserEntity> getUserList(){
        return userService.getUserList();
    }

    @ApiOperation(value = "根据id获取用户",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getUserEntityById",method = RequestMethod.GET)
    public Object getUserEntityById(@ApiParam(value = "id") @RequestParam(value = "id")Long Id){
        return userService.getUserById(Id);
    }

    @ApiOperation(value = "获取所有用户sort排序",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getsortUserList",method = RequestMethod.GET)
    public List<UserEntity> getsortUserList(){
        return userService.getCurrentUserList();
    }

    @ApiOperation(value = "分页获取用户信息",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getPageUserList",method = RequestMethod.GET)
    public Page<UserEntity> getPageUserList(){
        return userService.getPageUserList();
    }

    @ApiOperation(value = "根据id删除用户信息",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "daleteUserEntityById",method = RequestMethod.GET)
    public void daleteUserEntityById(@ApiParam(value = "id") @RequestParam(value = "id")Long Id){
        userService.daleteUserEntityById(Id);
    }

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder){
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));  //TimeZone时区,解决差8小时问题
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,false));
    }

}
