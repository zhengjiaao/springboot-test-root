package com.zja.security;

import com.zja.core.HmacUtil;
import com.zja.core.Sm3DigestUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 在线接入请求签名工具（在线SDK与HTTP接口共用）
 *
 * <p>签名算法：HMAC-SM3；签名串格式（各字段以换行符\n分隔）：</p>
 * <pre>
 * stringToSign = appKey + "\n" + timestamp + "\n" + nonce + "\n" + method + "\n" + uri(含query) + "\n" + SM3(body).hex
 * signature    = HMAC-SM3(appSecret, stringToSign) 转小写16进制
 * </pre>
 *
 * <p>防篡改：body参与签名；防重放：timestamp窗口 + nonce唯一性由拦截器校验。</p>
 *
 * @author: zhengja
 */
public final class SignatureUtil {

	private SignatureUtil() {
	}

	/** 构造签名串 */
	public static String buildStringToSign(String appKey, String timestamp, String nonce,
	                                        String method, String uri, byte[] body) {
		String bodyDigest = Sm3DigestUtil.digestHex(body == null ? new byte[0] : body);
		return appKey + "\n" + timestamp + "\n" + nonce + "\n" + method + "\n" + uri + "\n" + bodyDigest;
	}

	/** 计算签名：HMAC-SM3(appSecret, stringToSign)，返回小写16进制 */
	public static String sign(String appSecret, String stringToSign) {
		return HmacUtil.hmacSm3Hex(
				java.util.Base64.getEncoder().encodeToString(appSecret.getBytes(StandardCharsets.UTF_8)),
				stringToSign);
	}

	/** 验证签名（常量时间比较，防时序攻击） */
	public static boolean verify(String appSecret, String stringToSign, String signatureHex) {
		if (signatureHex == null || signatureHex.isEmpty()) {
			return false;
		}
		String expected = sign(appSecret, stringToSign);
		return MessageDigest.isEqual(
				expected.getBytes(StandardCharsets.UTF_8),
				signatureHex.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
	}
}
