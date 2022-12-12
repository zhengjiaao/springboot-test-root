/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-12 13:49
 * @Since:
 */
package com.zja.repositorys.node;

import com.zja.entity.node.PhoneNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 *
 */
public interface PhoneNodeRepository extends Neo4jRepository<PhoneNode, Long> {

    PhoneNode findByName(String name);
}
