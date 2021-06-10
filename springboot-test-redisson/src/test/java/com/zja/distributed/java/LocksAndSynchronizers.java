package com.zja.distributed.java;

import com.zja.BaseTests;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 17:05
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：分布式锁和同步器
 * 参考 https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers
 */
public class LocksAndSynchronizers extends BaseTests {

    /****************************** 测试 分布式锁 Lock  start*****************************/

    /**
     * Lock
     *
     * Redis based distributed reentrant Lock object for Java and implements Lock interface.
     *
     * If Redisson instance which acquired lock crashes then such lock could hang forever in acquired state. To avoid this Redisson maintains lock watchdog, it prolongs lock expiration while lock holder Redisson instance is alive. By default lock watchdog timeout is 30 seconds and can be changed through Config.lockWatchdogTimeout setting.
     *
     * Also Redisson allow to specify leaseTime parameter during lock acquisition. After specified time interval locked lock will be released automatically.
     *
     * RLock object behaves according to the Java Lock specification. It means only lock owner thread can unlock it otherwise IllegalMonitorStateException would be thrown. Otherwise consider to use RSemaphore object
     */
    @Test
    public void testLock() throws InterruptedException {
        RLock lock = redisson.getLock("myLock");

        // traditional lock method
//        lock.lock(); //默认 永久持有锁,持有锁30秒后会自动释放锁
        // or acquire lock and automatically unlock it after 10 seconds
//        lock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds  等待锁定获取长达 100 秒
        // and automatically unlock it after 10 seconds   获取到锁,10秒后自动解锁
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);

        if (res) {
            try {
                System.out.println("成功获取锁");
                // 业务操作...

            } finally {
                //会出现异常: 当前线程未完成锁就释放了,会抛异常
//                lock.unlock();

                //解决异常 java.lang.IllegalMonitorStateException: attempt to unlock lock, not locked by current thread by node id: 41d048e1-e025-4ccf-9f5b-34d221c29526 thread-id: 1
                if (lock.isLocked()) { // 是否还是锁定状态
                    if (lock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
                        System.out.println("释放锁");
                        lock.unlock(); // 释放锁
                    }
                } else {
                    System.out.println("锁已经自动释放");
                }
            }
        } else {
            System.out.println("未获取锁");
        }


        /*try {
            lock.lock();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }*/
    }

    @Test
    public void testAsyncLock() throws InterruptedException {
        RLock lock = redisson.getLock("myLock");

//        RFuture<Void> lockFuture = lock.lockAsync();

        // or acquire lock and automatically unlock it after 10 seconds
//        RFuture<Void> lockFuture = lock.lockAsync(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        lockFuture.await(2, TimeUnit.SECONDS);

        lockFuture.whenComplete((res, exception) -> {
            // 业务操作...
            if (lock.isLocked()) { // 是否还是锁定状态
                if (lock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
                    System.out.println("释放锁");
                    lock.unlockAsync(); // 释放锁
                }
            } else {
                System.out.println("锁已经自动释放");
            }
        });
    }


    //公平锁（可重入锁）
    //Fair Lock
    //Redis based distributed reentrant fair Lock object for Java implements Lock interface.
    //
    //Fair lock guarantees that threads will acquire it in is same order they requested it. All waiting threads are queued and if some thread has died then Redisson waits its return for 5 seconds. For example, if 5 threads are died for some reason then delay will be 25 seconds
    //公平锁, 保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程
    @Test
    public void testFairLock() throws InterruptedException {
        RLock lock = redisson.getFairLock("myLock");

        // traditional lock method
        // 最常见的使用方法
//        lock.lock();

        // or acquire lock and automatically unlock it after 10 seconds
        // 支持过期解锁功能, 10秒钟以后自动解锁,无需调用unlock方法手动解锁
//        lock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
                //业务...
            } finally {
                lock.unlock();
            }
        }
    }

    @Test
    public void testAsyncFairLock() throws InterruptedException {
        RLock lock = redisson.getFairLock("myLock");

//        RFuture<Void> lockFuture = lock.lockAsync();

        // or acquire lock and automatically unlock it after 10 seconds
//        RFuture<Void> lockFuture = lock.lockAsync(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

//        lockFuture.await(2,TimeUnit.SECONDS);

        lockFuture.whenComplete((res, exception) -> {
            // ...
            lock.unlockAsync();
        });
    }

    /**
     * MultiLock 多锁
     *
     * Redis based distributed MultiLock object allows to group Lock objects and handle them as a single lock. Each RLock object may belong to different Redisson instances.
     */
    @Test
    public void testMultiLock() throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.159.128:6379");

        RedissonClient redisson1 = Redisson.create(config);
        RedissonClient redisson2 = Redisson.create(config);
        RedissonClient redisson3 = Redisson.create(config);
        RLock lock1 = redisson1.getLock("lock1");
        RLock lock2 = redisson2.getLock("lock2");
        RLock lock3 = redisson3.getLock("lock3");

        RLock multiLock = redisson.getMultiLock(lock1, lock2, lock3);

        // traditional lock method
//        multiLock.lock();

        // or acquire lock and automatically unlock it after 10 seconds
//        multiLock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁, 将多个锁同时释放
        boolean res = multiLock.tryLock(100, 10, TimeUnit.SECONDS);

        if (res) {
            try {
                System.out.println("成功获取锁");
                // 业务操作...
                Thread.sleep(5000); //业务操作时间5秒
            } finally {
                multiLock.unlock();
                System.out.println("释放锁");
            }
        } else {
            System.out.println("未获取锁");
        }
    }

    @Test
    public void testAsyncMultiLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.159.128:6379");

        RedissonClient redisson1 = Redisson.create(config);
        RedissonClient redisson2 = Redisson.create(config);
        RedissonClient redisson3 = Redisson.create(config);
        RLock lock1 = redisson1.getLock("lock1");
        RLock lock2 = redisson2.getLock("lock2");
        RLock lock3 = redisson3.getLock("lock3");

        RLock multiLock = redisson.getMultiLock(lock1, lock2, lock3);

//        RFuture<Void> lockFuture = multiLock.lockAsync();

        // or acquire lock and automatically unlock it after 10 seconds
//        RFuture<Void> lockFuture = multiLock.lockAsync(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        RFuture<Boolean> lockFuture = multiLock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        lockFuture.whenComplete((res, exception) -> {
            // ...
            multiLock.unlockAsync();
        });
    }

    /**
     * ReadWriteLock 读写锁  多个读锁,一个写锁
     *
     * Redis based distributed reentrant ReadWriteLock object for Java implements ReadWriteLock interface. Both Read and Write locks implement RLock interface
     */
    @Test
    public void testReadWriteLock() throws InterruptedException {
        RReadWriteLock rwlock = redisson.getReadWriteLock("rwlock");

        RLock lock = rwlock.readLock();
        // or
//        RLock lock = rwlock.writeLock();

        // traditional lock method
//        lock.lock();

        // or acquire lock and automatically unlock it after 10 seconds
//        lock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
                System.out.println("成功获取锁");
                // 业务操作...
                Thread.sleep(5000);

            } finally {
                //会出现异常: 当前线程未完成锁就释放了,会抛异常
//                lock.unlock();

                //解决异常 java.lang.IllegalMonitorStateException: attempt to unlock lock, not locked by current thread by node id: 41d048e1-e025-4ccf-9f5b-34d221c29526 thread-id: 1
                if (lock.isLocked()) { // 是否还是锁定状态
                    if (lock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
                        lock.unlock(); // 释放锁
                        System.out.println("释放锁");
                    }
                } else {
                    System.out.println("锁已经自动释放");
                }
            }
        } else {
            System.out.println("未获取锁");
        }
    }


    @Test
    public void testAsyncReadWriteLock() {
        RReadWriteLock rwlock = redisson.getReadWriteLock("myLock");

        RLock lock = rwlock.readLock();
        // or
//        RLock lock = rwlock.writeLock();

//        RFuture<Void> lockFuture = lock.lockAsync();

        // or acquire lock and automatically unlock it after 10 seconds
//        RFuture<Void> lockFuture = lock.lockAsync(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        lockFuture.whenComplete((res, exception) -> {
            // ...
            lock.unlockAsync();
        });
    }

    /**
     * Semaphore 信号量  与同步锁有关,支持多线程，也可以是多进程
     *
     * Redis based distributed Semaphore object for Java similar to Semaphore object.
     *
     * Could be initialized before usage, but it's not requirement, with available permits amount through trySetPermits(permits) method.
     */
    @Test
    public void testSemaphore() throws InterruptedException {
        RSemaphore semaphore = redisson.getSemaphore("mySemaphore");

        // acquire single permit
//        semaphore.acquire(); //获取一个许可

        // or acquire 10 permits
//        semaphore.acquire(10);

        // or try to acquire permit
//        boolean res = semaphore.tryAcquire();

        // or try to acquire permit or wait up to 15 seconds
//        boolean res = semaphore.tryAcquire(15, TimeUnit.SECONDS);

        // or try to acquire 10 permit
//        boolean res = semaphore.tryAcquire(10);

        // or try to acquire 10 permits or wait up to 15 seconds
        //尝试获得 10 个许可证或最多等待 15 秒
        boolean res = semaphore.tryAcquire(10, 15, TimeUnit.SECONDS);
        if (res) {
            try {
                //...
                System.out.println("获取许可成功");
            } finally {
                semaphore.release();
                System.out.println("释放许可");
            }
        }
    }


    @Test
    public void testAsyncSemaphore() {
        RSemaphore semaphore = redisson.getSemaphore("mySemaphore");

        // acquire single permit
//        RFuture<Void> acquireFuture = semaphore.acquireAsync();

        // or acquire 10 permits
//        RFuture<Void> acquireFuture = semaphore.acquireAsync(10);

        // or try to acquire permit
//        RFuture<Boolean> acquireFuture = semaphore.tryAcquireAsync();

        // or try to acquire permit or wait up to 15 seconds
//        RFuture<Boolean> acquireFuture = semaphore.tryAcquireAsync(15, TimeUnit.SECONDS);

        // or try to acquire 10 permit
//        RFuture<Boolean> acquireFuture = semaphore.tryAcquireAsync(10);

        // or try to acquire 10 permits or wait up to 15 seconds
        RFuture<Boolean> acquireFuture = semaphore.tryAcquireAsync(10, 15, TimeUnit.SECONDS);

        acquireFuture.whenComplete((res, exception) -> {
            // ...
            semaphore.releaseAsync();
        });
    }

    //PermitExpirableSemaphore
    //Redis based distributed Semaphore object for Java with lease time parameter support for each acquired permit. Each permit identified by own id and could be released only using its id.
    //
    //Should be initialized before usage with available permits amount through trySetPermits(permits) method. Allows to increase/decrease number of available permits through addPermits(permits) method
    @Test
    public void testPermitExpirableSemaphore() throws InterruptedException {
        RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");

        //尝试设置许可证
        semaphore.trySetPermits(23);

        // acquire permit
//        String id = semaphore.acquire();

        // or acquire permit with lease time in 10 seconds
//        String id = semaphore.acquire(10, TimeUnit.SECONDS);

        // or try to acquire permit
//        String id = semaphore.tryAcquire();

        // or try to acquire permit or wait up to 15 seconds
//        String id = semaphore.tryAcquire(15, TimeUnit.SECONDS);

        // or try to acquire permit with least time 15 seconds or wait up to 10 seconds
        String id = semaphore.tryAcquire(10, 15, TimeUnit.SECONDS);
        if (id != null) {
            try {
                //...
            } finally {
                semaphore.release(id);
            }
        }
    }

    @Test
    public void testAsyncPermitExpirableSemaphore() {
        RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");

        RFuture<Boolean> setFuture = semaphore.trySetPermitsAsync(23);

// acquire permit
//        RFuture<String> acquireFuture = semaphore.acquireAsync();

// or acquire permit with lease time in 10 seconds
//        RFuture<String> acquireFuture = semaphore.acquireAsync(10, TimeUnit.SECONDS);

// or try to acquire permit
//        RFuture<String> acquireFuture = semaphore.tryAcquireAsync();

// or try to acquire permit or wait up to 15 seconds
//        RFuture<String> acquireFuture = semaphore.tryAcquireAsync(15, TimeUnit.SECONDS);

// or try to acquire permit with least time 15 seconds or wait up to 10 seconds
        RFuture<String> acquireFuture = semaphore.tryAcquireAsync(10, 15, TimeUnit.SECONDS);
        acquireFuture.whenComplete((id, exception) -> {
            // ...
            semaphore.releaseAsync(id);
        });
    }


    /**
     * CountDownLatch
     *
     * Redis based distributed CountDownLatch object for Java has structure similar to CountDownLatch object.
     *
     * Should be initialized with count by trySetCount(count) method before usage.
     */
    @Test
    public void testCountDownLatch() throws InterruptedException {
        RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");

        latch.trySetCount(2); //尝试设置 2个计数
        // await for count down
        latch.await(); //等待所有count计数被消费
        //继续执行主程序
        System.out.println("主程序执行成功");
    }

    @Test
    public void testCountDownLatch2() throws InterruptedException {
        // in other thread or JVM
        RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");
        latch.countDown(); //消费一个计数
    }

    @Test
    public void testAsyncCountDownLatch() throws InterruptedException {
        RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");

        RFuture<Boolean> setFuture = latch.trySetCountAsync(1);
        // await for count down
        RFuture<Void> awaitFuture = latch.awaitAsync();
        awaitFuture.await();
        System.out.println("主程序执行成功");
    }

    @Test
    public void testAsyncCountDownLatch2() {
        // in other thread or JVM
        RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");
        RFuture<Void> countFuture = latch.countDownAsync();
    }

    /**
     * Spin Lock 自旋锁  未测试-不支持
     *
     * Redis based distributed reentrant SpinLock object for Java and implements Lock interface.
     *
     * Thousands or more locks acquired/released per short time interval may cause reaching of network throughput limit and Redis CPU overload because of pubsub usage in Lock object. This occurs due to nature of Redis pubsub - messages are distributed to all nodes in Redis cluster. Spin Lock uses Exponential Backoff strategy by default for lock acquisition instead of pubsub channel.
     */
    @Test
    public void testSpinLock() throws InterruptedException {
        /*RLock lock = redisson.getSpinLock("myLock");

        // traditional lock method
        lock.lock();

        // or acquire lock and automatically unlock it after 10 seconds
        lock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
                //...
            } finally {
                lock.unlock();
            }
        }*/
    }

}
