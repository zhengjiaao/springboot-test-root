package com.zja.repositorys;

import com.zja.entity.HaveDynamic;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:16
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public interface HaveDynamicRepository extends Neo4jRepository<HaveDynamic, Long> {
}
