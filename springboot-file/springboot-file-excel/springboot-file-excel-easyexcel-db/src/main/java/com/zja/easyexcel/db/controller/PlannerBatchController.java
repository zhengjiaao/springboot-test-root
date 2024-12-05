/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-19 0:58
 * @Since:
 */
package com.zja.easyexcel.db.controller;

import com.zja.easyexcel.db.planner.PlannerEntity;
import com.zja.easyexcel.db.service.PlannerEntityBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class PlannerBatchController {

    @Autowired
    PlannerEntityBatch plannerEntityBatch;

    /**
     * http://127.0.0.1:8080/saveAll/1000
     */
    @GetMapping(value = "/saveAll/{size}")
    public Object saveAll(@PathVariable Integer size) {
        List<PlannerEntity> entityList = generateData(size);
        plannerEntityBatch.saveAll(entityList);
        return true;
    }

    /**
     * http://127.0.0.1:8080/batchInsert/1000
     */
    @GetMapping(value = "/batchInsert/{size}")
    public Object batchInsert(Integer size) {
        List<PlannerEntity> entityList = generateData(size);
        plannerEntityBatch.jpaSaveAllByBatch(entityList);
        return true;
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
