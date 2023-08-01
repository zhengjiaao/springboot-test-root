/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-17 20:22
 * @Since:
 */
package com.zja.service;

import com.zja.BaseTests;
import com.zja.FileExcleApplication;
import com.zja.planner.PlannerEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileExcleApplication.class)
public class PlannerEntityBatchTests extends BaseTests {

    @Autowired
    PlannerEntityBatch plannerEntityBatch;

    @Test
    public void saveAll() {
        List<PlannerEntity> entityList = generateData(10000);
        plannerEntityBatch.saveAll(entityList);
    }

    @Test
    public void jpaSaveAllByBatch() {
        List<PlannerEntity> entityList = generateData(1000);
        plannerEntityBatch.jpaSaveAllByBatch(entityList);
    }

    @Test
    public void jdbcSaveAllByBatch() {
        List<PlannerEntity> entityList = generateData(100000);
        plannerEntityBatch.jdbcSaveAllByBatch(entityList);
    }


    /**
     * 生成数据
     * @param size 生成多少条数据
     */
    private static List<PlannerEntity> generateData(int size) {
        log.info("generateData() 方法，生成数据: {} 条", size);
        List<PlannerEntity> entityList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PlannerEntity entity = new PlannerEntity();
            entity.setCompanyName("公司" + i);
            entity.setName("名称" + i);
            entity.setLicenseNumber("证件号");
            entity.setValidityPeriod("有效期");
            entity.setDateOfIssue("发证日期");
            entity.setCertificateNumber("证书编号");

            entityList.add(entity);
        }

        return entityList;
    }
}
