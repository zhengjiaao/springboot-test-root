package com.zja.rest;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author dingchw
 * @date 2019/4/2.
 */
@Component
public class RestProxyFactory<T> implements FactoryBean<T> {
    @Autowired
    private Environment env;

    private Class<T> interfaceClass;

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public T getObject() throws Exception {
        return (T)new RestProxy().bind(interfaceClass,env);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
