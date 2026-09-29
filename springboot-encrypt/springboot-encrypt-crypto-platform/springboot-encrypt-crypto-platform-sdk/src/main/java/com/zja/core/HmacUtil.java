package com.zja.core;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 消息认证码：HMAC-SM3（国密）/ HMAC-SHA256（国际）
 *
 * <p>用于数据完整性+来源真实性校验，密改对标：HMAC-SHA256 → HMAC-SM3。</p>
 *
 * @author: zhengja
 */
public final class HmacUtil {

	/** MAC密钥推荐长度 */
	public static final int KEY_SIZE = 32;

	private static final SecureRandom RANDOM = new SecureRandom();

	private HmacUtil() {
	}

	/** 生成32字节随机MAC密钥并返回Base64串 */
	public static String generateKeyBase64() {
		byte[] key = new byte[KEY_SIZE];
		RANDOM.nextBytes(key);
		return Base64.getEncoder().encodeToString(key);
	}

	/**
	 * HMAC-SM3 计算（底层BouncyCastle）
	 *
	 * @param keyBase64 Base64密钥
	 * @param data      待计算数据
	 * @return 16进制小写串（长度64）
	 */
	public static String hmacSm3Hex(String keyBase64, String data) {
		byte[] key = Base64.getDecoder().decode(keyBase64);
		byte[] in = data.getBytes(StandardCharsets.UTF_8);

		HMac hMac = new HMac(new SM3Digest());
		hMac.init(new KeyParameter(key));
		hMac.update(in, 0, in.length);
		byte[] out = new byte[hMac.getMacSize()];
		hMac.doFinal(out, 0);
		return Hex.encodeHexString(out);
	}

	/**
	 * HMAC-SHA256 计算（JDK标准实现）
	 *
	 * @param keyBase64 Base64密钥
	 * @param data      待计算数据
	 * @return 16进制小写串（长度64）
	 */
	public static String hmacSha256Hex(String keyBase64, String data) {
		try {
			byte[] key = Base64.getDecoder().decode(keyBase64);
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key, "HmacSHA256"));
			return Hex.encodeHexString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new IllegalStateException("HMAC-SHA256计算失败", e);
		}
	}
}
