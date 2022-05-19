/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-07 14:48
 * @Since:
 */
package com.zja.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.zja.planner.PlannerEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 分析事件监听器-入库操作
 */
@Slf4j
public class PlannerEntityReadListener extends AnalysisEventListener<PlannerEntity> {

    private PlannerEntityBatch plannerEntityBatch;

    public PlannerEntityReadListener(PlannerEntityBatch plannerEntityBatch) {
        this.plannerEntityBatch = plannerEntityBatch;
    }

    /**
     * 每隔50条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    private List<PlannerEntity> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 获取解析 Excel 后的数据
     * @param data      数据
     * @param context  上下文内容
     */
    @Override
    public void invoke(PlannerEntity data, AnalysisContext context) {
        //log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成后执行
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 存储数据
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

//        plannerEntityBatch.saveAll(cachedDataList);
        plannerEntityBatch.jpaSaveAllByBatch(cachedDataList);

        log.info("存储数据库成功！");
    }
}
