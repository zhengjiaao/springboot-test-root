package com.zja.dto.arcgis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:27
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：地图服务数据
 */
@Data
@ApiModel("地图服务数据")
public class MapServerDTO implements Serializable {
    @ApiModelProperty("显示字段名")
    private String displayFieldName;
    @ApiModelProperty("字段别名")
    private FieldAliasesDTO fieldAliases;
    @ApiModelProperty("几何类型")
    private String geometryType;
    @ApiModelProperty("空间参考")
    private SpatialReferenceDTO spatialReference;
    @ApiModelProperty("字段列表")
    private List<FieldsDTO> fields;
    @ApiModelProperty("特征列表")
    private List<FeaturesDTO> features;

}
