package com.zja.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 三方接入应用
 *
 * @author: zhengja
 */
@Data
public class AppEntity {

	/** 主键 */
	private String id;
	/** 应用接入标识（对外公开） */
	private String appKey;
	/** 应用接入密钥（仅注册时返回一次，永不出现在响应中） */
	@JsonIgnore
	private String appSecret;
	/** 应用名称 */
	private String appName;
	/** 负责人/联系方式 */
	private String contact;
	/** 备注 */
	private String remark;
	/** 状态：ENABLED / DISABLED */
	private String status;
	/** 注册时间 */
	private Date createTime;
}
