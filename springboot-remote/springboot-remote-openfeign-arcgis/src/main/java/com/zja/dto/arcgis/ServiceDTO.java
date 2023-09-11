package com.zja.dto.arcgis;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-22 14:52
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：服务信息
 */
@Data
public class ServiceDTO implements Serializable {
    //服务名称and服务地址
    private String name;
    //服务类型
    private String type;
}
