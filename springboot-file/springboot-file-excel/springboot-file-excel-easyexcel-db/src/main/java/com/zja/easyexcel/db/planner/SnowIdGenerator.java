/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-18 21:27
 * @Since:
 */
package com.zja.easyexcel.db.planner;

import com.zja.easyexcel.db.util.id.IdUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 自定义主键生成策略: 雪花算法 id
 */
public class SnowIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return IdUtil.snowFlakeId();
    }

    /**
     * 支持 Jdbc 批量插入
     */
    @Override
    public boolean supportsJdbcBatchInserts() {
        // 默认 false , 使用批量插入可以提升性能。Spring Data Jpa 在一些情况下自动禁用批量功能，需要手动启用。
        return true;
    }
}
