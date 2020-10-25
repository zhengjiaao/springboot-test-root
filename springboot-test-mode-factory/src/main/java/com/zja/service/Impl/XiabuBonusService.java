package com.zja.service.Impl;

import com.zja.factory.BonusStrategyFactory;
import com.zja.service.BonusService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 22:45
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Service
public class XiabuBonusService implements BonusService, InitializingBean {

    @Override
    public String useBonusPlan(Integer bonus) {
        //饿了
        if (true) {
            return "去呷哺吃" + bonus + "元的火锅";
        }
        return "在考虑考虑";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册到Map中
        BonusStrategyFactory.register(300, this);
    }
}
