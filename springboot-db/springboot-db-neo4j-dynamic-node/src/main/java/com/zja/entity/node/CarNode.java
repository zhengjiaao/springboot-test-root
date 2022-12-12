package com.zja.entity.node;

import com.zja.entity.BaseNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：汽车
 */
@Data
@NodeEntity(label = "Car")
public class CarNode extends BaseNode {
    /**
     * 汽车品牌
     */
    private String carBrand;
}
