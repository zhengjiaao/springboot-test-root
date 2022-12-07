/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-14 11:04
 * @Since:
 */
package com.zja.jta.primary.repository;

import com.zja.jta.primary.entity.PrimaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 用法参考：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface
 *
 * Repository
 * CrudRepository 常用的
 * PagingAndSortingRepository 分页、排序
 * JpaRepository 常用的
 * QuerydslPredicateExecutor 扩展，可选的
 */
//@Repository
public interface PrimaryRepository extends CrudRepository<PrimaryEntity, Long>, PagingAndSortingRepository<PrimaryEntity, Long> {

}
