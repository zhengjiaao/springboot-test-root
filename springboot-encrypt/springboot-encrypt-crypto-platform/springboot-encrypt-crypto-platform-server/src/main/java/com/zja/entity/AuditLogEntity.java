package com.zja.entity;

import lombok.Data;

import java.util.Date;

/**
 * 密码运算审计日志
 *
 * <p>密评要求：所有密钥操作与密码运算必须可审计。演示实现为内存有界队列，生产环境应落库或上报审计平台。</p>
 *
 * @author: zhengja
 */
@Data
public class AuditLogEntity {

	/** 日志id */
	private long id;
	/** 发起应用 */
	private String appKey;
	/** 操作类型：如 KEY_CREATE / SYMMETRIC_ENCRYPT / SIGN / VERIFY */
	private String action;
	/** 涉及密钥 */
	private String keyId;
	/** 算法 */
	private String algorithm;
	/** 是否成功 */
	private boolean success;
	/** 说明信息 */
	private String message;
	/** 耗时（毫秒） */
	private long costMs;
	/** 来源IP */
	private String clientIp;
	/** 发生时间 */
	private Date timestamp;
}
