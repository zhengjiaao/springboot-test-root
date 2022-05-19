package com.zja.util.id;

import java.util.UUID;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2019-11-22 13:39
 * @Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Id 生成工具
 */
public abstract class IdUtil {

    private static final IdWorker idWorker = new IdWorker();
    private static final SnowFlake snowFlake = new SnowFlake(2, 3);

    /**
     * 单机版Id生成器 线程安全的
     * 描述：使用jpaId生成算法 是递增 19位数字
     * 例如：1177330942235631616，1177330942235631617
     */
    public static long dataId() {
        return idWorker.getId();
    }

    /**
     * 分布式：id生成器  100万个id大概5~12秒 线程安全的
     * 描述：使用雪花算法 18位(数字)，不是递增，是递增趋势
     * 例如：607561623821103104，607561705450647552
     */
    public static Long snowFlakeId() {
        return snowFlake.nextId();
    }

    /**
     * 分布式：id生成器  100万个id大概5~12秒 线程安全的
     */
    public static String snowFlakeIdString() {
        return Long.toString(snowFlake.nextId());
    }

    /**
     * MongoDB ID生成策略实现 线程安全  如：数据库id
     * 24位(数字+字符)
     * 例如：60dbdfe55536d22a7a977897，60dbdfe55536d22a7a977898
     */
    public static String mgdbId() {
        return ObjectIdGenerator.next();
    }

    /**
     * 当前系统时间戳 单机版 不安全
     * 13位(数字)
     * 例如：1625020777738
     */
    public static long currentTimeMillisId() {
        return System.currentTimeMillis();
    }

    /**
     * 随机产生6位数字  如：验证码
     * 例如：146520
     */
    public static String uuid6() {
        int randNum = 1 + (int) (Math.random() * ((999999 - 1) + 1));
        return String.valueOf(randNum);
    }

    /**
     * 随机产生19位数字  系统当前日期时间 + 随机6位数
     * 例如：1652880112343711546
     */
    public static String uuid19() {
        return System.currentTimeMillis() + uuid6();
    }

    /**
     * 随机产生32位uuid  随机数
     * 例如：210b6dbd427455abf52c928a658cfe4
     */
    public static String uuid32() {
        return uuid36().replace("-", "");
    }

    /**
     * 随机产生36位uuid  随机数
     * 例如：c6882952-42fc-43e5-a757-e279a6f36743
     */
    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {

        System.out.println("dataId: " + dataId()); //1177330942235631616 1177330942235631618
        System.out.println("dataId2: " + dataId()); //1177330942235631617 1177330942235631619

        System.out.println("snowFlakeId: " + IdUtil.snowFlakeId()); //607561623821103104 607561705450647552
        System.out.println("snowFlakeId2: " + IdUtil.snowFlakeId()); //607561623821103105 607561705450647553

        System.out.println("mgdbId: " + mgdbId());

        System.out.println("uuid6: " + uuid6());
        System.out.println("uuid19: " + uuid19());
        System.out.println("uuid32: " + uuid32());
        System.out.println("uuid36: " + uuid36());

    }
}
