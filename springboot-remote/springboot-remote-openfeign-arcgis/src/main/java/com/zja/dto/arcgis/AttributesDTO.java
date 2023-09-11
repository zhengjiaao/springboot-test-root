package com.zja.dto.arcgis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:31
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：属性-区域基本信息
 */
@Api("属性信息")
public class AttributesDTO implements Serializable {

    @ApiModelProperty("对象id")
    private int objectid;
    @ApiModelProperty("市名称")
    private String name;
    @ApiModelProperty("行政区划代码(市级)")
    private String code;
    @ApiModelProperty("面积")
    private double mj;
    @ApiModelProperty("形状面积")
    private double shape_area;
    @ApiModelProperty("形状长度")
    private double shape_len;


    @JsonProperty("OBJECTID")
    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
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
    public double getMj() {
        return mj;
    }

    public void setMj(double mj) {
        this.mj = mj;
    }

    @JsonProperty("SHAPE.AREA")
    public double getShape_area() {
        return shape_area;
    }

    public void setShape_area(double shape_area) {
        this.shape_area = shape_area;
    }

    @JsonProperty("SHAPE.LEN")
    public double getShape_len() {
        return shape_len;
    }

    public void setShape_len(double shape_len) {
        this.shape_len = shape_len;
    }
}
