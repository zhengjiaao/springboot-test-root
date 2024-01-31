/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-17 10:50
 * @Since:
 */
package com.zja.obfuscated.allatori.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.persistence.EntityManager;

/**
 * QueryDSL 在支持JPA的同时，也提供了对 Hibernate 的支持。
 *
 * @author: zhengja
 * @since: 2023/08/17 10:50
 */
@Lazy
@Configuration
public class QueryDSLConfig {

    @Bean
    public JPAQueryFactory queryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
