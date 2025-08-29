package com.zja.process.exception;

/**
 * @Author: zhengja
 * @Date: 2025-08-15 14:50
 */
public class FormNotFoundException extends RuntimeException {
    public FormNotFoundException(String message) {
        super(message);
    }

    public FormNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}