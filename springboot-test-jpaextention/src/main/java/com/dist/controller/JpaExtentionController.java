package com.dist.controller;

import com.dist.config.SqlMapperExt;
import com.dist.dao.UserDao;
import com.dist.entity.UserEntity;
import com.slyak.spring.jpa.SqlMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 11:24
 */
@RestController
@RequestMapping(value = "jpa/exten")
@Api(tags = {"JpaExtentionController"},description = "jpa原扩展及jpa自定义扩展测试")
public class JpaExtentionController {

    @Autowired
    UserDao userDao;

    @Autowired
    SqlMapper sqlMapper;

    @Autowired
    SqlMapperExt sqlMapperExt;

    @ApiOperation(value = "测试jpa扩展",httpMethod = "GET")
    @RequestMapping(value = "v1/getjpa",method = RequestMethod.GET)
    public void jpaexten() throws Exception{

        System.out.println("=原jpa支持=====");
        final List<UserEntity> entityList = userDao.findAll();
        System.out.println(entityList);

        System.out.println("=原jpa支持sql=====");
        final List<String> names = userDao.getNames();
        System.out.println(names);

        System.out.println("======jpa原扩展========");
        final List<UserEntity> all = userDao.getAlls();
        System.out.println(all);

        System.out.println("sqlMapper自定义扩展");
        List<String> resultList = (List<String>)sqlMapper.getResultList("UserEntity:getList");
        System.out.println(resultList);

        System.out.println("对sqlMapper再次进行自定义扩展");
        List<String> nameList = (List<String>) sqlMapperExt.getResultList("UserEntity:getNames");
        System.out.println(nameList);
    }
}
