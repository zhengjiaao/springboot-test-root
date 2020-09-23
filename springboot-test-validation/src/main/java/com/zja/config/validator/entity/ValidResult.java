package com.zja.config.validator.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-18 13:13
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：校验结果类
 */
@Data
public class ValidResult {

    /**
     * 是否有参数错误
     */
    private boolean hasErrors;

    /**
     * 错误信息
     */
    private List<ErrorMessage> errors;

    public ValidResult() {
        this.errors = new ArrayList<>();
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    /**
     * 是否有参数错误 true有错误信息/false无错误信息
     * @return
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * 添加错误信息
     * @param propertyName 错误字段
     * @param message 错误信息
     */
    public void addError(String propertyName, String message) {
        this.errors.add(new ErrorMessage(propertyName, message));
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    /**
     * 获取所有验证信息
     * @return 集合形式
     */
    public List<ErrorMessage> getListErrors() {
        return errors;
    }

    /**
     * 获取所有验证信息
     * @return 字符串形式
     */
    public String getStringErrors(){
        StringBuilder sb = new StringBuilder();
        for (ErrorMessage error : errors) {
            sb.append(error.getProperty()).append(":").append(error.getMessage()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 获取所有验证信息
     * @return  Map key:value 形式
     */
    public Map getMapErrors(){
        Map<String, String> map = new ConcurrentHashMap<>();
        for (ErrorMessage error : errors) {
            map.put(error.getProperty(),error.getMessage());
        }
        return map;
    }

}
