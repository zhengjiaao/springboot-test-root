package com.zja.dto.arcgis;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-22 14:49
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：图层信息
 */
@Data
public class LayerDTO implements Serializable {
    //图层id
    private Integer id;
    //图层名称
    private String name;
    //父层ID 根目录-1
    private Integer parentLayerId;
    //默认可见 true
    private Boolean defaultVisibility;
}
