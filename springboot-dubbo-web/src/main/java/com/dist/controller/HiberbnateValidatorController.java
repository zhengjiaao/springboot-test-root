package com.dist.controller;

import com.dist.model.dto.ValidatorDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**校验
 * spring-boot-starter-web包里面有hibernate-validator包，不需要引用hibernate validator依赖
 *
 * 配置：ValidatorConfiguration
 * 默认： 1.普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
 *       2.快速失败返回模式(只要有一个验证失败，则返回)
 * @author zhengja@dist.com.cn
 * @data 2019/8/12 9:30
 */
@RequestMapping(value = "rest/validator")
@RestController
@Api(tags = {"HiberbnateValidatorController"},description = "Hiberbnate Validator 校验")
public class HiberbnateValidatorController extends BaseController {

    @ApiOperation(value = "POST接口验证",httpMethod = "POST")
    @RequestMapping(value = "/postValidator",method = RequestMethod.POST)
    public void validator(@ApiParam(value = "具体参考：Model") @RequestBody @Valid ValidatorDto validatorDto,@ApiParam(value = "BindingResult是验证不通过的结果集合") BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
    }
}
