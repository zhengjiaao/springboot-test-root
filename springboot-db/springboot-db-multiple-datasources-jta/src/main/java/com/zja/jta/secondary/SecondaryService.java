/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-25 16:26
 * @Since:
 */
package com.zja.jta.secondary;

import com.zja.jta.secondary.entity.SecondaryEntity;
import com.zja.jta.secondary.repository.SecondaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondaryService {

    @Autowired
    SecondaryRepository secondaryRepository;

    public void add_example() {
        SecondaryEntity secondaryEntity = new SecondaryEntity();
        secondaryEntity.setName("数据源2-实体类");
        secondaryRepository.save(secondaryEntity);
    }

    //无注解时       抛异常，会存储
//    @Transactional(rollbackFor = Exception.class)     //抛异常，不会存储
//    @Transactional(value = "primaryTransactionManager", rollbackFor = Exception.class)  //抛异常，会存储
//    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)  //抛异常，不会存储
//    @Transactional(value = "jtaTransactionManager", rollbackFor = Exception.class)      //抛异常，会存储
    public void add_rollback_example() {
        SecondaryEntity secondaryEntity = new SecondaryEntity();
        secondaryEntity.setName("数据源2-实体类");
        secondaryRepository.save(secondaryEntity);

        //故意抛出异常测试
        int i = 10 / 0;
    }

}
