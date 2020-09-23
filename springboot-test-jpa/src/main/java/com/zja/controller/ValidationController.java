package com.zja.controller;

import com.zja.config.validator.api.ValidatorAPI;
import com.zja.config.validator.entity.ValidResult;
import com.zja.config.validator.util.ValidationUtil;
import com.zja.dto.UserDTO;
import com.zja.exception.APIException;
import com.zja.result.ResultUtil;
import com.zja.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 14:08
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Hibernate Validator和Bean Validation
 */
@Validated  //配合get请求注解@RequestParam参数使用
@RestController
@RequestMapping("validation/public")
@Api(value = "ValidationController", description = "后端验证参数")
public class ValidationController {

    @Autowired
    @Qualifier("validatorAPIAll")
    private ValidatorAPI validatorAPIAll;

    @Autowired
    @Qualifier("validatorAPIOne")
    private ValidatorAPI validatorAPIOne;

    @GetMapping("api/v1")
    @ApiOperation("自定义异常")
    public ResultVO get() {
        if (true) {
            throw new APIException();
        }
        return ResultUtil.success(true);
    }

    @GetMapping("user/v1")
    @ApiOperation("新增用户信息")
    public ResultVO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setLoginName("123456");
        userDTO.setPassword("123456");
        userDTO.setEmail("126@qq.com");
        return ResultUtil.success(userDTO);
    }

    @PostMapping("user/v1")
    @ApiOperation(value = "新增用户-普通失败模式", notes = "普通失败模式-注解检验参数")
    public ResultVO addUser(@Valid @RequestBody UserDTO userDTO) {
        //自定义异常重组错误参数信息放入map
        return ResultUtil.success(userDTO);
    }

    @GetMapping("user/v2")
    @ApiOperation(value = "新增用户-普通失败模式", notes = "普通失败模式-抛异常BindException异常")
    public ResultVO addUser2(@Valid UserDTO userDTO) {
        //自定义异常重组错误参数信息放入map
        return ResultUtil.success(userDTO);
    }

    @PostMapping("user/v3")
    @ApiOperation(value = "新增用户-普通失败模式", notes = "普通失败模式-检验所有参数错误")
    public ResultVO addUser3(@RequestBody UserDTO userDTO) {
        ValidResult validResult = validatorAPIAll.validateBean(userDTO);
        if (validResult.hasErrors()) {
            //返回字符串参数错误信息
            return ResultUtil.paramError(validResult.getErrors());
        }
        return ResultUtil.success(userDTO);
    }

    @PostMapping("user/v4")
    @ApiOperation(value = "新增用户-快速失败返回模式", notes = "快速失败返回模式-随机检验一个参数错误")
    public ResultVO addUser4(@RequestBody UserDTO userDTO) {
        ValidResult validResult = validatorAPIOne.validateBean(userDTO);
        if (validResult.hasErrors()) {
            //参数错误信息放入map
            return ResultUtil.paramError(validResult.getMapErrors());
        }
        return ResultUtil.success(userDTO);
    }

    @PostMapping("user/v5")
    @ApiOperation(value = "新增用户-快速失败返回模式", notes = "快速失败返回模式-随机检验一个参数错误")
    public ResultVO addUser5(@RequestBody UserDTO userDTO) {
        ValidResult validResult = validatorAPIOne.validateBean(userDTO);
        if (validResult.hasErrors()) {
            //集合参数错误信息放入list
            return ResultUtil.paramError(validResult.getListErrors());
        }
        return ResultUtil.success(userDTO);
    }


    @PostMapping("util/v1")
    @ApiOperation("自定义工具类验证")
    public ResultVO addUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName("1234");
        userDTO.setPassword("");
        userDTO.setEmail("126qq.com");

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(userDTO);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
        return ResultUtil.success(true);
    }


    @GetMapping("str/v1")
    @ApiOperation(value = "单个参数校验", notes = "注释校验")
    public ResultVO strv1(@RequestParam @Email String email) {

        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName("1234");
        userDTO.setPassword("");
        userDTO.setEmail(email);

        return ResultUtil.success(userDTO);
    }

    @GetMapping("str/v2")
    @ApiOperation(value = "单个参数校验", notes = "注释校验")
    public ResultVO strv2(@RequestParam @NotBlank String loginName) {

        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName(loginName);
        userDTO.setPassword("123");
        userDTO.setEmail("123@.com");

        return ResultUtil.success(userDTO);
    }


}
