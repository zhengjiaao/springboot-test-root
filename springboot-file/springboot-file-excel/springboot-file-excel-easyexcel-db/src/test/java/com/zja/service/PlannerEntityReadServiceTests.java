/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-13 17:13
 * @Since:
 */
package com.zja.service;

import com.zja.BaseTests;
import com.zja.easyexcel.db.planner.PlannerEntity;
import com.zja.easyexcel.db.service.PlannerEntityReadService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class PlannerEntityReadServiceTests extends BaseTests {

    @Autowired
    PlannerEntityReadService plannerEntityReadService;

    /**
     * excel导入数据并完成数据存储
     */
    @Test
    public void importAndSave() {
//        plannerEntityReadService.importAndSave("D:\\Temp\\excel\\规划师信息-模拟数据-1000.xlsx");
//        plannerEntityReadService.importAndSave("D:\\Temp\\excel\\规划师信息-模拟数据-10000.xlsx");
        plannerEntityReadService.importAndSave("D:\\Temp\\excel\\规划师信息-模拟数据-30000.xlsx");
    }

    @Test
    public void findAll() {
        Page<PlannerEntity> entityPage = plannerEntityReadService.findAll(1, 3);
        printlnAnyObj(entityPage);
    }

}
