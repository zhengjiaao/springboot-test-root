package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对称解密请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "对称解密请求（SM4/AES）")
public class SymmetricDecryptRequest {

	@ApiModelProperty(value = "对称密钥标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "密文（Base64）", required = true)
	private String cipherText;
}
