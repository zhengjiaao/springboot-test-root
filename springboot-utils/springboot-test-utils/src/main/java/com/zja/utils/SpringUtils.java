package com.zja.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;

public final class SpringUtils {

	private static SpringUtils context = null;// 单例模式

	private WebApplicationContext webApplicationContext; // Spring应用上下文环境

	public static SpringUtils getInstance() {
		if (context == null) {
			context = new SpringUtils();
		}
		return context;
	}

    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     *
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) throws BeansException {
        return (T) webApplicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws BeansException
     *
     */
    public <T> T getBean(Class<T> clz) throws BeansException {
        T result = (T) webApplicationContext.getBean(clz);
        return result;
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public boolean containsBean(String name) {
        return webApplicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     *
     */
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return webApplicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     *
     */
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return webApplicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     *
     */
    public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return webApplicationContext.getAliases(name);
    }

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	public synchronized void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

}