package com.zja.core;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SM2 非对称算法：密钥对生成、公钥加密、私钥解密、私钥签名、公钥验签
 *
 * <p>底层依赖：bcprov-jdk15to18 轻量API（自主封装，不依赖hutool-crypto，无需JCE Provider注册）；
 * 密文格式 C1C3C2（新国标推荐），签名算法为 SM3withSM2（国密标准签名，DER编码）。</p>
 *
 * @author: zhengja
 */
public final class Sm2CryptoUtil {

	/** 生成密钥对结果中的公键名 */
	public static final String PUBLIC_KEY = "publicKey";
	/** 生成密钥对结果中的私键名 */
	public static final String PRIVATE_KEY = "privateKey";

	private static final SecureRandom RANDOM = new SecureRandom();

	/** SM2 标准曲线 sm2p256v1 域参数（国密唯一定义曲线） */
	private static final ECDomainParameters DOMAIN_PARAMS = createDomainParameters();

	private Sm2CryptoUtil() {
	}

	private static ECDomainParameters createDomainParameters() {
		X9ECParameters x9 = GMNamedCurves.getByName("sm2p256v1");
		return new ECDomainParameters(x9.getCurve(), x9.getG(), x9.getN(), x9.getH());
	}

	/**
	 * 生成 SM2 密钥对
	 *
	 * @return {publicKey: X.509编码Base64公钥, privateKey: PKCS#8编码Base64私钥}
	 */
	public static Map<String, String> generateKeyPairBase64() {
		try {
			ECKeyPairGenerator generator = new ECKeyPairGenerator();
			generator.init(new ECKeyGenerationParameters(DOMAIN_PARAMS, RANDOM));
			AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();

			Map<String, String> keyPairMap = new LinkedHashMap<>();
			keyPairMap.put(PUBLIC_KEY, toX509Base64((ECPublicKeyParameters) keyPair.getPublic()));
			keyPairMap.put(PRIVATE_KEY, toPkcs8Base64((ECPrivateKeyParameters) keyPair.getPrivate()));
			return keyPairMap;
		} catch (Exception e) {
			throw new IllegalStateException("生成SM2密钥对失败", e);
		}
	}

	/**
	 * 公钥加密（适合加密小段数据，如会话密钥、摘要值）
	 *
	 * <p>密文结构：C1(65字节椭圆曲线随机点) + C3(32字节SM3摘要) + C2(密文)，Base64编码。</p>
	 *
	 * @param publicKeyBase64 X.509编码Base64公钥
	 * @param plainText       明文
	 * @return Base64密文
	 */
	public static String encryptByPublicKey(String publicKeyBase64, String plainText) {
		try {
			byte[] in = plainText.getBytes(StandardCharsets.UTF_8);
			SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
			engine.init(true, new ParametersWithRandom(toPublicParams(publicKeyBase64), RANDOM));
			return Base64.getEncoder().encodeToString(engine.processBlock(in, 0, in.length));
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("SM2加密失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param privateKeyBase64 PKCS#8编码Base64私钥
	 * @param cipherTextBase64 公钥加密产生的Base64密文
	 * @return 明文
	 */
	public static String decryptByPrivateKey(String privateKeyBase64, String cipherTextBase64) {
		try {
			byte[] in = Base64.getDecoder().decode(cipherTextBase64);
			SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
			engine.init(false, toPrivateParams(privateKeyBase64));
			return new String(engine.processBlock(in, 0, in.length), StandardCharsets.UTF_8);
		} catch (IllegalStateException | IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("SM2解密失败，请检查密文与密钥是否匹配", e);
		}
	}

	/**
	 * 私钥签名（SM3withSM2，返回DER编码签名值的Base64串）
	 *
	 * @param privateKeyBase64 PKCS#8编码Base64私钥
	 * @param data             待签名数据
	 * @return Base64签名值
	 */
	public static String signByPrivateKey(String privateKeyBase64, String data) {
		try {
			SM2Signer signer = new SM2Signer();
			signer.init(true, new ParametersWithRandom(toPrivateParams(privateKeyBase64), RANDOM));
			byte[] in = data.getBytes(StandardCharsets.UTF_8);
			signer.update(in, 0, in.length);
			return Base64.getEncoder().encodeToString(signer.generateSignature());
		} catch (Exception e) {
			throw new IllegalStateException("SM2签名失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 公钥验签（SM3withSM2）
	 *
	 * @param publicKeyBase64 X.509编码Base64公钥
	 * @param data            原始数据
	 * @param signBase64      Base64签名值
	 * @return 验签结果
	 */
	public static boolean verifyByPublicKey(String publicKeyBase64, String data, String signBase64) {
		try {
			SM2Signer signer = new SM2Signer();
			signer.init(false, toPublicParams(publicKeyBase64));
			byte[] in = data.getBytes(StandardCharsets.UTF_8);
			signer.update(in, 0, in.length);
			return signer.verifySignature(Base64.getDecoder().decode(signBase64));
		} catch (Exception e) {
			return false;
		}
	}

	/** X.509编码Base64公钥 → BC轻量加密参数 */
	private static ECPublicKeyParameters toPublicParams(String publicKeyBase64) throws IOException {
		return (ECPublicKeyParameters) PublicKeyFactory.createKey(Base64.getDecoder().decode(publicKeyBase64));
	}

	/** PKCS#8编码Base64私钥 → BC轻量解密参数 */
	private static ECPrivateKeyParameters toPrivateParams(String privateKeyBase64) throws IOException {
		return (ECPrivateKeyParameters) PrivateKeyFactory.createKey(Base64.getDecoder().decode(privateKeyBase64));
	}

	private static String toX509Base64(ECPublicKeyParameters publicKey) throws IOException {
		return Base64.getEncoder().encodeToString(
				SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey).getEncoded());
	}

	private static String toPkcs8Base64(ECPrivateKeyParameters privateKey) throws IOException {
		return Base64.getEncoder().encodeToString(
				PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey).getEncoded());
	}
}
