package com.zja.retry.aop.service;

import com.zja.retry.aop.aspect.Backoff;
import com.zja.retry.aop.aspect.Retryable;
import com.zja.retry.aop.exception.BusinessException;
import com.zja.retry.aop.exception.DataException;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 17:36
 */
@Service
public class UserService {

    // 场景：数据库连接偶尔失败。当添加失败时，会进行最多 2 次重试，每次重试的间隔时间为 500 毫秒
    // @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))  // 默认所有异常都可以触发重试
    @Retryable(include = {DataException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void findUser1(int e) throws Exception {

        System.out.println("重试..."); // maxAttempts = 3，重试3次(包含第一次 调用)

        if (1 == e) {
            throw new RuntimeException("查询用户失败"); // 添加 value = {RuntimeException.class}，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else if (2 == e) {
            throw new DataException("查询用户失败"); // 添加 value = {DataException.class}，只有 DataException 会重试
        } else if (3 == e) {
            throw new BusinessException("查询用户失败"); // 添加 value = {BusinessException.class}，只有 BusinessException 会重试
        } else if (4 == e) {
            throw new Exception("查询用户失败"); // 添加 value = {Exception.class}，，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else {
            System.out.println("查询用户成功");
        }
    }

    // 场景：调用第三方 API 经常超时。当添加失败时，会进行最多 3 次重试，每次重试的间隔时间为 2000 毫秒，且每次重试的时间间隔会逐渐增加（即第一次重试间隔为 2000 毫秒，第二次为 4000 毫秒，第三次为 8000 秒）
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2)) // 默认所有异常都可以触发重试
    // @Retryable(include = {DataException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String findUser2(int e) throws Exception {

        System.out.println("重试..."); // maxAttempts = 3，重试3次(包含第一次 调用)

        if (1 == e) {
            throw new RuntimeException("查询用户失败"); // 添加 value = {RuntimeException.class}，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else if (2 == e) {
            throw new DataException("查询用户失败"); // 添加 value = {DataException.class}，只有 DataException 会重试
        } else if (3 == e) {
            throw new BusinessException("查询用户失败"); // 添加 value = {BusinessException.class}，只有 BusinessException 会重试
        } else if (4 == e) {
            throw new Exception("查询用户失败"); // 添加 value = {Exception.class}，，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else {
            return "查询用户成功";
        }

    }

}
