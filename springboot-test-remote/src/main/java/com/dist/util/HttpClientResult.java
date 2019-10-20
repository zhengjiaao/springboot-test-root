package com.dist.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/28 9:57
 */
@Data
public class HttpClientResult implements Serializable {

    //响应状态码
    private int code;

    //响应String数据
    private String content;

    //响应byte 字节数据
    private byte[] bytes;

    public HttpClientResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public HttpClientResult(int code) {
        this.code = code;
    }

    public HttpClientResult(int code, byte[] bytes) {
        this.code = code;
        this.bytes = bytes;
    }

}
