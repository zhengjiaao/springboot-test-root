/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-14 11:12
 * @Since:
 */
package com.zja.jta.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

/**
 * 统一事务管理（分布式事务，可选的）
 */
@Configuration
public class JtaTransactionManagerConfig {

    /**
     * 配置spring的JtaTransactionManager，底层委派给atomikos进行处理
     */
    @Primary
    @Bean(name = "jtaTransactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

}
