package com.zja.common;

/**
 * 平台常量定义
 *
 * @author: zhengja
 */
public final class Constants {

	private Constants() {
	}

	// ===== 在线接入（HTTP/在线SDK）请求头 =====
	/** 三方应用凭证标识 */
	public static final String HEADER_APP_KEY = "X-App-Key";
	/** 请求时间戳（毫秒） */
	public static final String HEADER_TIMESTAMP = "X-Timestamp";
	/** 请求随机串（防重放） */
	public static final String HEADER_NONCE = "X-Nonce";
	/** 请求签名：HMAC-SM3(appSecret, stringToSign) 的十六进制串 */
	public static final String HEADER_SIGNATURE = "X-Signature";

	// ===== 拦截器向控制器传递当前应用标识 =====
	public static final String ATTR_APP_KEY = "CURRENT_APP_KEY";

	// ===== 业务响应码 =====
	/** 参数错误 */
	public static final int CODE_BAD_REQUEST = 400;
	/** 接入认证失败 */
	public static final int CODE_UNAUTHORIZED = 401;
	/** 无权访问（如跨应用访问密钥） */
	public static final int CODE_FORBIDDEN = 403;
	/** 请求体过大（请求体缓存过滤器返回） */
	public static final int CODE_PAYLOAD_TOO_LARGE = 413;
	/** 资源不存在 */
	public static final int CODE_NOT_FOUND = 404;
	/** 业务处理失败 */
	public static final int CODE_INTERNAL_ERROR = 500;

	// ===== 应用/密钥状态 =====
	public static final String STATUS_ENABLED = "ENABLED";
	public static final String STATUS_DISABLED = "DISABLED";
	public static final String STATUS_DESTROYED = "DESTROYED";
}
