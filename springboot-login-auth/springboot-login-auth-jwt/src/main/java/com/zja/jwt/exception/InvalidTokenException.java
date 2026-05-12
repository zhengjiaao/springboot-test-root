package com.zja.jwt.exception;

/**
 * @Author: zhengja
 * @Date: 2025-07-11 16:43
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}