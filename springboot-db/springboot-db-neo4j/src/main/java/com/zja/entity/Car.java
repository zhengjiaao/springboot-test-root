package com.zja.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
//@NodeEntity
@EqualsAndHashCode(callSuper = false)
public class Car extends BaseNode {
    private String brand;
}
