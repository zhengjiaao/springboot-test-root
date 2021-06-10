package com.zja.execution;

import com.zja.BaseTests;
import org.junit.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RAtomicLongAsync;
import org.redisson.api.RFuture;

import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-03 13:21
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class AsyncWay extends BaseTests {

    /**
     * async-way
     *
     * Redisson supports auto-retry policy for each operation and tries to send command during each attempt. Retry policy controlled by retryAttempts (default is 3) and retryInterval (default is 1000 ms) settings. Each attempt executed after retryInterval time interval.
     *
     * Redisson instances are fully thread-safe. Objects with synchronous/asynchronous methods could be reached via RedissonClient interface.
     * Reactive and RxJava3 methods through RedissonReactiveClient and RedissonRxClient interfaces respectively.
     */

    @Test
    public void testAsync() throws InterruptedException {
        RAtomicLong longObject = redisson.getAtomicLong("myLong");
        // sync way
        boolean b = longObject.compareAndSet(3, 401);
        // async way
        RFuture<Boolean> result = longObject.compareAndSetAsync(3, 401);

        result.await(2, TimeUnit.SECONDS);

       /* RedissonReactiveClient client = Redisson.createReactive(config);
        RAtomicLongReactive longObject = client.getAtomicLong('myLong');
        // reactive way
        Mono<Boolean> result = longObject.compareAndSet(3, 401);*/

        /*RedissonRxClient client = Redisson.createRx(config);
        RAtomicLongRx longObject= client.getAtomicLong("myLong");
        // RxJava2 way
        Flowable<Boolean result = longObject.compareAndSet(3, 401);*/
    }

    //Async way
    //Most Redisson objects extend asynchronous interface with asynchronous methods which mirror to synchronous methods. Like this:
    @Test
    public void testAsync2(){
        // RAtomicLong extends RAtomicLongAsync
        RAtomicLongAsync longObject = redisson.getAtomicLong("myLong");
        RFuture<Boolean> future = longObject.compareAndSetAsync(1, 401);
    }

    //Asynchronous method returns extended RFuture object extending CompletionStage and Future interfaces
    @Test
    public void testAsync3(){
        RAtomicLongAsync longObject = redisson.getAtomicLong("myLong");
        RFuture<Boolean> future = longObject.compareAndSetAsync(1, 401);

        future.whenComplete((res, exception) -> {
            // handle both result and exception
        });

        // or
        /*future.thenAccept(res -> {
            // handle result
        }).exceptionally(exception -> {
            // handle exception
        });*/
    }

    //Avoid to use blocking methods in future listeners. Listeners executed by netty-threads and delays in listeners may cause errors in Redis request/response processing. Use follow methods to execute blocking methods in listeners
    @Test
    public void testAsync4(){
        RAtomicLongAsync longObject = redisson.getAtomicLong("myLong");
        RFuture<Boolean> future = longObject.compareAndSetAsync(1, 401);

       /* future.whenCompleteAsync((res, exception) -> {
            // handle both result and exception
        }, executor);
*/

        // or
        /*future.thenAcceptAsync(res -> {
            // handle result
        }, executor).exceptionallyAsync(exception -> {
            // handle exception
        }, executor);*/
    }


}
