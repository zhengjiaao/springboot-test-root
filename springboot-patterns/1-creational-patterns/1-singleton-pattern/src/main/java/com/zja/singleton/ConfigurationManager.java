/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 17:28
 * @Since:
 */
package com.zja.singleton;

import java.util.Properties;

/**
 * @author: zhengja
 * @since: 2023/10/07 17:28
 */
public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Properties configProperties;

    // 私有化构造函数，防止外部实例化
    private ConfigurationManager() {
        // 加载配置文件
        configProperties = new Properties();
        // 读取配置文件并初始化configProperties
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return configProperties.getProperty(key);
    }

    public Object setProperty(String key,String value) {
        return configProperties.setProperty(key,value);
    }


    // 其他配置相关的方法...
}
