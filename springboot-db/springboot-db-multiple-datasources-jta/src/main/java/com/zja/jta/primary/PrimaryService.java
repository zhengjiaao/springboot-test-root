/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-25 16:26
 * @Since:
 */
package com.zja.jta.primary;

import com.zja.jta.primary.entity.PrimaryEntity;
import com.zja.jta.primary.repository.PrimaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrimaryService {

    @Autowired
    PrimaryRepository primaryRepository;

    public void add_example() {
        PrimaryEntity primaryEntity = new PrimaryEntity();
        primaryEntity.setName("数据源1-实体类");
        primaryRepository.save(primaryEntity);
    }

    //无注解时       抛异常，会存储
//    @Transactional(rollbackFor = Exception.class)     //抛异常，不会存储
//    @Transactional(value = "primaryTransactionManager", rollbackFor = Exception.class)  //抛异常，不会存储
//    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)  //抛异常，会存储
//    @Transactional(value = "jtaTransactionManager", rollbackFor = Exception.class)      //抛异常，会存储
    public void add_rollback_example() {
        PrimaryEntity primaryEntity = new PrimaryEntity();
        primaryEntity.setName("数据源1-实体类");
        primaryRepository.save(primaryEntity);

        //故意抛出异常测试
        int i = 10 / 0;
    }

}
