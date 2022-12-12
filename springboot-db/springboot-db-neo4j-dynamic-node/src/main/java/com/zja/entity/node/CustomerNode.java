package com.zja.entity.node;

import com.zja.entity.BaseNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：顾客
 */
@Data
@NodeEntity(label = "Customer")
public class CustomerNode extends BaseNode {
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 类型：1=普通客户、2=会员客户、3=高级会员客户
     */
    private int type;
    /**
     * 测试空属性值：当属性值为空，不会入库保存字段
     */
    private String test;
}
