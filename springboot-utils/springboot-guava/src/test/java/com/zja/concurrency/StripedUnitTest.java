/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 11:19
 * @Since:
 */
package com.zja.concurrency;

import com.google.common.util.concurrent.Striped;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: zhengja
 * @since: 2023/11/07 11:19
 */
public class StripedUnitTest {

    @Test
    public void testStripedLock() throws InterruptedException {
        // 创建一个分段锁，有10个段
        Striped<Lock> striped = Striped.lock(10);

        // 获取分段锁
        Lock lock1 = striped.get("key1");
        Lock lock2 = striped.get("key2");

        // 验证获取的锁不为空
        assertNotNull(lock1);
        assertNotNull(lock2);

        // 验证不同键的锁是不同的
        assertNotSame(lock1, lock2);

        // 使用锁进行同步
        lock1.lock();
        assertTrue(lock1.tryLock());
        lock1.unlock();

        // 使用不同的锁进行同步
        lock2.lock();
        assertTrue(lock2.tryLock());
        lock2.unlock();
    }

}
