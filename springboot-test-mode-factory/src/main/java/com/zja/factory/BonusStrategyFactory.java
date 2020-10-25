package com.zja.factory;

import com.zja.service.BonusService;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 22:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
// 创建一个奖金工厂类
public class BonusStrategyFactory {

    // 用map来保存如何使用奖金的策略类
    private static Map<Integer, BonusService> strategyMap = new ConcurrentHashMap<Integer, BonusService>();

    //通过奖金多少查找对应的使用策略
    public static BonusService getByBonus(Integer bonus) {
        return strategyMap.get(bonus);
    }

    // 将奖金和对应的使用策略注册到map里
    public static void register(Integer bonus, BonusService bonusService) {
        Assert.notNull(bonus, "bonus can't be null");
        strategyMap.put(bonus, bonusService);
    }
}
