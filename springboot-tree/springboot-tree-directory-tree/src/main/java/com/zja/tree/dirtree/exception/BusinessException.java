package com.zja.tree.dirtree.exception;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 11:17
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}