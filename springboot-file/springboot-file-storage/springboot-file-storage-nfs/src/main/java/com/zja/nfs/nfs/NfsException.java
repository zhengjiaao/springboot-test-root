package com.zja.nfs.nfs;

/**
 * @Author: zhengja
 * @Date: 2025-06-12 14:35
 */
public class NfsException extends Exception {
    public NfsException(String message) {
        super(message);
    }

    public NfsException(String message, Throwable cause) {
        super(message, cause);
    }
}