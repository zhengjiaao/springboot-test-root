package com.zja.retry.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 15:12
 */
@Aspect
@Component
public class RetryableAspect {

    @Around("@annotation(retryable)")
    public Object retryableAdvice(ProceedingJoinPoint joinPoint, Retryable retryable) throws Throwable {
        int maxAttempts = retryable.maxAttempts();
        Backoff backoff = retryable.backoff();
        long delay = backoff.delay();
        long maxDelay = backoff.maxDelay();
        double multiplier = backoff.multiplier();
        Class<? extends Throwable>[] includes = retryable.include();
        Class<? extends Throwable>[] excludes = retryable.exclude();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (isIncluded(e, includes) && !isExcluded(e, excludes)) {
                    if (attempt < maxAttempts) {
                        long currentDelay = (long) (delay * Math.pow(multiplier, attempt - 1));
                        if (maxDelay > 0 && currentDelay > maxDelay) {
                            currentDelay = maxDelay;
                        }
                        TimeUnit.MILLISECONDS.sleep(currentDelay);
                    } else {
                        throw e;
                    }
                } else {
                    throw e;
                }
            }
        }
        return null;
    }

    private boolean isIncluded(Throwable e, Class<? extends Throwable>[] includes) {
        if (includes.length == 0) {
            return true;
        }
        for (Class<? extends Throwable> include : includes) {
            if (include.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcluded(Throwable e, Class<? extends Throwable>[] excludes) {
        for (Class<? extends Throwable> exclude : excludes) {
            if (exclude.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
