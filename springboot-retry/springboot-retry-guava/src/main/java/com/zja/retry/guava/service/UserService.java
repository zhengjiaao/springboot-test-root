package com.zja.retry.guava.service;

import com.zja.retry.guava.exception.BusinessException;
import com.zja.retry.guava.exception.DataException;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 17:36
 */
@Service
public class UserService {

    public Callable<Void> findUser1(int e) throws Exception {

        System.out.println("重试...");

        if (1 == e) {
            throw new RuntimeException("查询用户失败"); // 
        } else if (2 == e) {
            throw new DataException("查询用户失败");
        } else if (3 == e) {
            throw new BusinessException("查询用户失败");
        } else if (4 == e) {
            throw new Exception("查询用户失败");
        } else {
            System.out.println("查询用户成功");
        }
        return null;
    }

    public Callable<String> findUser2(int e) throws Exception {

        System.out.println("重试...");

        if (1 == e) {
            throw new RuntimeException("查询用户失败");
        } else if (2 == e) {
            throw new DataException("查询用户失败");
        } else if (3 == e) {
            throw new BusinessException("查询用户失败");
        } else if (4 == e) {
            throw new Exception("查询用户失败");
        } else {
            return () -> "查询用户成功";
        }

    }

}
