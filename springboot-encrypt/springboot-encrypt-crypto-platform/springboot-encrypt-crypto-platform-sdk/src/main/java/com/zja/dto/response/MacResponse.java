package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息认证码响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "消息认证码响应")
public class MacResponse {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "MAC值（16进制小写）")
	private String mac;
}
