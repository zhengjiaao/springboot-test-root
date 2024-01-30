/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-09 16:20
 * @Since:
 */
package com.zja.obfuscated.proguard.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * id生成工具类
 *
 * @author: zhengja
 * @since: 2023/08/09 16:20
 * @dasc: 请先尝试在以下工具包中查找封装的方法，若找不到，在此 工具类 添加自定文件方法
 * @see java.util.UUID
 * @see IdUtil
 * @see cn.hutool.core.lang.UUID
 */
@Slf4j
public class IdGeneratorUtil {

    //雪花算法
    public static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(getMachinePiece() % 32, getProcessPiece() % 32);


    public static void main(String[] args) {
        //生成500万个id，所耗时记录
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
//            System.out.println(""); //1520
//            log.info(""); //2305
            String id = randomNanoId(4);
//            Long id = nextIdLong();
//            System.out.println(id);
        }//3483

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

    }

    /**
     * 雪花算法 Snowflake ID
     * <p>
     * 示例：1689205124666884282
     * 特点：19个字符，纯数值，有序性，递增趋势(不是完全连贯的)，高性能、可分解性、字符串
     * 压测：生成500W个id，耗时大概1224毫秒
     * 适用：全局唯一性id、数据库主键id、排序字段等
     * 缺点：可预测性，依赖系统时钟，有限的容量，配置复杂性
     * <p>
     *
     * @return Snowflake ID
     */
    public static Long nextIdLong() {
        return SNOWFLAKE.nextId();
    }

    /**
     * 排序
     */
    public static Long nextSortLong() {
        return nextIdLong();
    }

    /**
     * 雪花算法 Snowflake ID
     * <p>
     * 示例：1689203091228590240
     * 特点：19个字符，纯数值，有序性，递增趋势(不是完全连贯的)，高性能、可分解性、字符串
     * 压测：生成500W个id，耗时大概1234毫秒
     * 适用：全局唯一性id、数据库主键id、文档id等
     * 缺点：可预测性，依赖系统时钟，有限的容量，配置复杂性
     * <p>
     *
     * @return Snowflake ID
     */
    public static String nextIdStr() {
        return SNOWFLAKE.nextIdStr();
    }

    /**
     * 创建 MongoDB ID生成策略实现的 objectId
     * <p>
     * 示例：648a9ea4a65574cf26fdbcb3
     * 特点：24个字符，混合(数值+字母)，无顺序，非严格递增（同一秒内由同一进程生成的多个ObjectId之间，自增计数器会逐渐递增），字符串
     * 压测：生成500W个id，耗时大概2880毫秒
     * 适用：数据库主键id、文档id等
     * 缺点：可预测性、不适用于全局唯一性要求、依赖系统时间、不可分解性
     * <pre>
     * 组成：
     * 1. Time 时间戳。
     * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
     * 3. PID 进程ID。确保同一机器中不冲突
     * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
     * <pre>
     * <p>
     * @return MongoDB ID
     */
    public static String objectId() {
        return IdUtil.objectId();
    }

    /**
     * UUID 去掉了横线 "-"
     * <p>
     * 示例：bc48749b91084e3ab138fd272585fa4c
     * 特点：32个字符，混合(数值+字母)，无顺序，非递增，字符串
     * 压测：生成500W个id，耗时大概1709毫秒
     * 适用：数据库主键id、文档id、分布式id等
     * 缺点：可预测性，长度较长(32个字符)，不易读，无序性，索引性能(数据库)
     * <p>
     *
     * @return UUID
     */
    public static String fastSimpleUUID() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * UUID 带横线 "-"
     * <p>
     * 示例：bc48749b91084e3ab138fd272585fa4c
     * 特点：36个字符，混合(数值+字母)，无顺序，非递增，字符串
     * 压测：生成500W个id，耗时大概1798毫秒
     * 适用：数据库主键id、文档id、分布式id等
     * 缺点：可预测性，长度较长(36个字符)，不易读，无序性，索引性能(数据库)
     * <p>
     *
     * @return UUID
     */
    public static String fastUUID() {
        return IdUtil.fastUUID();
    }


    /**
     * 随机id
     * <p>
     * 示例：-2Fw8UxZZPE-VQ_LDHr-9
     * 特点：21个字符，混合(数值+字母+特殊符号)，唯一性，无序性，较高的安全性(隐私保护)，字符串
     * 压测：生成500W个id，耗时大概3548毫秒
     * 适用：需要唯一性的标识符,安全性要求高的场景,高并发环境
     * 缺点：不可预测性，不可读性，索引性能(数据库)
     * <p>
     *
     * @return randomNanoId
     */
    public static String randomNanoId() {
        return IdUtil.nanoId();
    }

    /**
     * 随机id
     * <p>
     * 示例：-2Fw 或 Qf_YDE
     * 特点：36个字符，混合(数值+字母+特殊符号)，唯一性，无序性，较高的安全性(隐私保护)，字符串
     * 压测：生成500W个id，耗时大概3548毫秒
     * 适用：需要唯一性的标识符,安全性要求高的场景,高并发环境
     * 缺点：不可预测性，不可读性，索引性能(数据库)
     * <p>
     *
     * @return randomNanoId
     */
    public static String randomNanoId(int size) {
        return IdUtil.nanoId(size);
    }

    //以下是私有方法


    // 获取机器标识，每台主机不一样，固定的，示例：-1677656064
    private static int getMachinePiece() {
        int machinePiece;
        try {
            StringBuilder netSb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                byte[] hardwareAddress = ni.getHardwareAddress();
                if (hardwareAddress != null) {
                    for (byte b : hardwareAddress) {
                        netSb.append(String.format("%02X", b));
                    }
                }
            }
            machinePiece = netSb.toString().hashCode() << 16;
        } catch (Throwable e) {
            machinePiece = (RandomUtil.randomInt()) << 16;
        }
        return machinePiece;
    }

    // 获取进程标识，每个进程不一样，变动的。示例：62891
    private static int getProcessPiece() {
        int processPiece;
        try {
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            int atIndex = processName.indexOf('@');
            int processId = (atIndex > 0) ? Integer.parseInt(processName.substring(0, atIndex)) : processName.hashCode();
            int loaderId = System.identityHashCode(ClassLoader.getSystemClassLoader());
            String processSb = Integer.toHexString(processId) + Integer.toHexString(loaderId);
            processPiece = processSb.hashCode() & 0xFFFF;
        } catch (Throwable t) {
            processPiece = Runtime.getRuntime().availableProcessors();
        }
        return processPiece;
    }
}
