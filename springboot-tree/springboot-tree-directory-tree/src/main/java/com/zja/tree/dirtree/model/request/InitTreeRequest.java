package com.zja.tree.dirtree.model.request;

import com.zja.tree.dirtree.model.dto.TreeNodeData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 初始化 请求参数
 *
 * @author: zhengja
 * @since: 2025/11/06 11:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("InitTreeRequest 新增 或 更新 初始化信息")
public class InitTreeRequest implements Serializable {

    @NotBlank(message = "业务类型不能为空")
    private String businessType;

    @ApiModelProperty("业务id")
    private String businessId;

    @Valid
    @NotEmpty(message = "目录树节点不能为空")
    private List<TreeNodeData> nodes;

    @ApiModelProperty("是否覆盖，如果覆盖，先删除现有的")
    private boolean overwrite = false;

    @ApiModelProperty("创建人")
    private String createdBy;

}