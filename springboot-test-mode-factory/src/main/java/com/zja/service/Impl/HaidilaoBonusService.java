package com.zja.service.Impl;

import com.zja.factory.BonusStrategyFactory;
import com.zja.service.BonusService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 22:46
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Service
public class HaidilaoBonusService implements BonusService, InitializingBean {
    @Override
    public String useBonusPlan(Integer bonus) {
        //饿了
        return "去海底捞吃" + bonus + "元的火锅";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册到Map中
        //可用枚举 100

        // SUPER_DADA
        BonusStrategyFactory.register(100, this);
    }
}
