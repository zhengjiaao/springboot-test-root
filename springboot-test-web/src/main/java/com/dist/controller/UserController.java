package com.dist.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dist.define.Constants;
import com.dist.interfaces.UserService;
import com.dist.model.dto.UserDTO;
import com.dist.model.dto.UserVo;
import com.dist.model.entity.UserEntity;
import com.dist.response.ResponseData;
import com.dist.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:36
 * @Author: Mr.Zheng
 * @Description:
 */
@Slf4j
@RestController("userController")
@RequestMapping("rest/user")
@Api(tags = {"UserController"}, description = "用户服务-增删改查操作")
public class UserController extends  BaseController{

    @Reference
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
    public UserEntity getUserEntityById(@ApiParam(value = "id") @RequestParam(value = "id")Long Id){
        return userService.getUserById(Id);
    }

    @ApiOperation(value = "获取所有用户sort排序",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getsortUserList",method = RequestMethod.GET)
    public List<UserEntity> getsortUserList(){
        return userService.getCurrentUserList();
    }

    @ApiOperation(value = "分页获取用户信息",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "getPageUserList",method = RequestMethod.GET)
    public Object getPageUserList(){
        //org.springframework.data.domain.Page<UserEntity> pageUserList = userService.getPageUserList();
        return null;
    }

    @ApiOperation(value = "根据id删除用户信息",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "daleteUserEntityById",method = RequestMethod.GET)
    public void daleteUserEntityById(@ApiParam(value = "id") @RequestParam(value = "id")Long Id){
        userService.daleteUserEntityById(Id);
    }

    @ApiOperation(value = "获取视图",notes = "UserService",httpMethod = "GET")
    @RequestMapping(value = "findUserVo",method = RequestMethod.GET)
    public List<UserVo> getUserVo(){
        return userService.findUserVo();
    }


    @ApiOperation(value="用户登录", httpMethod = "POST")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public com.dist.response.ResponseData loginWithDevice(@ApiParam(value = "参考 Moble",required = true) @RequestBody UserDTO userDTO){
        HttpSession session = this.request.getSession(true);
        log.info("sessionId(): "+session.getId());
        session.setAttribute(Constants.SESSION_USER, userDTO);
        Enumeration<String> attributeNames = session.getAttributeNames();
        log.info("attributeNames(): "+attributeNames);
        return ResponseUtil.success(session.getAttribute(Constants.SESSION_USER));
    }

    @ApiOperation(value="用户退出）", httpMethod = "GET")
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ResponseData logout(){
        log.info("sessionId(): "+session.getId());
        log.info("removeAttribute前-当前用户信息："+session.getAttribute(Constants.SESSION_USER));
        Enumeration<String> attributeNames = session.getAttributeNames();
        log.info("removeAttribute前-attributeNames(): "+attributeNames);
        session.removeAttribute(Constants.SESSION_USER);
        log.info("removeAttribute后-当前用户信息："+session.getAttribute(Constants.SESSION_USER));
        log.info("removeAttribute后-attributeNames(): "+attributeNames);
        session.invalidate();
        log.info("invalidate后-当前用户信息："+session.getAttribute(Constants.SESSION_USER));
        log.info("invalidate后-attributeNames(): "+attributeNames);
        return ResponseUtil.success();
    }

}
