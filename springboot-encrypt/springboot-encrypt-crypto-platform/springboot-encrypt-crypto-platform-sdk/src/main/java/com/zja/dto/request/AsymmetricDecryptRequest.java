package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 非对称解密请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "非对称解密请求（SM2/RSA私钥解密）")
public class AsymmetricDecryptRequest {

	@ApiModelProperty(value = "非对称密钥对标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "密文（Base64）", required = true)
	private String cipherText;
}
