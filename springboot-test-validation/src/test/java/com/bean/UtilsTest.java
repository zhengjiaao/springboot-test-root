package com.bean;

import com.zja.SpringbootValidationApplication;
import com.zja.config.validator.ValidatorAPI;
import com.zja.dto.GroupDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-18 11:04
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootValidationApplication.class)
public class UtilsTest {

    @Autowired
    @Qualifier("verifyAll")
    private Validator verifyAll;

    @Autowired
    @Qualifier("verifyOne")
    private Validator verifyOne;

    @Autowired
    @Qualifier("validatorAPIAll")
    private ValidatorAPI validatorAPIAll;

    @Autowired
    @Qualifier("validatorAPIOne")
    private ValidatorAPI validatorAPIOne;

    @Test
    public void test() {

    }

    //tset全部验证
    @Test
    public void tsetVerifyAll() {
        GroupDTO groupDTO = new GroupDTO();
        Set<ConstraintViolation<GroupDTO>> result = verifyAll.validate(groupDTO);
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
        //结果：返回所有验证错误
        //middlename middlename may be empty: null
        //firstname firstname may be empty: null

    }

    //测试验证一个
    @Test
    public void testVerifyOne() {
        GroupDTO groupDTO = new GroupDTO();
        Set<ConstraintViolation<GroupDTO>> result = verifyOne.validate(groupDTO);
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
        //结果：随机返回一个验证错误
        //middlename middlename may be empty: null
    }


    //tset全部验证
    @Test
    public void tsetVerifyAll2() {
        GroupDTO groupDTO = new GroupDTO();
        //ResultVO resultVO = validatorAPIAll.validateBean(groupDTO);
        //System.out.println(resultVO.getData());
        //结果：返回所有验证错误
        //middlename middlename may be empty: null
        //firstname firstname may be empty: null

    }


    //测试验证一个
    @Test
    public void testVerifyOne2() {
        GroupDTO groupDTO = new GroupDTO();
        //ResultVO resultVO = validatorAPIOne.validateBean(groupDTO);
        //System.out.println(resultVO.getData());
        //结果：随机返回一个验证错误
        //middlename middlename may be empty: null
    }

}
