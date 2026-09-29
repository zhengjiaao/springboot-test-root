package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密钥创建请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "密钥创建请求（密钥归属当前接入应用）")
public class KeyCreateRequest {

	@ApiModelProperty(value = "算法：SM4/AES_128/AES_256/SM2/RSA_2048/HMAC_SM3/HMAC_SHA_256", required = true, example = "SM4")
	private String algorithm;

	@ApiModelProperty(value = "密钥别名（便于业务识别）", example = "订单表字段加密密钥")
	private String alias;

	@ApiModelProperty(value = "备注", example = "2026年密改专用")
	private String remark;
}
