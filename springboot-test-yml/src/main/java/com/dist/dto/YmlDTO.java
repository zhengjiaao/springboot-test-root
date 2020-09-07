package com.dist.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-28 10:48
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class YmlDTO implements Serializable {
    private String ymlName;
    private String ymlPath;
}
