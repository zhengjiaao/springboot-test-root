package com.dist.util.exception;

/**
 * 解密异常
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class DecryptException extends Exception{

    private static String DECRYPT_EXCEPTION = "密文无法解密";

    public DecryptException() {
        super(DECRYPT_EXCEPTION);
    }

    public DecryptException(String msg) {
        super(msg);
    }
}
