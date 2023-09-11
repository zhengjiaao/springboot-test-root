package com.zja.dto.arcgis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:32
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：特征
 */
@Data
@Api("特征信息")
public class FeaturesDTO implements Serializable {

    @ApiModelProperty("属性")
    private AttributesDTO attributes;
    @ApiModelProperty("几何对象")
    private GeometryDTO geometry;

}
