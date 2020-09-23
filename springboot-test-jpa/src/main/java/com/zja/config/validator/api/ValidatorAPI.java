package com.zja.config.validator.api;

import com.zja.config.validator.entity.ValidResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 15:46
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义验证参数工具类
 */
public class ValidatorAPI {

    /**
     * 校验类
     */
    private Validator validator;

    /**
     * 有参构造
     */
    public ValidatorAPI(Validator validator) {
        this.validator = validator;
    }

    /**
     * 支持子类继承扩展
     * @param validator
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * 校验对象
     *
     * @param t      bean
     * @param groups 校验组
     * @return ValidResult 校验结果类
     */
    public <T> ValidResult validateBean(T t, Class<?>... groups) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验bean的某一个属性
     *
     * @param obj          bean
     * @param propertyName 属性名称
     * @return ValidResult 校验结果类
     */
    public <T> ValidResult validateProperty(T obj, String propertyName) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(propertyName, violation.getMessage());
            }
        }
        return result;
    }

}
