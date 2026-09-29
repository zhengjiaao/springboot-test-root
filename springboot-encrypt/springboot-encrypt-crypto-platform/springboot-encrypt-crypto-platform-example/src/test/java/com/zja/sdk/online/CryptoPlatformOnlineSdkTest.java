package com.zja.sdk.online;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.common.ApiResponse;
import com.zja.common.Constants;
import com.zja.dto.request.AppRegisterRequest;
import com.zja.dto.response.AppRegisterResponse;
import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.dto.response.KeyView;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import com.zja.security.SignatureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 在线SDK接入集成测试：模拟三方系统从注册到全量密码运算的完整流程
 *
 * <p>演示应用（demo-app）由平台启动时自动初始化，凭证来自application.yml。</p>
 *
 * @author: zhengja
 */
@DisplayName("在线SDK：接入平台全流程")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CryptoPlatformOnlineSdkTest {

	private static final String DEMO_APP_KEY = "demo-app";
	private static final String DEMO_APP_SECRET = "demo-app-secret-123456";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private String baseUrl;
	private CryptoPlatformClient demoClient;

	@BeforeEach
	void setUp() {
		baseUrl = "http://localhost:" + port;
		demoClient = new CryptoPlatformClient(baseUrl, DEMO_APP_KEY, DEMO_APP_SECRET);
	}

	@Test
	@DisplayName("全流程：创建SM4/SM2/HMAC密钥 → 加解密/签名验签/摘要/MAC")
	void fullCryptoFlow() {
		// 1. 创建SM4密钥并加解密
		KeyCreateResponse sm4Key = demoClient.createKey("SM4", "测试-数据加密密钥");
		assertNotNull(sm4Key.getKeyId());
		String plainText = "在线SDK全流程测试数据-机密 123";
		EncryptResponse encrypt = demoClient.symmetricEncrypt(sm4Key.getKeyId(), plainText);
		DecryptResponse decrypt = demoClient.symmetricDecrypt(sm4Key.getKeyId(), encrypt.getCipherText());
		assertEquals(plainText, decrypt.getPlainText());

		// 2. 创建SM2密钥对：加解密 + 签名验签
		KeyCreateResponse sm2Key = demoClient.createKey("SM2", "测试-签名密钥对");
		assertNotNull(sm2Key.getPublicKey());
		EncryptResponse sm2Encrypt = demoClient.asymmetricEncrypt(sm2Key.getKeyId(), "SM2小段数据");
		assertEquals("SM2小段数据", demoClient.asymmetricDecrypt(sm2Key.getKeyId(), sm2Encrypt.getCipherText()).getPlainText());

		String signData = "待签名业务报文";
		SignResponse sign = demoClient.sign(sm2Key.getKeyId(), signData);
		assertEquals("SM3withSM2", sign.getAlgorithm());
		assertTrue(demoClient.verify(sm2Key.getKeyId(), signData, sign.getSignature()).isVerified());

		// 3. 摘要（SM3）
		DigestResponse digest = demoClient.digest("SM3", "abc");
		assertEquals("66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0", digest.getDigest());

		// 4. HMAC-SM3消息认证码
		KeyCreateResponse macKey = demoClient.createKey("HMAC_SM3", "测试-MAC密钥");
		MacResponse mac = demoClient.mac(macKey.getKeyId(), "完整性校验");
		assertEquals(64, mac.getMac().length());

		// 5. 密钥列表与销毁
		List<KeyView> keys = demoClient.listKeys();
		assertTrue(keys.size() >= 3);
		demoClient.destroyKey(macKey.getKeyId());
		assertThrows(OnlineSdkException.class, () -> demoClient.getKey(macKey.getKeyId()));
	}

	@Test
	@DisplayName("注册新应用 → 新凭证接入（三方自主接入流程）")
	void registerNewAppThenAccess() {
		AppRegisterRequest registerRequest = new AppRegisterRequest();
		registerRequest.setAppName("在线注册的三方应用");
		registerRequest.setContact("tester");

		ResponseEntity<ApiResponse<AppRegisterResponse>> response = restTemplate.exchange(
				"/api/app/register", HttpMethod.POST, new HttpEntity<>(registerRequest),
				new ParameterizedTypeReference<ApiResponse<AppRegisterResponse>>() {
				});
		assertEquals(ApiResponse.CODE_SUCCESS, response.getBody().getCode());
		AppRegisterResponse registered = response.getBody().getData();
		assertNotNull(registered.getAppSecret());

		// 使用新凭证通过在线SDK接入
		CryptoPlatformClient newClient = new CryptoPlatformClient(baseUrl, registered.getAppKey(), registered.getAppSecret());
		KeyCreateResponse key = newClient.createKey("SM4", "新应用密钥");
		EncryptResponse encrypt = newClient.symmetricEncrypt(key.getKeyId(), "新应用数据");
		assertEquals("新应用数据", newClient.symmetricDecrypt(key.getKeyId(), encrypt.getCipherText()).getPlainText());
	}

	@Test
	@DisplayName("接入安全：错误appSecret被拒绝（401）")
	void wrongSecretRejected() {
		CryptoPlatformClient badClient = new CryptoPlatformClient(baseUrl, DEMO_APP_KEY, "wrong-secret");
		OnlineSdkException exception = assertThrows(OnlineSdkException.class,
				() -> badClient.digest("SM3", "data"));
		assertEquals(401, exception.getCode());
	}

	@Test
	@DisplayName("接入安全：重放请求被拒绝（nonce防重放）")
	void replayAttackRejected() throws Exception {
		String path = "/api/crypto/digest";
		byte[] bodyBytes = "{\"algorithm\":\"SM3\",\"data\":\"replay-test\"}".getBytes(StandardCharsets.UTF_8);
		String timestamp = String.valueOf(System.currentTimeMillis());
		String nonce = UUID.randomUUID().toString().replace("-", "");
		String stringToSign = SignatureUtil.buildStringToSign(DEMO_APP_KEY, timestamp, nonce, "POST", path, bodyBytes);
		String signature = SignatureUtil.sign(DEMO_APP_SECRET, stringToSign);

		// 第一次请求成功
		assertEquals(ApiResponse.CODE_SUCCESS, postRaw(path, bodyBytes, timestamp, nonce, signature));
		// 相同nonce+时间戳+签名的第二次请求被判定为重放
		assertEquals(401, postRaw(path, bodyBytes, timestamp, nonce, signature));
	}

	@Test
	@DisplayName("密钥隔离：应用A无法使用应用B的密钥")
	void crossAppKeyIsolation() {
		// 注册新应用A
		AppRegisterRequest registerRequest = new AppRegisterRequest();
		registerRequest.setAppName("密钥隔离测试应用");
		ResponseEntity<ApiResponse<AppRegisterResponse>> response = restTemplate.exchange(
				"/api/app/register", HttpMethod.POST, new HttpEntity<>(registerRequest),
				new ParameterizedTypeReference<ApiResponse<AppRegisterResponse>>() {
				});
		AppRegisterResponse appA = response.getBody().getData();
		CryptoPlatformClient clientA = new CryptoPlatformClient(baseUrl, appA.getAppKey(), appA.getAppSecret());

		// 拿到演示应用B的密钥
		KeyView demoKey = demoClient.listKeys().get(0);

		// 应用A使用应用B的密钥 → 404（密钥不存在，防止跨应用探测）
		OnlineSdkException exception = assertThrows(OnlineSdkException.class,
				() -> clientA.symmetricEncrypt(demoKey.getKeyId(), "越权数据"));
		assertEquals(404, exception.getCode());
	}

	/** 手工发送带指定认证头的原始POST请求，返回平台业务码 */
	private int postRaw(String path, byte[] body, String timestamp, String nonce, String signature) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl + path).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		connection.setRequestProperty(Constants.HEADER_APP_KEY, DEMO_APP_KEY);
		connection.setRequestProperty(Constants.HEADER_TIMESTAMP, timestamp);
		connection.setRequestProperty(Constants.HEADER_NONCE, nonce);
		connection.setRequestProperty(Constants.HEADER_SIGNATURE, signature);

		try (OutputStream out = connection.getOutputStream()) {
			out.write(body);
			out.flush();
		}

		InputStream in = connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] chunk = new byte[4096];
		int n;
		while (in != null && (n = in.read(chunk)) != -1) {
			buffer.write(chunk, 0, n);
		}
		connection.disconnect();

		ApiResponse<?> apiResponse = objectMapper.readValue(new String(buffer.toByteArray(), StandardCharsets.UTF_8), ApiResponse.class);
		return apiResponse.getCode();
	}
}
