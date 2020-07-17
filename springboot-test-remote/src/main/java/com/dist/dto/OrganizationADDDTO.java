package com.dist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yangmin
 * @date 2017/12/19
 */
@Data
@ApiModel(value = "添加组织机构信息")
public class OrganizationADDDTO implements Serializable {

    @ApiModelProperty(value = "机构名", required = true)
    private String fullname;

    @ApiModelProperty(value = "父机构的guid编码", required = true)
    private String parentguid;

    @ApiModelProperty(value = "机构类型：1=单位，2=部门，3=团队", required = true)
    private Integer orgtype;

    @ApiModelProperty(value="所属区域代码")
    private String ownRegion;
}
