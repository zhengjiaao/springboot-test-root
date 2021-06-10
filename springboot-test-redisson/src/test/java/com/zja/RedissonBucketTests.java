package com.zja;

import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 13:25
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class RedissonBucketTests extends BaseTests {

    @Test
    public void testBucket() {
        //自定义序列化编码方式
        /*Codec codec = new FastjsonCodec();
        redisson.getBucket("BucketA",codec);*/

        RBucket<Object> bucket = redisson.getBucket("BucketA");
        bucket.set(111); //创建桶，设置桶存储的对象
        //桶自动过期时间60秒
//        bucket.set("222", 60, TimeUnit.SECONDS);
        Object o = bucket.get(); //返回存储桶对象
        long size = bucket.size(); //桶存储对象的大小
    }

    @Test
    public void testBucket_trySet() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
        //尝试以原子方式将元素设置为不存在的桶中
        //存在桶，则设置失败 false
        boolean trySet = bucket.trySet("555");//尝试设置桶的新值
//        boolean trySet1 = bucket.trySet("444", 60, TimeUnit.SECONDS);//可设置桶超时时间
    }

    @Test
    public void testBucket_SetIfExists() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
        bucket.set("333");

        //桶不存在，则设置失败
        bucket.setIfExists("111"); //仅当桶存在才会设置对象值
//        boolean b = bucket.setIfExists("222", 60, TimeUnit.SECONDS); //可设置桶过期时间
        Object o = bucket.get(); //111
    }

    @Test
    public void testBucket_compareAndSet() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
//        bucket.set("aaa");
        //仅当当前值等于预期值，以原子方式将值设置为给定的更新值
        //桶不存在或不是预期值，新值将设置失败
        boolean b = bucket.compareAndSet("aaa", "222");
    }

    @Test
    public void testBucket_getAndSet() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
        //返回桶的旧值，设置新值, 桶不存在 返回null，并设置新值
        Object o1 = bucket.getAndSet("777");
//        Object ddd = bucket.getAndSet("888", 50, TimeUnit.SECONDS);
    }

    @Test
    public void testBucket_setAsync() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
        RFuture<Void> voidRFuture = bucket.setAsync("555");//异步设置桶的新值
//        RFuture<Void> voidRFuture1 = bucket.setAsync("666", 5, TimeUnit.SECONDS);//可设置超时时间
    }

    @Test
    public void testBucket_getAndDelete() {
        RBucket<Object> bucket = redisson.getBucket("BucketA");
        //返回并删除存储桶对象
        Object bucketAndDelete = bucket.getAndDelete();
    }

}
