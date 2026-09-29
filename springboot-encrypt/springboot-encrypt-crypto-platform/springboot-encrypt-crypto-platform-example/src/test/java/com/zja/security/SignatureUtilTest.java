package com.zja.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 接入签名工具单元测试：签名串构造、HMAC-SM3签名与验证
 *
 * @author: zhengja
 */
@DisplayName("在线接入签名（HMAC-SM3）")
class SignatureUtilTest {

	private static final String APP_KEY = "app-test";
	private static final String APP_SECRET = "test-secret";

	@Test
	@DisplayName("签名串格式：6个字段以换行分隔，body参与SM3摘要")
	void buildStringToSign() {
		byte[] body = "hello".getBytes(StandardCharsets.UTF_8);
		String stringToSign = SignatureUtil.buildStringToSign(APP_KEY, "1700000000000", "nonce-1", "POST", "/api/crypto/digest", body);

		String[] parts = stringToSign.split("\n");
		assertEquals(6, parts.length);
		assertEquals(APP_KEY, parts[0]);
		assertEquals("1700000000000", parts[1]);
		assertEquals("nonce-1", parts[2]);
		assertEquals("POST", parts[3]);
		assertEquals("/api/crypto/digest", parts[4]);
		// 第6段为body的SM3摘要（64位hex）
		assertEquals(64, parts[5].length());
	}

	@Test
	@DisplayName("签名与验证：正确密钥验签通过，篡改验签失败")
	void signAndVerify() {
		String stringToSign = SignatureUtil.buildStringToSign(APP_KEY, "1700000000000", "nonce-1", "POST", "/api/crypto/digest", new byte[0]);
		String signature = SignatureUtil.sign(APP_SECRET, stringToSign);

		assertEquals(64, signature.length());
		assertTrue(SignatureUtil.verify(APP_SECRET, stringToSign, signature));
		// 错误密钥验证失败
		assertFalse(SignatureUtil.verify("wrong-secret", stringToSign, signature));
		// 篡改签名串后验证失败
		assertFalse(SignatureUtil.verify(APP_SECRET, stringToSign + "x", signature));
		// 签名值被篡改后验证失败
		assertFalse(SignatureUtil.verify(APP_SECRET, stringToSign, "0" + signature.substring(1)));
		// 大写签名也能通过（兼容不同语言SDK的hex大小写差异）
		assertTrue(SignatureUtil.verify(APP_SECRET, stringToSign, signature.toUpperCase()));
	}

	@Test
	@DisplayName("body不同则签名不同（防篡改基础）")
	void bodyBoundToSignature() {
		String sign1 = SignatureUtil.sign(APP_SECRET, SignatureUtil.buildStringToSign(APP_KEY, "1", "n", "POST", "/p", "a".getBytes(StandardCharsets.UTF_8)));
		String sign2 = SignatureUtil.sign(APP_SECRET, SignatureUtil.buildStringToSign(APP_KEY, "1", "n", "POST", "/p", "b".getBytes(StandardCharsets.UTF_8)));
		assertNotEquals(sign1, sign2);
	}
}
