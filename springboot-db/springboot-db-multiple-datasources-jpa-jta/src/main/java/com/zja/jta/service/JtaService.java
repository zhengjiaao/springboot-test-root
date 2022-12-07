/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-25 16:01
 * @Since:
 */
package com.zja.jta.service;

import com.zja.jta.primary.entity.PrimaryEntity;
import com.zja.jta.primary.repository.PrimaryRepository;
import com.zja.jta.secondary.entity.SecondaryEntity;
import com.zja.jta.secondary.repository.SecondaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JtaService {

    @Autowired
    PrimaryRepository primaryRepository;

    @Autowired
    SecondaryRepository secondaryRepository;

    public void add_example() {
        PrimaryEntity primaryEntity = new PrimaryEntity();
        primaryEntity.setName("数据源1-实体类");
        primaryRepository.save(primaryEntity);

        SecondaryEntity secondaryEntity = new SecondaryEntity();
        secondaryEntity.setName("数据源2-实体类");
        secondaryRepository.save(secondaryEntity);
    }

    //无注解时       抛异常，全部存储
//    @Transactional(rollbackFor = Exception.class)     //使用jta-atomikos时，抛异常，全部不会存储；不使用jta-atomikos时,抛异常,user不会存储，goods会存储；
//    @Transactional(value = "primaryTransactionManager", rollbackFor = Exception.class)  //抛异常，user不会存储，goods会存储
//    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)  //抛异常，user会存储，goods不会存储
//    @Transactional(value = "jtaTransactionManager", rollbackFor = Exception.class)      //抛异常，全部存储
    public void add_rollback_example() {

        PrimaryEntity primaryEntity = new PrimaryEntity();
        primaryEntity.setName("数据源1-实体类");
        primaryEntity.setCreateTime(new Date());
        primaryRepository.save(primaryEntity);

        SecondaryEntity secondaryEntity = new SecondaryEntity();
        secondaryEntity.setName("数据源2-实体类");
        secondaryEntity.setCreateTime(new Date());
        secondaryRepository.save(secondaryEntity);

        //故意抛出异常测试
        int i = 10 / 0;
    }

}
