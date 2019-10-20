package com.dist.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * rest代理帮助类
 * @author dingchw
 * @date 2019/4/1.
 */
public class RestProxyHelper {
    private final static Logger LOG = LoggerFactory.getLogger(RestProxyHelper.class);

    private final static String PACKAGE_SCAN = "dist.rest-api.package-scan";



    /**
     * 使用cglib编写动态代理实现，然后将代理注册成spring bean
     * @param env
     * @param applicationContext
     */
    public static void registryRestProxyBean(Environment env, ApplicationContext applicationContext)throws Exception{
        //先找出配置
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();

        String restInterfacePackage = env.getProperty(PACKAGE_SCAN);
        if(null == restInterfacePackage){
            throw new Exception("Rest扫描包路径未配置，请配置待扫描的包路径:[" + PACKAGE_SCAN + "]");
        }
        String restInterfacePythsicPath = restInterfacePackage.replace(".","/");
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(restInterfacePythsicPath);
        List<Class> restInterfaceList = new ArrayList<>();
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            File rootDir = new File(filePath);
            File[] files = rootDir.listFiles();
            if (null != files){
                for (File file : files) {
                    String clazzFullName = file.getName();
                    String clzzNameWithoutPrefix = clazzFullName.substring(0,clazzFullName.indexOf(".class"));

                    Class clzz = Class.forName(restInterfacePackage + "." + clzzNameWithoutPrefix);
                    if(clzz.isInterface()){
                        restInterfaceList.add(clzz);
                    }
                }
            }

        }
        //注册到bean里面去
        restInterfaceList.forEach((clzzItem)->{
            LOG.info("正在生成接口【{}】的代理",clzzItem.getSimpleName());
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clzzItem);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
            definition.setBeanClass(RestProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(clzzItem.getName(), definition);
        });
    }
}
