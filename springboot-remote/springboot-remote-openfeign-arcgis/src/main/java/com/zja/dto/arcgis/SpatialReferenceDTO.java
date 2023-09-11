package com.zja.dto.arcgis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:30
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：空间参考
 */
@Data
@Api("空间参考")
public class SpatialReferenceDTO implements Serializable {
    @ApiModelProperty("wkid")
    private int wkid;
    @ApiModelProperty("最新wkid")
    private int latestWkid;
}
