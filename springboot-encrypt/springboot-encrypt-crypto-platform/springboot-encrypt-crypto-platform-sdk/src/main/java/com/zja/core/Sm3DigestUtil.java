package com.zja.core;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;

import java.nio.charset.StandardCharsets;

/**
 * SM3 摘要算法（国密标准杂凑算法，输出256位）
 *
 * @author: zhengja
 */
public final class Sm3DigestUtil {

	private Sm3DigestUtil() {
	}

	/** 计算 SM3 摘要 */
	public static byte[] digest(byte[] data) {
		SM3Digest sm3 = new SM3Digest();
		sm3.update(data, 0, data.length);
		byte[] out = new byte[sm3.getDigestSize()];
		sm3.doFinal(out, 0);
		return out;
	}

	/** 计算 SM3 摘要，返回16进制小写串（长度64） */
	public static String digestHex(String data) {
		return Hex.encodeHexString(digest(data.getBytes(StandardCharsets.UTF_8)));
	}

	/** 计算 SM3 摘要（字节入参），返回16进制小写串（长度64） */
	public static String digestHex(byte[] data) {
		return Hex.encodeHexString(digest(data));
	}

	/** 计算 SM3 摘要，返回Base64串 */
	public static String digestBase64(String data) {
		return java.util.Base64.getEncoder().encodeToString(digest(data.getBytes(StandardCharsets.UTF_8)));
	}
}
