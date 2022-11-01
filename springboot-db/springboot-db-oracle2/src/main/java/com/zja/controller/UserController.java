package com.zja.controller;

import com.zja.entity.duke.DukeUserEntity;
import com.zja.entity.gxtz.GxtzUserEntity;
import com.zja.response.ResponseData;
import com.zja.response.ResponseUtil;
import com.zja.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 12:16
 */
@RestController
@Api(value = "用户管理",description = "jpa测试双数据源")
@RequestMapping(value = "/rest/v1")
public class UserController {

    @Resource
    UserService userService;

    @ApiOperation(value = "保存用户信息-DukeUser",notes = "数据源一的数据保存",httpMethod = "POST")
    @RequestMapping(value = "saveDukeUser",method = {RequestMethod.POST})
    public ResponseData saveDukeUser(@RequestBody DukeUserEntity dukeUserEntity){
       /* DukeUserEntity dukeUserEntity = new DukeUserEntity();
        dukeUserEntity.setName("lisi");
        dukeUserEntity.setAge("23");
        dukeUserEntity.setCreateTime(new Date());
        dukeUserEntity.setGuid("1");*/
        DukeUserEntity saveDukeUser = userService.saveDukeUser(dukeUserEntity);
        return ResponseUtil.success(saveDukeUser);
    }

    @ApiOperation(value = "保存用户信息-GxtzUser",notes = "数据源二的数据保存",httpMethod = "POST")
    @RequestMapping(value = "saveGxtzUser",method = {RequestMethod.POST})
    public ResponseData saveGxtzUser(){
        GxtzUserEntity gxtzUserEntity = new GxtzUserEntity();
        gxtzUserEntity.setName("zhangsan");
        gxtzUserEntity.setAge("24");
        gxtzUserEntity.setCreateTime(new Date());
        gxtzUserEntity.setGuid("1");
        GxtzUserEntity saveGxtzUser = userService.saveGxtzUser(gxtzUserEntity);
        return ResponseUtil.success(saveGxtzUser);
    }

    @ApiOperation(value = "查询-DukeUser",notes = "数据源一的数据查询",httpMethod = "GET")
    @RequestMapping(value = "getAllDukeUser",method = {RequestMethod.GET})
    public ResponseData getAllDukeUser(){
        return ResponseUtil.success(userService.getAllDukeUser());
    }

    @ApiOperation(value = "查询-GxtzUser",notes = "数据源二的数据查询",httpMethod = "GET")
    @RequestMapping(value = "getAllGxtzUser",method = {RequestMethod.GET})
    public ResponseData getAllGxtzUser(){
        return ResponseUtil.success(userService.getAllGxtzUser());
    }
}
