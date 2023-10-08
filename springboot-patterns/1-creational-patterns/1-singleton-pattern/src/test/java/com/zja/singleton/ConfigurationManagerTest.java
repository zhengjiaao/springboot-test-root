/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 17:29
 * @Since:
 */
package com.zja.singleton;

import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2023/10/07 17:29
 */
public class ConfigurationManagerTest {


    @Test
    public void test() {
        //设置属性
        ConfigurationManager configManagerSet = ConfigurationManager.getInstance();
        configManagerSet.setProperty("database.url","jdbc:127.0.0.1");
        configManagerSet.setProperty("database.username","admin");
        configManagerSet.setProperty("app.name","testApp");

        //全局可以访问属性(其他类也可以)
        ConfigurationManager configManager = ConfigurationManager.getInstance();
        String dbUrl = configManager.getProperty("database.url");
        String username = configManager.getProperty("database.username");

        // 在其他类中也可以通过全局访问点获取ConfigurationManager实例
        ConfigurationManager anotherConfigManager = ConfigurationManager.getInstance();
        String appName = anotherConfigManager.getProperty("app.name");
        int maxConnections = Integer.parseInt(anotherConfigManager.getProperty("database.maxConnections"));

        // 使用获取的配置信息进行相应操作
        // ...
    }
}
