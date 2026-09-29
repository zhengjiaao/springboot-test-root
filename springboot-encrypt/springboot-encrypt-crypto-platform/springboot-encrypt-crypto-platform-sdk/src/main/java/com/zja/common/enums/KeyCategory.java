package com.zja.common.enums;

/**
 * 算法类别
 *
 * @author: zhengja
 */
public enum KeyCategory {

	/** 对称加密 */
	SYMMETRIC,
	/** 非对称加密（密钥对） */
	ASYMMETRIC,
	/** 摘要（无需密钥） */
	DIGEST,
	/** 消息认证码 */
	MAC
}
