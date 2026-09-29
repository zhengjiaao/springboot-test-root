package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验签响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "验签响应")
public class VerifyResponse {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "验签结果")
	private boolean verified;
}
