package com.dist.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Date: 2019-11-22 13:39
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Hibernate JPA自定义Id主键递增趋势策略
 */
public class MyIdGenerator implements IdentifierGenerator {

    /**
     * jpa自定义主键生成策略: 与底层数据库无关，只与代码有关
     * @param sharedSessionContractImplementor
     * @param o
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        //雪花算法：不是递增，是递增趋势
        SnowFlake snowFlake = new SnowFlake(2, 3);

        long start = System.currentTimeMillis();
        long id = snowFlake.nextId();
        System.out.println("雪花算法id: "+id);
        //100万个id大概5~12秒
        System.out.println(System.currentTimeMillis() - start);

        return id;
    }
}
