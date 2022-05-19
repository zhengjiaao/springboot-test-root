/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-18 1:43
 * @Since:
 */
package com.zja.service;

import com.alibaba.excel.EasyExcel;
import com.zja.planner.PlannerDao;
import com.zja.planner.PlannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PlannerEntityWriteService {

    @Autowired
    private PlannerDao plannerDao;

    /**
     * 查询数据并导出到 excel文件中
     */
    public void findDataAndExport(String excelPath, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PlannerEntity> entityPage = plannerDao.findAll(pageable);

        EasyExcel.write(excelPath, PlannerEntity.class).sheet().doWrite(entityPage.getContent());
    }

}
