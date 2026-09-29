package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 解密响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "解密响应")
public class DecryptResponse {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "明文（UTF-8文本）")
	private String plainText;
}
