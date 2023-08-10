/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-10 10:14
 * @Since:
 */
package com.zja.util;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Seata基于改良版雪花算法的分布式UUID生成器(高性能、全局唯一、趋势递增)
 *
 * <p>
 * 原版雪花算法缺点：
 * 1.时钟敏感(时钟回拨/时钟漂移)、
 * 2.突发性能有上限(QPS约400w/s,但准确是4096/ms)
 * 新版雪花算法优化：
 * 1.生成器弱依赖于操作系统时钟。
 * 2.节点ID和时间戳互换位置。
 * 3.生成器不再有4096/ms的突发性能限制了。
 * 新版缺点：
 * 单节点内部是单调递增，多节点部署时，不再是全局单调递增了。节点ID排在高位，那么节点ID大的，生成的ID一定大于节点ID小的，不管时间上谁先谁后。
 *
 * </p>
 *
 * @author: zhengja
 * @since: 2023/08/10 10:14
 * <p>
 * 改了哪些内容：与原版雪花算法对比，改成[时间戳和节点ID]换个位置，并支持自定义开始时间，同时只获取一次系统时间戳。
 * 注意：改良版雪花算法，无法做到全局递增，但是能够做到单节点递增
 * 来自：
 * <a href="https://github.com/seata/seata/blob/2.x/common/src/main/java/io/seata/common/util/IdWorker.java">...</a>
 * 讲解：
 * <a href="http://seata.io/zh-cn/blog/seata-analysis-UUID-generator.html">...</a>
 * <a href="http://seata.io/zh-cn/blog/seata-snowflake-explain.html">...</a>
 * <a href="https://juejin.cn/post/7264387737276203065#heading-0">...</a>
 */
public class IdWorker {

    /**
     * Start time cut (2020-05-03)
     */
    private final long twepoch = 1588435200000L;

    /**
     * The number of bits occupied by workerId
     */
    private final int workerIdBits = 10;

    /**
     * The number of bits occupied by timestamp
     */
    private final int timestampBits = 41;

    /**
     * The number of bits occupied by sequence
     */
    private final int sequenceBits = 12;

    /**
     * Maximum supported machine id, the result is 1023
     */
    private final int maxWorkerId = ~(-1 << workerIdBits);

    /**
     * business meaning: machine ID (0 ~ 1023)
     * actual layout in memory:
     * highest 1 bit: 0
     * middle 10 bit: workerId
     * lowest 53 bit: all 0
     */
    private long workerId;

    /**
     * timestamp and sequence mix in one Long
     * highest 11 bit: not used
     * middle  41 bit: timestamp
     * lowest  12 bit: sequence
     */
    private AtomicLong timestampAndSequence;

    /**
     * mask that help to extract timestamp and sequence from a long
     */
    private final long timestampAndSequenceMask = ~(-1L << (timestampBits + sequenceBits));

    /**
     * instantiate an IdWorker using given workerId
     *
     * @param workerId if null, then will auto assign one
     */
    public IdWorker(Long workerId) {
        initTimestampAndSequence();
        initWorkerId(workerId);
    }

    /**
     * init first timestamp and sequence immediately
     */
    private void initTimestampAndSequence() {
        long timestamp = getNewestTimestamp();
        long timestampWithSequence = timestamp << sequenceBits;
        this.timestampAndSequence = new AtomicLong(timestampWithSequence);
    }

    /**
     * init workerId
     *
     * @param workerId if null, then auto generate one
     */
    private void initWorkerId(Long workerId) {
        if (workerId == null) {
            workerId = generateWorkerId();
        }
        if (workerId > maxWorkerId || workerId < 0) {
            String message = String.format("worker Id can't be greater than %d or less than 0", maxWorkerId);
            throw new IllegalArgumentException(message);
        }
        this.workerId = workerId << (timestampBits + sequenceBits);
    }

    /**
     * get next UUID(base on snowflake algorithm), which look like:
     * highest 1 bit: always 0
     * next   10 bit: workerId
     * next   41 bit: timestamp
     * lowest 12 bit: sequence
     *
     * @return UUID
     */
    public long nextId() {
        waitIfNecessary();
        long next = timestampAndSequence.incrementAndGet();
        long timestampWithSequence = next & timestampAndSequenceMask;
        return workerId | timestampWithSequence;
    }

    /**
     * block current thread if the QPS of acquiring UUID is too high
     * that current sequence space is exhausted
     */
    private void waitIfNecessary() {
        long currentWithSequence = timestampAndSequence.get();
        long current = currentWithSequence >>> sequenceBits;
        long newest = getNewestTimestamp();
        if (current >= newest) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignore) {
                // don't care
            }
        }
    }

    /**
     * get newest timestamp relative to twepoch
     */
    private long getNewestTimestamp() {
        return System.currentTimeMillis() - twepoch;
    }

    /**
     * auto generate workerId, try using mac first, if failed, then randomly generate one
     *
     * @return workerId
     */
    private long generateWorkerId() {
        try {
            return generateWorkerIdBaseOnMac();
        } catch (Exception e) {
            return generateRandomWorkerId();
        }
    }

    /**
     * use lowest 10 bit of available MAC as workerId
     *
     * @return workerId
     * @throws Exception when there is no available mac found
     */
    private long generateWorkerIdBaseOnMac() throws Exception {
        Enumeration<NetworkInterface> all = NetworkInterface.getNetworkInterfaces();
        while (all.hasMoreElements()) {
            NetworkInterface networkInterface = all.nextElement();
            boolean isLoopback = networkInterface.isLoopback();
            boolean isVirtual = networkInterface.isVirtual();
            if (isLoopback || isVirtual) {
                continue;
            }
            byte[] mac = networkInterface.getHardwareAddress();
            return ((mac[4] & 0B11) << 8) | (mac[5] & 0xFF);
        }
        throw new RuntimeException("no available mac found");
    }

    /**
     * randomly generate one as workerId
     *
     * @return workerId
     */
    private long generateRandomWorkerId() {
        return new Random().nextInt(maxWorkerId + 1);
    }
}