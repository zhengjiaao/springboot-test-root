/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-07 11:17
 * @Since:
 */
package com.zja.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 规划师信息
 */
@Repository
public interface PlannerDao extends JpaRepository<PlannerEntity, Long>, JpaSpecificationExecutor<PlannerEntity> {

}
