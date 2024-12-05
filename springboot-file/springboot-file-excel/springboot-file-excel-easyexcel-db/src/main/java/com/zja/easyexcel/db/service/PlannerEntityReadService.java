/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-22 15:58
 * @Since:
 */
package com.zja.easyexcel.db.service;

import com.alibaba.excel.EasyExcel;
import com.zja.easyexcel.db.planner.PlannerDao;
import com.zja.easyexcel.db.planner.PlannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PlannerEntityReadService {

    @Autowired
    private PlannerDao plannerDao;

    @Autowired
    private PlannerEntityBatch plannerEntityBatch;

    /**
     * excel导入数据并完成数据存储
     */
//    @Async("taskAsyncExecutor")
    public synchronized void importAndSave(String excelPath) {
        //每次导入，都是全量的数据
        //plannerDao.deleteAllInBatch();

        //sheet()默认读取第一个sheet ,  doRead()异步无模型读(默认表头占一行,从第2行开始读)
        EasyExcel.read(excelPath, PlannerEntity.class, new PlannerEntityReadListener(plannerEntityBatch)).sheet().doRead();
    }

    /**
     * 分页查询
     */
    public Page<PlannerEntity> findAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return plannerDao.findAll(pageable);
    }

}
