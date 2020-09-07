package com.dist.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-07 14:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Hibernate-validator 参数检验配置
 */
@Configuration
public class ValidatorConfig {

    /**
     * hibernate.validator.fail_fast：true  快速失败返回模式    false 普通模式
     * 默认为普通模式：
     * 1.普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
     * 2.快速失败返回模式(只要有一个验证失败，则返回)
     */
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }
}
