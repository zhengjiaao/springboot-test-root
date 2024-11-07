package com.zja.report.factory;

import com.zja.report.factory.strategy.ReportStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:30
 */
@Component
public class ReportFactory implements ApplicationContextAware, InitializingBean {

    private final Map<Integer, ReportStrategy> strategyMap = new ConcurrentHashMap<>();

    private ApplicationContext appContext;

    public ReportStrategy getReportStrategy(Integer templateCode) {
        ReportStrategy strategy = this.strategyMap.get(templateCode);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的 templateCode: " + templateCode);
        }
        return strategy;
    }

    public Set<Integer> templateCodeList() {
        return strategyMap.keySet();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ReportStrategy> beansOfType = this.appContext.getBeansOfType(ReportStrategy.class);
        beansOfType.values().forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
