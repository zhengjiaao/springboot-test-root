/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-19 0:38
 * @Since:
 */
package com.zja.easyexcel.db.controller;

import com.zja.easyexcel.db.planner.PlannerEntity;
import com.zja.easyexcel.db.service.PlannerEntityReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlannerController {

    @Autowired
    PlannerEntityReadService plannerEntityReadService;

    /**
     * http://127.0.0.1:8080/find?pageNum=1&pageSize=3
     */
    @GetMapping(value = "/find")
    public Page<PlannerEntity> findAll(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return plannerEntityReadService.findAll(pageNum, pageSize);
    }

    /**
     * http://127.0.0.1:8080/import/1000
     * http://127.0.0.1:8080/import/10000
     * http://127.0.0.1:8080/import/30000
     */
    @GetMapping(value = "/import/{size}")
    public void importAndSave(@PathVariable String size) {
        //"D:\\Temp\\excel\\规划师信息-模拟数据-1000.xlsx"
        String excelPath = "D:\\Temp\\excel\\规划师信息-模拟数据-" + size + ".xlsx";

        plannerEntityReadService.importAndSave(excelPath);
    }

}
