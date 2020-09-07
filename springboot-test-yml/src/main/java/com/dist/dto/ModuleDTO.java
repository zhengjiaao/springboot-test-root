package com.dist.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-28 10:46
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class ModuleDTO implements Serializable {
    private String moduleName;
    private String modulePath;
    private List<YmlDTO> ymlDTOS;
}
