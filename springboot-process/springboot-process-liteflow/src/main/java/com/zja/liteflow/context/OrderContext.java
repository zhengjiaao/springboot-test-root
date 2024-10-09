package com.zja.liteflow.context;

import com.yomahub.liteflow.context.ContextBean;
import lombok.Data;

/**
 * 订单实体
 *
 * @Author: zhengja
 * @Date: 2024-10-08 13:58
 */
@Data
@ContextBean("orderContext") // 设置上下文别名，默认 OrderContext --> orderContext
public class OrderContext {
    private String id;
    private String name;
    private String status;
    private String type;
    private boolean oversea;
    private String userId;
}
