/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-18 20:13
 * @Since:
 */
package com.zja.service;

import com.alibaba.excel.EasyExcel;
import com.zja.BaseTests;
import com.zja.easyexcel.db.planner.PlannerEntity;
import com.zja.easyexcel.db.service.PlannerEntityWriteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PlannerEntityWriteServiceTests extends BaseTests {

    @Autowired
    PlannerEntityWriteService plannerEntityWriteService;

    /**
     * 查询数据并导出
     */
    @Test
    public void findDataAndExport() {
        plannerEntityWriteService.findDataAndExport("D:\\Temp\\excel\\规划师信息-write.xlsx", 1, 10);
    }

    @Test
    public void write() {
        List<PlannerEntity> entityList = generateData(1000);

        EasyExcel.write("D:\\Temp\\excel\\规划师信息-模拟数据-1000.xlsx", PlannerEntity.class).sheet().doWrite(entityList);
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
            entity.setId(Long.valueOf(i));
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
