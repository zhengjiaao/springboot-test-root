/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-12 10:43
 * @Since:
 */
package com.zja.repositorys.node;

import com.zja.entity.node.CarNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 *
 */
public interface CarNodeRepository extends Neo4jRepository<CarNode, Long> {

    CarNode findByCarBrand(String carBrand);
}
