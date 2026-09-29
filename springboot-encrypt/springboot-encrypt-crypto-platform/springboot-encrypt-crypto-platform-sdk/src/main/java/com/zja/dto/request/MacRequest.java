package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息认证码计算请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "消息认证码计算请求（HMAC-SM3/HMAC-SHA256）")
public class MacRequest {

	@ApiModelProperty(value = "MAC密钥标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "原始数据（UTF-8文本）", required = true, example = "待认证的数据")
	private String data;
}
