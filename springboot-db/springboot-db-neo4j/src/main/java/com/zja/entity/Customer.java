package com.zja.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
//@NodeEntity
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseNode{
    private String name;
}
