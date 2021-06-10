package com.zja;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import org.junit.Test;
import org.redisson.api.*;
import reactor.core.publisher.Mono;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-05-31 16:29
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class RedissonClientTests extends BaseTests {

    //Redisson 原生

    //Redisson 使用RFuture在应用异步编程模型的示例
    @Test
    public void testAsyncRedissonClient() {
        // perform operations
        RBucket<String> bucket = redisson.getBucket("simpleObject");
        RFuture<Void> setFuture = bucket.setAsync("This is object value");
        setFuture.onComplete((value, exception) -> {
            // on invocation completion
        });
        RMap<String, String> map = redisson.getMap("simpleMap");
        RFuture<String> putFuture = map.putAsync("mapKey", "This is map value");
        putFuture.onComplete((value, exception) -> {
            System.out.println("previous value: " + value);
        });

        RFuture<String> getFuture = bucket.getAsync();
        getFuture.onComplete((value, exception) -> {
            System.out.println("stored object value: " + value);
        });
        RFuture<String> getMapFuture = map.getAsync("mapKey");
        getMapFuture.onComplete((value, exception) -> {
            System.out.println("stored map value: " + value);
        });
        redisson.shutdown();
    }

    //Redisson 使用Mono在应用反应式编程模型的示例
    @Test
    public void testRedissonReactiveClient() {
        // perform operations
        RBucketReactive<String> bucket = redissonReactiveClient.getBucket("simpleObject");
        Mono<Void> setMono = bucket.set("This is object value");
        setMono.subscribe(value -> {
            // on invocation completion
        });
        RMapReactive<String, String> map = redissonReactiveClient.getMap("simpleMap");
        Mono<String> putMono = map.put("mapKey", "This is map value");
        putMono.subscribe(value -> {
            System.out.println("previous value: " + value);
        });

        Mono<String> getMono = bucket.get();
        getMono.subscribe(value -> {
            System.out.println("stored object value: " + value);
        });
        Mono<String> getMapMono = map.get("mapKey");
        getMapMono.subscribe(value -> {
            System.out.println("stored map value: " + value);
        });
        redisson.shutdown();
    }

    //Redisson 使用RxJava2在应用实现异步和反应式编程的示例
    @Test
    public void testRedissonRxClient() {
        // perform operations
        RBucketRx<String> bucket = redissonRxClient.getBucket("simpleObject");
        Completable completable = bucket.set("This is object value");
        completable.subscribe(() -> {
            // on invocation completion
        });
        RMapRx<String, String> map = redissonRxClient.getMap("simpleMap");
        Maybe<String> putMaybe = map.put("mapKey", "This is map value");
        putMaybe.subscribe(value -> {
            System.out.println("previous value: " + value);
        });

        Maybe<String> getMaybe = bucket.get();
        getMaybe.subscribe(value -> {
            System.out.println("stored object value: " + value);
        });
        Maybe<String> mapGetMaybe = map.get("mapKey");
        mapGetMaybe.subscribe(value -> {
            System.out.println("stored map value: " + value);
        });
        redisson.shutdown();
    }
}
