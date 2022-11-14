package com.zja.util;


import com.zja.rest.RestProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 16:49
 */
@Component
public class AppContextUtil implements ApplicationContextAware {

    @Autowired
    private Environment environment;

    private static ApplicationContext applicationContext;
    private static Logger LOG = LoggerFactory.getLogger(AppContextUtil.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(AppContextUtil.applicationContext == null) {
            AppContextUtil.applicationContext = applicationContext;
            registryRestTemplate(applicationContext);
        }
        System.out.println("---------------获取applicationContext---------------");
    }

    /**
     * 注册Rest代理实现
     * @param applicationContext
     */
    private void registryRestTemplate(ApplicationContext applicationContext){
        try{
            RestProxyHelper.registryRestProxyBean(environment,applicationContext);
        }catch (Exception e){
            e.getMessage();
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T>T getBean(Class<T> cls) throws BeansException {
        return applicationContext.getBean(cls);
    }

    public static Object getBean(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }
}
