package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 应用注册响应（appSecret仅此一次返回，请妥善保存）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "应用注册响应，appSecret仅此一次返回")
public class AppRegisterResponse {

	@ApiModelProperty(value = "应用接入标识")
	private String appKey;

	@ApiModelProperty(value = "应用接入密钥（仅此一次返回，请妥善保存）")
	private String appSecret;

	@ApiModelProperty(value = "应用名称")
	private String appName;

	@ApiModelProperty(value = "状态：ENABLED")
	private String status;

	@ApiModelProperty(value = "注册时间")
	private Date createTime;
}
