/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-14 16:51
 * @Since:
 */
package com.zja.repositorys;

import com.zja.entity.Phone;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 *
 */
public interface PhoneRepository extends Neo4jRepository<Phone, Long> {

}
