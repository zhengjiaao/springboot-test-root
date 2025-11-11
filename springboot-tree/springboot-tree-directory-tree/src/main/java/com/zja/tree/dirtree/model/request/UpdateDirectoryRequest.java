package com.zja.tree.dirtree.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2025/11/06 10:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("UpdateDirectoryRequest 新增 或 更新 信息")
public class UpdateDirectoryRequest implements Serializable {

    @ApiModelProperty("节点名称")
    private String name;
    @ApiModelProperty("节点别名")
    private String alias;
    @ApiModelProperty("节点描述")
    private String description;
    @ApiModelProperty("节点排序字段")
    private Integer sortOrder;
    @ApiModelProperty("节点图标")
    private String icon;
    @ApiModelProperty("节点自定义扩展属性")
    private Map<String, String> customAttributes;

}