package com.zja.retry.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 15:31
 */
@Slf4j
public class RetryTest {

    //  实现一个自定义重试策略：重试一定次数，每次间隔一定时间
    @Test
    public void test_1() throws Exception {
        CheckTaskResultVO taskResultWithRetries = getTaskResultWithRetriesV1("1234");
        System.out.println(taskResultWithRetries);
    }

    public CheckTaskResultVO getTaskResultWithRetriesV1(String projectId) throws MaxRetriesExceededException, InterruptedException {
        // 获取任务结果
        CheckTaskResultVO taskResultVO = null;
        int maxRetries = 10; // 假设最多重试10次
        int currentRetry = 0;

        while (currentRetry < maxRetries) {
            try {
                taskResultVO = getReviewResult(projectId);
                if ("RUNNING".equals(taskResultVO.getStatus())) {
                    currentRetry++;
                    log.info("Task is still running, retrying... (剩余重试次数: {})", maxRetries - currentRetry);
                    Thread.sleep(3000);
                } else if ("SUCCEED".equals(taskResultVO.getStatus())) {
                    break;
                } else {
                    throw new IllegalStateException("Unexpected task status: " + taskResultVO.getStatus());
                }
            } catch (Exception e) {
                log.error("Error occurred during task retrieval: ", e);
                currentRetry++;
                if (currentRetry >= maxRetries) {
                    throw new MaxRetriesExceededException("Maximum retries exceeded：" + maxRetries);
                }
                Thread.sleep(3000); // 继续尝试
            }
        }

        log.info("合规性审查任务结果: {}", taskResultVO);
        return taskResultVO;
    }

    //  实现一个自定义重试策略：最多等待一小时，每次间隔一定时间
    @Test
    public void test_2() throws Exception {
        CheckTaskResultVO taskResultWithRetries = getTaskResultWithRetriesV2("123");
        System.out.println(taskResultWithRetries);
    }

    private CheckTaskResultVO getTaskResultWithRetriesV2(String projectId) throws MaxRetriesExceededException, InterruptedException {
        long maxWaitTimeMillis = 60 * 60 * 1000; // 1小时
        long startTime = System.currentTimeMillis();
        long currentTime;

        while ((currentTime = System.currentTimeMillis()) - startTime < maxWaitTimeMillis) {
            CheckTaskResultVO taskResultVO = getReviewResult(projectId);

            switch (taskResultVO.getStatus()) {
                case "RUNNING":
                    log.info("Task is still running, retrying... (Elapsed time: {} ms)", currentTime - startTime);
                    Thread.sleep(3000);
                    break;
                case "SUCCEED":
                    log.info("Task succeeded");
                    return taskResultVO;
                default:
                    log.error("Unexpected task status: {}", taskResultVO.getStatus());
                    throw new IllegalStateException("Unexpected task status: " + taskResultVO.getStatus());
            }
        }

        log.error("Maximum wait time exceeded: {} ms", maxWaitTimeMillis);
        throw new MaxRetriesExceededException("Maximum wait time exceeded: " + maxWaitTimeMillis);
    }

    private CheckTaskResultVO getReviewResult(String projectId) {
        if ("123".equals(projectId)) {
            return new CheckTaskResultVO("SUCCEED", "success", "data");
        }
        return new CheckTaskResultVO("RUNNING", "running", "data");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CheckTaskResultVO {
        private String status;
        private String message;
        private String data;
    }

    static class MaxRetriesExceededException extends Exception {
        public MaxRetriesExceededException(String message) {
            super(message);
        }
    }
}
