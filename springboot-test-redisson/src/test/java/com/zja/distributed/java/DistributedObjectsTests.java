package com.zja.distributed.java;

import com.zja.BaseTests;
import com.zja.entity.AnyObject;
import com.zja.entity.SomeObject;
import org.junit.Test;
import org.redisson.api.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 14:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：参考 https://github.com/redisson/redisson/wiki/6.-Distributed-objects
 */
public class DistributedObjectsTests extends BaseTests {

    @Test
    public void testAnyObject() {
        RBucket<AnyObject> bucket = redisson.getBucket("anyObject");

        bucket.set(new AnyObject(1));
        AnyObject obj = bucket.get(); //1

        bucket.trySet(new AnyObject(3)); //false
        bucket.compareAndSet(new AnyObject(4), new AnyObject(5)); //false
        bucket.getAndSet(new AnyObject(6)); //1

        //bucket.get(); //6
    }

    //Code example of Async interface usage:
    @Test
    public void testAsyncAnyObject() {
        RBucket<AnyObject> bucket = redisson.getBucket("anyObject");

        RFuture<Void> future = bucket.setAsync(new AnyObject(1));
        RFuture<AnyObject> objfuture = bucket.getAsync();

        RFuture<Boolean> tsFuture = bucket.trySetAsync(new AnyObject(3));
        RFuture<Boolean> csFuture = bucket.compareAndSetAsync(new AnyObject(4), new AnyObject(5));
        RFuture<AnyObject> gsFuture = bucket.getAndSetAsync(new AnyObject(6));
    }

    //Use RBuckets interface to execute operations over multiple RBucket objects:
    @Test
    public void testBucketObject() {
        RBuckets buckets = redisson.getBuckets();

        Map<String, Object> map = new HashMap<>();
        map.put("myBucket1", new AnyObject(1));
        map.put("myBucket2", new AnyObject(2));

        // sets all or nothing if some bucket is already exists
        buckets.trySet(map);
        // store all at once
        buckets.set(map);

        // get all bucket values  异常
//        Map<String, Object> loadedBuckets = buckets.get("myBucket1");
    }

    //Code example of Async interface usage:
    @Test
    public void testAsyncBucketObject() {
        RBuckets buckets = redisson.getBuckets();

        Map<String, Object> map = new HashMap<>();
        map.put("myBucket1", new AnyObject(1));
        map.put("myBucket2", new AnyObject(2));

        // sets all or nothing if some bucket is already exists
        RFuture<Boolean> tsFuture = buckets.trySetAsync(map);
        // store all at once
        RFuture<Void> sFuture = buckets.setAsync(map);

        // get all bucket values
//        RFuture<Map<String, Object>> bucketsFuture = buckets.getAsync("myBucket1", "myBucket2", "myBucket3");
    }


    @Test
    public void testRBinaryStream() {
        RBinaryStream stream = redisson.getBinaryStream("anyStream");

        byte[] content = "content data".getBytes();
        stream.set(content);
        stream.getAndSet(content);
        stream.trySet(content);
//        stream.compareAndSet(oldContent, content);
    }

    @Test
    public void testAsyncRBinaryStream() {
        RBinaryStream stream = redisson.getBinaryStream("anyStream");

        byte[] content = "content data2".getBytes();
        stream.set(content);
        stream.getAndSet(content);
        stream.trySet(content);
//        stream.compareAndSet(oldContent, content);
    }


    @Test
    public void testInputStreamAndOutputStream() throws IOException {
        RBinaryStream stream = redisson.getBinaryStream("anyStream");

        InputStream is = stream.getInputStream();
        byte[] readBuffer = "content data2".getBytes();
        is.read(readBuffer);

        OutputStream os = stream.getOutputStream();
        byte[] contentToWrite = " data3".getBytes();
        os.write(contentToWrite); //继续追加写
    }

    @Test
    public void testSeekableByteChannel() throws IOException {
        // 从redis anyStream中读取
        RBinaryStream stream = redisson.getBinaryStream("anyStream");
        SeekableByteChannel sbc = stream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("d:\\Temp\\NioTest3.txt");
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer readBuffer = ByteBuffer.allocate(512);

        while (true) {
            readBuffer.clear();
            int read = sbc.read(readBuffer); //读取
            System.out.println(read);
            if (-1 == read) {
                break;
            }
            readBuffer.flip();
            outputChannel.write(readBuffer); //输出
        }
        outputStream.close();

        sbc.position(0); //从0位置开始操作
        String s = "I was here!/n";
        byte data[] = s.getBytes();
        ByteBuffer contentToWrite = ByteBuffer.wrap(data);
        sbc.write(contentToWrite);  //写入

        //sbc.truncate(5);  // 截切保留
    }

    @Test
    public void testAsynchronousByteChannel() throws IOException {
        // 从redis anyStream中读取
        RBinaryStream stream = redisson.getBinaryStream("anyStream");

        AsynchronousByteChannel sbc = stream.getAsynchronousChannel();

        ByteBuffer readBuffer = ByteBuffer.allocate(512);
        Future<Integer> read = sbc.read(readBuffer);//读取

        String s = "I was here! 2 /n";
        byte data[] = s.getBytes();
        ByteBuffer contentToWrite = ByteBuffer.wrap(data);
        sbc.write(contentToWrite);  //写入
    }


    //Java implementation of Redis based RBitSet object provides API similar to java.util.BitSet. It represents vector of bits that grows as needed. Size limited by Redis to 4 294 967 295 bits
    @Test
    public void testBitSet() {
        RBitSet set = redisson.getBitSet("simpleBitset");

        set.set(0, true);
        set.set(1812, false);

        set.clear(0);

        set.and("anotherBitset");
        set.xor("anotherBitset");
    }

    @Test
    public void testAsyncBitSet() {
        RBitSetAsync set = redisson.getBitSet("simpleBitset");

//        RFuture<Boolean> setFuture = set.setAsync(0, true);
        RFuture<Boolean> setFuture = set.setAsync(1812, false);

        RFuture<Boolean> clearFuture = set.clearAsync(0);

        RFuture<Void> andFuture = set.andAsync("anotherBitset");
        RFuture<Void> xorFuture = set.xorAsync("anotherBitset");
    }

    //Java implementation of Redis based AtomicLong object provides API similar to java.util.concurrent.atomic.AtomicLong object
    @Test
    public void testRAtomicLong() {
        RAtomicLong atomicLong = redisson.getAtomicLong("myAtomicLong");
        atomicLong.set(3);
        atomicLong.incrementAndGet(); //4
        atomicLong.get(); //4
    }

    @Test
    public void testAsyncRAtomicLong() {
        RAtomicLongAsync atomicLong = redisson.getAtomicLong("myAtomicLong");

        RFuture<Void> setFuture = atomicLong.setAsync(3);
        RFuture<Long> igFuture = atomicLong.incrementAndGetAsync();
        RFuture<Long> getFuture = atomicLong.getAsync();
    }

    //Java implementation of Redis based AtomicDouble object.
    @Test
    public void testAtomicDouble() {
        RAtomicDouble atomicDouble = redisson.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(2.81);
        atomicDouble.addAndGet(4.11); //6.92
        atomicDouble.get(); //6.92
    }

    @Test
    public void testAsyncAtomicDouble() throws InterruptedException, ExecutionException {
        RAtomicDoubleAsync atomicDouble = redisson.getAtomicDouble("myAtomicDouble");

        RFuture<Void> setFuture = atomicDouble.setAsync(2.81);
        RFuture<Double> agFuture = atomicDouble.addAndGetAsync(4.11);
        RFuture<Double> getFuture = atomicDouble.getAsync();

        getFuture.await(2,TimeUnit.SECONDS);
        System.out.println(getFuture.get()); //2.8

    }

    //Redis based distributed RBloomFilter bloom filter for Java. Number of contained bits is limited to 2^32.
    //
    //Must be initialized with capacity size by tryInit(expectedInsertions, falseProbability) method before usage
    @Test
    public void testBloomFilter() {
        RBloomFilter<SomeObject> bloomFilter = redisson.getBloomFilter("sample");
        // initialize bloom filter with
        // expectedInsertions = 55000000
        // falseProbability = 0.03
        bloomFilter.tryInit(55000000L, 0.03);

        bloomFilter.add(new SomeObject("field1Value", "field2Value"));
        bloomFilter.add(new SomeObject("field5Value", "field8Value"));

        boolean contains = bloomFilter.contains(new SomeObject("field1Value", "field8Value"));
        long count = bloomFilter.count();
        System.out.println(contains);  //false
        System.out.println(count); //2
    }

    //Redis based distributed RHyperLogLog object for Java. Probabilistic data structure that lets you maintain counts of millions of items with extreme space efficiency
    @Test
    public void testHyperLogLog() {
        RHyperLogLog<Integer> log = redisson.getHyperLogLog("log");
        log.add(1);
        log.add(2);
        log.add(3);

        log.count();
    }

    //Java implementation of Redis based LongAdder object provides API similar to java.util.concurrent.atomic.LongAdder object.
    @Test
    public void testLongAdder() {
        RLongAdder atomicLong = redisson.getLongAdder("myLongAdder");
        atomicLong.add(12);
        atomicLong.increment();
        atomicLong.decrement();
        atomicLong.sum();

        //Object should be destroyed if it's not used anymore, but it's not necessary to call destroy method if Redisson goes shutdown.
        atomicLong.destroy();
    }

    //Redis based distributed RateLimiter object for Java restricts the total rate of calls either from all threads regardless of Redisson instance or from all threads working with the same Redisson instance. Doesn't guarantee fairness.
    @Test
    public void testRateLimiter() {
        RRateLimiter limiter = redisson.getRateLimiter("myLimiter");
        // Initialization required only once.
        // 5 permits per 2 seconds
        limiter.trySetRate(RateType.OVERALL, 5, 2, RateIntervalUnit.SECONDS);

        // acquire 3 permits or block until they became available
        limiter.acquire(3);
    }


    @Test
    public void testIdGenerator() {
        RIdGenerator generator = redisson.getIdGenerator("generator");

        // Initialize with start value = 12 and allocation size = 20000
        generator.tryInit(12, 20000);

        for (int i = 0; i < 1000000; ++i) {
            long id = generator.nextId();
            System.out.println(i + "_id: " + id);
        }
    }

    @Test
    public void testAsyncIdGenerator() throws InterruptedException, ExecutionException {
        RIdGenerator generator = redisson.getIdGenerator("generator");

        // Initialize with start value = 12 and allocation size = 20000
        RFuture<Boolean> initFuture = generator.tryInitAsync(12, 20000);

        RFuture<Long> idFuture = generator.nextIdAsync();

        idFuture.await(2, TimeUnit.SECONDS);
        System.out.println(idFuture.get());
    }

}
