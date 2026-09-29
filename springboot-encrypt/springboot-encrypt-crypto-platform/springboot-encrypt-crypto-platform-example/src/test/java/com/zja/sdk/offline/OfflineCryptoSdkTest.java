package com.zja.sdk.offline;

import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 离线SDK单元测试：本地密钥生成与全算法运算
 *
 * @author: zhengja
 */
@DisplayName("离线SDK：本地运算全流程")
class OfflineCryptoSdkTest {

	private OfflineCryptoSdk sdk;

	@BeforeEach
	void setUp() {
		sdk = new OfflineCryptoSdk();
	}

	@Test
	@DisplayName("SM4对称加解密（国密密改对标AES）")
	void sm4RoundTrip() {
		OfflineKey key = sdk.createKey("SM4", "测试SM4密钥");
		String plainText = "离线SM4加密测试数据 123@#$ 中文";

		EncryptResponse encrypt = sdk.symmetricEncrypt(key.getKeyId(), plainText);
		DecryptResponse decrypt = sdk.symmetricDecrypt(key.getKeyId(), encrypt.getCipherText());

		assertEquals(plainText, decrypt.getPlainText());
		assertEquals("SM4", decrypt.getAlgorithm());
		// 相同明文两次加密，密文应不同（随机IV保证语义安全）
		assertNotEquals(encrypt.getCipherText(), sdk.symmetricEncrypt(key.getKeyId(), plainText).getCipherText());
	}

	@Test
	@DisplayName("AES-256对称加解密")
	void aesRoundTrip() {
		OfflineKey key = sdk.createKey("AES_256", "测试AES密钥");
		String plainText = "AES offline test data";

		EncryptResponse encrypt = sdk.symmetricEncrypt(key.getKeyId(), plainText);
		assertEquals(plainText, sdk.symmetricDecrypt(key.getKeyId(), encrypt.getCipherText()).getPlainText());
	}

	@Test
	@DisplayName("SM2非对称加解密 + 签名验签（国密密改对标RSA）")
	void sm2RoundTrip() {
		OfflineKey key = sdk.createKey("SM2", "测试SM2密钥对");
		assertNotNull(key.getPublicKeyBase64());
		assertNotNull(key.getPrivateKeyBase64());

		String plainText = "SM2会话密钥数据";
		EncryptResponse encrypt = sdk.asymmetricEncrypt(key.getKeyId(), plainText);
		assertEquals(plainText, sdk.asymmetricDecrypt(key.getKeyId(), encrypt.getCipherText()).getPlainText());

		String data = "待签名报文-离线SDK";
		SignResponse sign = sdk.sign(key.getKeyId(), data);
		assertEquals("SM3withSM2", sign.getAlgorithm());
		assertTrue(sdk.verify(key.getKeyId(), data, sign.getSignature()).isVerified());
		// 篡改数据后验签失败
		assertFalse(sdk.verify(key.getKeyId(), data + "-tampered", sign.getSignature()).isVerified());
	}

	@Test
	@DisplayName("RSA非对称加解密 + 签名验签")
	void rsaRoundTrip() {
		OfflineKey key = sdk.createKey("RSA_2048", "测试RSA密钥对");
		String plainText = "RSA offline data";

		EncryptResponse encrypt = sdk.asymmetricEncrypt(key.getKeyId(), plainText);
		assertEquals(plainText, sdk.asymmetricDecrypt(key.getKeyId(), encrypt.getCipherText()).getPlainText());

		SignResponse sign = sdk.sign(key.getKeyId(), plainText);
		assertEquals("SHA256withRSA", sign.getAlgorithm());
		assertTrue(sdk.verify(key.getKeyId(), plainText, sign.getSignature()).isVerified());
	}

	@Test
	@DisplayName("SM3摘要（含标准测试向量）与HMAC-SM3")
	void digestAndMac() {
		// SM3标准测试向量：SM3("abc") = 66c7f0f4...
		DigestResponse digest = sdk.digest("SM3", "abc");
		assertEquals("66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0", digest.getDigest());

		OfflineKey macKey = sdk.createKey("HMAC_SM3", "测试HMAC密钥");
		MacResponse mac = sdk.mac(macKey.getKeyId(), "完整性校验数据");
		assertEquals(64, mac.getMac().length());
	}

	@Test
	@DisplayName("密钥导入导出（模拟持久化后重启加载）")
	void keyImportExport() {
		OfflineKey key = sdk.createKey("SM4", "待导出密钥");

		// 模拟持久化：导出（含密钥材料）→ 新SDK实例导入
		OfflineCryptoSdk newSdk = new OfflineCryptoSdk();
		OfflineKey imported = new OfflineKey();
		imported.setKeyId(key.getKeyId());
		imported.setAlgorithm(key.getAlgorithm());
		imported.setCategory(key.getCategory());
		imported.setAlias(key.getAlias());
		imported.setSecretKeyBase64(key.getSecretKeyBase64());
		imported.setCreateTime(key.getCreateTime());
		newSdk.importKey(imported);

		String plainText = "跨SDK实例数据";
		String cipherText = sdk.symmetricEncrypt(key.getKeyId(), plainText).getCipherText();
		// 原SDK加密的数据，导入同一密钥的新SDK可以解密（离线互通的基础）
		assertEquals(plainText, newSdk.symmetricDecrypt(imported.getKeyId(), cipherText).getPlainText());
	}

	@Test
	@DisplayName("密钥列表与删除")
	void keyManage() {
		sdk.createKey("SM4", "key1");
		sdk.createKey("HMAC_SM3", "key2");
		assertEquals(2, sdk.listKeys().size());

		String removedId = sdk.listKeys().get(0).getKeyId();
		sdk.removeKey(removedId);
		assertEquals(1, sdk.listKeys().size());
		assertEquals(Optional.empty(), sdk.listKeys().stream()
				.filter(k -> k.getKeyId().equals(removedId)).findFirst());
	}
}
