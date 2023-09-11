package com.zja.dto.arcgis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:29
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：字段别名
 */
@Api("字段别名信息")
public class FieldAliasesDTO implements Serializable {

    @ApiModelProperty("对象id")
    private String objectid;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("码")
    private String code;
    @ApiModelProperty("面积")
    private String mj;
    @ApiModelProperty("形状面积")
    private String shape_area;
    @ApiModelProperty("形状长度")
    private String shape_len;


    @JsonProperty("OBJECTID")
    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    @JsonProperty("NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("MJ")
    public String getMj() {
        return mj;
    }

    public void setMj(String mj) {
        this.mj = mj;
    }

    @JsonProperty("SHAPE.AREA")
    public String getShape_area() {
        return shape_area;
    }

    public void setShape_area(String shape_area) {
        this.shape_area = shape_area;
    }

    @JsonProperty("SHAPE.LEN")
    public String getShape_len() {
        return shape_len;
    }

    public void setShape_len(String shape_len) {
        this.shape_len = shape_len;
    }
}
