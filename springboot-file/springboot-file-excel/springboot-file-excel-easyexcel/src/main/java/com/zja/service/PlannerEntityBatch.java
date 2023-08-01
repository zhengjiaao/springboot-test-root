/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-25 21:24
 * @Since:
 */
package com.zja.service;

import cn.hutool.core.collection.CollectionUtil;
import com.zja.planner.PlannerDao;
import com.zja.planner.PlannerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 批处理服务
 */
@Slf4j
@Service
public class PlannerEntityBatch {

    @Autowired
    private PlannerDao plannerDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * jpa jpaRepository.saveAll()
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<PlannerEntity> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        long startTime = System.nanoTime();
        plannerDao.saveAll(entityList);
        log.info("saveAll() 方法，单次耗时: {} ms", (System.nanoTime() - startTime) / 1000000);
    }

    /**
     * jpa entityManager.persist()
     * 优点：避免jpaRepository.saveAll() 在执行插入数据时进行业务校验(会耗时)
     */
    @Transactional(rollbackFor = Exception.class)
    public void jpaSaveAllByBatch(List<PlannerEntity> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        long startTime = System.nanoTime();

        for (PlannerEntity entity : entityList) {
            entityManager.persist(entity);
        }

        entityManager.flush();
        entityManager.clear();

        long estimatedTime = System.nanoTime() - startTime;
        log.info("batchInsert() 方法，单次耗时: {} ms", estimatedTime / 1000000);
    }

    /**
     * jdbc 批量处理
     * 注意：需要在jdbc URL中添加一个参数才支持批量 rewriteBatchedStatements=true
     */
    @Transactional(rollbackFor = Exception.class)
    public void jdbcSaveAllByBatch(List<PlannerEntity> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }

        String sql = "INSERT INTO hys_planner_test (id,companyName,name,licenseNumber,validityPeriod,dateOfIssue,certificateNumber) VALUES (?,?,?,?,?,?,?) ";

        List<Object[]> objectList = new ArrayList<>();
        for (PlannerEntity entity : entityList) {
            //顺序与 sql 保持一致
            objectList.add(new Object[]{
                    entity.getId(),
                    entity.getCompanyName(),
                    entity.getName(),
                    entity.getLicenseNumber(),
                    entity.getValidityPeriod(),
                    entity.getDateOfIssue(),
                    entity.getCertificateNumber()
            });
        }

        jdbcTemplate.batchUpdate(sql, objectList);
    }

    /**
     * 批删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllInBatch() {
        plannerDao.deleteAllInBatch();
    }

}
