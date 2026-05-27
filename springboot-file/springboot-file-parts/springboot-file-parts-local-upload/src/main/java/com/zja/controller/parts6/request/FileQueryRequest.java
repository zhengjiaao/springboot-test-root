package com.zja.controller.parts6.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 文件信息查询请求
 *
 * @author: zhengja
 * @since: 2026/04/23
 */
@Data
@ApiModel("FileQueryRequest")
public class FileQueryRequest implements Serializable {

    @ApiModelProperty(value = "文件唯一标识（MD5）", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    @NotBlank(message = "文件唯一标识不能为空")
    private String fileIdentifier;

    @ApiModelProperty(value = "文件名", required = true, example = "document.zip")
    @NotBlank(message = "文件名不能为空")
    private String fileName;
}
