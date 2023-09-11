package com.zja.dto.arcgis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:31
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：几何信息
 */
@Data
@Api("几何信息")
public class GeometryDTO implements Serializable {
    @ApiModelProperty("所有多边形的点")
    private List<List<List<Double>>> rings;
    @ApiModelProperty("中心点坐标x")
    private String x;
    @ApiModelProperty("中心点坐标y")
    private String y;
}
