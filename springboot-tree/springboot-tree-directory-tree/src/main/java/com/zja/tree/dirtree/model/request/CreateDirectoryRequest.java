package com.zja.tree.dirtree.model.request;

import com.zja.tree.dirtree.entity.enums.NodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2025/11/06 10:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("CreateDirectoryRequest 新增 或 更新 信息")
public class CreateDirectoryRequest implements Serializable {

    @ApiModelProperty("节点名称")
    @NotBlank(message = "节点名称不能为空")
    private String name;
    @ApiModelProperty("别名")
    private String alias;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("父id")
    private Long parentId;

    @NotBlank(message = "业务类型不能为空")
    private String businessType;
    @ApiModelProperty("业务id")
    private String businessId;

    @NotNull(message = "节点类型不能为空")
    private NodeType nodeType;

    @ApiModelProperty("排序")
    private Integer sortOrder = 0;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("自定义扩展属性")
    private Map<String, String> customAttributes;
}