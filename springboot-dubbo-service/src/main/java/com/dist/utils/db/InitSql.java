package com.dist.utils.db;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Date: 2019-12-04 13:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：在初始化Bean时,操作数据库执行sql文件
 */
@Component
public class InitSql implements InitializingBean {

    @Value("${jdbc.isStartSql}")
    private boolean isStartSql;

    @Autowired
    private SchemaHandler schemaHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isStartSql){
            this.schemaHandler.execute();
        }
    }
}
