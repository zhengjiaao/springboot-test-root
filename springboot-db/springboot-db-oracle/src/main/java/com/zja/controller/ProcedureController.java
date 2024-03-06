package com.zja.controller;

import com.zja.entity.UserEntity;
import com.zja.service.ProcedureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2019/6/24 11:09
 */
@RestController("ProcedureController")
@RequestMapping("/procedure")
@Api(tags = {"简单存储过程示例"})
public class ProcedureController extends BaseController {

    @Resource
    private ProcedureService procedureService;

    @ApiOperation(value = "获取所有用户信息",notes = "存储过程",httpMethod = "GET")
    @GetMapping(value = "/getusers")
    public List<UserEntity> getUsers(){
        List<UserEntity> userEntityList = procedureService.getUsers();
        return userEntityList;
    }

    @ApiOperation(value = "根据年龄获取用户信息",notes = "存储过程",httpMethod = "GET")
    @GetMapping(value = "/getusersbyage")
    public Object getUsersByAge(@ApiParam(value = "传参值：年龄",required = true) @RequestParam String age){
        List<UserEntity> userEntityList = procedureService.getUsersByAge(age);
        return userEntityList;
    }
}
