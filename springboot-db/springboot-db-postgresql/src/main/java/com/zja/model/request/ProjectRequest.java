package com.zja.model.request;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2024/09/27 9:34
 */
@Data
@ApiModel("ProjectRequest 新增 或 更新 信息")
public class ProjectRequest implements Serializable {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("配置内容")
    private JSONObject configJson;

    @ApiModelProperty("大文本内容")
    private String configText;
}