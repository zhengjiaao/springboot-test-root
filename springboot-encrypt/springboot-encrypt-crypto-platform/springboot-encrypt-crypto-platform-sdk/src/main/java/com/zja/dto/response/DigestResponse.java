package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 摘要响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "摘要响应")
public class DigestResponse {

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "摘要值（16进制小写）")
	private String digest;
}
