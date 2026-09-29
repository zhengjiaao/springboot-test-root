package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 加密响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "加密响应")
public class EncryptResponse {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "密文（Base64）")
	private String cipherText;
}
