package com.zja.retry.service;

import com.zja.retry.exception.BusinessException;
import com.zja.retry.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * 订单
 *
 * @Author: zhengja
 * @Date: 2024-10-28 17:37
 */
@Slf4j
@Service
public class OrderService {

    // 场景：数据库连接偶尔失败。当添加失败时，会进行最多 2 次重试，每次重试的间隔时间为 500 毫秒
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))  // 默认所有异常都可以触发重试
    // @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void findOrder1(int e) throws Exception {

        System.out.println("重试..."); // maxAttempts = 3，重试3次(包含第一次 调用)

        if (1 == e) {
            throw new RuntimeException("查询订单失败"); // 添加 value = {RuntimeException.class}，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else if (2 == e) {
            throw new DataException("查询订单失败"); // 添加 value = {DataException.class}，只有 DataException 会重试
        } else if (3 == e) {
            throw new BusinessException("查询订单失败"); // 添加 value = {BusinessException.class}，只有 BusinessException 会重试
        } else if (4 == e) {
            throw new Exception("查询订单失败"); // 添加 value = {Exception.class}，，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else {
            System.out.println("查询订单成功");
        }

    }

    // 场景：调用第三方 API 经常超时。当添加失败时，会进行最多 3 次重试，每次重试的间隔时间为 2000 毫秒，且每次重试的时间间隔会逐渐增加（即第一次重试间隔为 2000 毫秒，第二次为 4000 毫秒，第三次为 8000 秒）
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2)) // 默认所有异常都可以触发重试
    // @Retryable(value = {DataException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String findOrder2(int e) throws Exception {

        System.out.println("重试..."); // maxAttempts = 3，重试3次(包含第一次 调用)

        if (1 == e) {
            throw new RuntimeException("查询订单失败"); // 添加 value = {RuntimeException.class}，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else if (2 == e) {
            throw new DataException("查询订单失败"); // 添加 value = {DataException.class}，只有 DataException 会重试
        } else if (3 == e) {
            throw new BusinessException("查询订单失败"); // 添加 value = {BusinessException.class}，只有 BusinessException 会重试
        } else if (4 == e) {
            throw new Exception("查询订单失败"); // 添加 value = {Exception.class}，，任何异常都会重试,例如：DataException、BusinessException、RuntimeException、Exception
        } else {
            return "查询订单成功";
        }

    }

    // @Recover 兜底，当 @Retryable 标记的方法重试次数耗尽后自动调用 @Recover 方法,执行的回调函数。

    // --------- 回调方法，无返回值，仅findOrder1方法生效

  /*  @Recover
    public void recover(RuntimeException e) {
        log.error("recover-1，达到最大重试次数", e);
    }

    @Recover
    public void recover(DataException e) {
        log.error("recover-2，达到最大重试次数", e);
    }

    @Recover
    public void recover(BusinessException e) {
        log.error("recover-3，达到最大重试次数", e);
    }

    @Recover
    public void recover(Exception e) {
        log.error("recover-4，达到最大重试次数", e);
    }*/

    // --------- 回调方法，带返回值，仅findOrder2方法生效

    @Recover
    public String recover(RuntimeException e) {
        log.error("recover-1，达到最大重试次数", e);
        return "达到最大重试次数-查询订单成功";
    }

    @Recover
    public String recover(DataException e) {
        log.error("recover-2，达到最大重试次数", e);
        return "达到最大重试次数-查询订单成功";
    }

    @Recover
    public String recover(BusinessException e) {
        log.error("recover-3，达到最大重试次数", e);
        return "达到最大重试次数-查询订单成功";
    }

    @Recover
    public String recover(Exception e) {
        log.error("recover-4，达到最大重试次数", e);
        return "达到最大重试次数-查询订单成功";
    }
}
