package com.zja.config.validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 18:52
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Validator 后台验证参数
 */
@Configuration
public class ValidatorConfiguration {

    /**
     * 默认是普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        //默认是普通模式，会返回所有的验证不通过信息集合
        return new MethodValidationPostProcessor();
    }

    /**
     * 快速失败返回模式(只要有一个验证失败，则返回)
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(@Qualifier("verifyOne") Validator verifyAll) {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        //设置validator模式为快速失败返回
        postProcessor.setValidator(verifyAll);
        return postProcessor;
    }

    /***
     * 普通模式：(会校验完所有的属性，然后返回所有的验证失败信息)
     * failFast:false 普通模式  true  快速失败返回模式
     */
    @Bean("verifyAll")
    public Validator verifyAll() {
        //两种方式，注解不同
        /*ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(false)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();*/

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "false")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }


    /**
     * 快速失败返回模式(只要有一个验证失败，则返回)
     * failFast:true  快速失败返回模式    false 普通模式
     */
    @Bean("verifyOne")
    public Validator verifyOne() {
        //两种方式，注解不同
        /*ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                //failFast的意思只要出现校验失败的情况，就立即结束校验，不再进行后续的校验
                .failFast(true)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();*/

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }

    /**
     * 普通模式
     *
     * @param validator
     */
    @Bean("validatorAPIAll")
    public ValidatorAPI validatorAPIAll(@Qualifier("verifyAll") Validator validator) {
        return new ValidatorAPI(validator);
    }

    /**
     * 快速失败返回模式
     *
     * @param validator
     */
    @Bean("validatorAPIOne")
    public ValidatorAPI validatorAPIOne(@Qualifier("verifyOne") Validator validator) {
        return new ValidatorAPI(validator);
    }

}
