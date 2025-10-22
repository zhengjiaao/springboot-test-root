package com.zja.table;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:54
 */
public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {
        super(message);
    }
}