package com.zja.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/19 16:38
 */
public class EpoitParamsDTO implements Serializable{

    private String token;
    private Map<String,String> params;

    public EpoitParamsDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "EpoitParamsDTO{" +
                "token='" + token + '\'' +
                ", params=" + params +
                '}';
    }
}
