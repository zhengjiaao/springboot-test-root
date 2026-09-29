package com.zja.sdk.online;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.common.ApiResponse;
import com.zja.common.Constants;
import com.zja.dto.request.AsymmetricDecryptRequest;
import com.zja.dto.request.AsymmetricEncryptRequest;
import com.zja.dto.request.DigestRequest;
import com.zja.dto.request.KeyCreateRequest;
import com.zja.dto.request.MacRequest;
import com.zja.dto.request.SignRequest;
import com.zja.dto.request.SymmetricDecryptRequest;
import com.zja.dto.request.SymmetricEncryptRequest;
import com.zja.dto.request.VerifyRequest;
import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.dto.response.KeyView;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import com.zja.security.SignatureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * 密码服务平台在线SDK（面向三方系统）
 *
 * <p>密钥由平台集中托管，本SDK自动完成接入签名（HMAC-SM3）、时间戳与防重放，三方业务代码只需三步：</p>
 * <pre>
 * CryptoPlatformClient client = new CryptoPlatformClient("http://platform-host:8086", appKey, appSecret);
 * KeyCreateResponse key = client.createKey("SM4", "订单加密密钥");
 * String cipher = client.symmetricEncrypt(key.getKeyId(), "机密数据");
 * </pre>
 *
 * <p>说明：本类位于独立SDK模块 springboot-encrypt-crypto-platform-sdk，可单独打包为
 * crypto-platform-sdk.jar 发布给三方系统引入，无需依赖服务端实现。</p>
 *
 * @author: zhengja
 */
public class CryptoPlatformClient {

	private final String baseUrl;
	private final String appKey;
	private final String appSecret;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/** 连接超时（毫秒） */
	private int connectTimeout = 5000;
	/** 读取超时（毫秒） */
	private int readTimeout = 15000;

	public CryptoPlatformClient(String baseUrl, String appKey, String appSecret) {
		if (baseUrl == null || baseUrl.trim().isEmpty()) {
			throw new IllegalArgumentException("baseUrl不能为空");
		}
		if (appKey == null || appKey.trim().isEmpty()) {
			throw new IllegalArgumentException("appKey不能为空");
		}
		if (appSecret == null || appSecret.trim().isEmpty()) {
			throw new IllegalArgumentException("appSecret不能为空");
		}
		String normalized = baseUrl.trim();
		this.baseUrl = normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
		this.appKey = appKey;
		this.appSecret = appSecret;
	}

	// ===== 密钥管理 =====

	/** 在平台创建托管密钥 */
	public KeyCreateResponse createKey(String algorithm, String alias) {
		KeyCreateRequest request = new KeyCreateRequest();
		request.setAlgorithm(algorithm);
		request.setAlias(alias);
		return post("/api/key/create", request, new TypeReference<ApiResponse<KeyCreateResponse>>() {
		});
	}

	/** 查询本应用全部密钥 */
	public List<KeyView> listKeys() {
		return get("/api/key/list", new TypeReference<ApiResponse<List<KeyView>>>() {
		});
	}

	/** 查询密钥详情 */
	public KeyView getKey(String keyId) {
		return get("/api/key/" + keyId, new TypeReference<ApiResponse<KeyView>>() {
		});
	}

	/** 销毁密钥（不可恢复） */
	public void destroyKey(String keyId) {
		delete("/api/key/" + keyId);
	}

	// ===== 对称加解密 =====

	/** 对称加密（SM4/AES） */
	public EncryptResponse symmetricEncrypt(String keyId, String plainText) {
		SymmetricEncryptRequest request = new SymmetricEncryptRequest();
		request.setKeyId(keyId);
		request.setPlainText(plainText);
		return post("/api/crypto/symmetric/encrypt", request, new TypeReference<ApiResponse<EncryptResponse>>() {
		});
	}

	/** 对称解密（SM4/AES） */
	public DecryptResponse symmetricDecrypt(String keyId, String cipherText) {
		SymmetricDecryptRequest request = new SymmetricDecryptRequest();
		request.setKeyId(keyId);
		request.setCipherText(cipherText);
		return post("/api/crypto/symmetric/decrypt", request, new TypeReference<ApiResponse<DecryptResponse>>() {
		});
	}

	// ===== 非对称加解密 =====

	/** 非对称公钥加密（SM2/RSA，小段数据） */
	public EncryptResponse asymmetricEncrypt(String keyId, String plainText) {
		AsymmetricEncryptRequest request = new AsymmetricEncryptRequest();
		request.setKeyId(keyId);
		request.setPlainText(plainText);
		return post("/api/crypto/asymmetric/encrypt", request, new TypeReference<ApiResponse<EncryptResponse>>() {
		});
	}

	/** 非对称私钥解密（SM2/RSA） */
	public DecryptResponse asymmetricDecrypt(String keyId, String cipherText) {
		AsymmetricDecryptRequest request = new AsymmetricDecryptRequest();
		request.setKeyId(keyId);
		request.setCipherText(cipherText);
		return post("/api/crypto/asymmetric/decrypt", request, new TypeReference<ApiResponse<DecryptResponse>>() {
		});
	}

	// ===== 摘要 / MAC / 签名 =====

	/** 摘要计算（SM3/MD5/SHA_256，无需密钥） */
	public DigestResponse digest(String algorithm, String data) {
		DigestRequest request = new DigestRequest();
		request.setAlgorithm(algorithm);
		request.setData(data);
		return post("/api/crypto/digest", request, new TypeReference<ApiResponse<DigestResponse>>() {
		});
	}

	/** 消息认证码（HMAC-SM3/HMAC-SHA256） */
	public MacResponse mac(String keyId, String data) {
		MacRequest request = new MacRequest();
		request.setKeyId(keyId);
		request.setData(data);
		return post("/api/crypto/mac", request, new TypeReference<ApiResponse<MacResponse>>() {
		});
	}

	/** 数字签名（SM2→SM3withSM2 / RSA→SHA256withRSA） */
	public SignResponse sign(String keyId, String data) {
		SignRequest request = new SignRequest();
		request.setKeyId(keyId);
		request.setData(data);
		return post("/api/crypto/sign", request, new TypeReference<ApiResponse<SignResponse>>() {
		});
	}

	/** 公钥验签 */
	public VerifyResponse verify(String keyId, String data, String signature) {
		VerifyRequest request = new VerifyRequest();
		request.setKeyId(keyId);
		request.setData(data);
		request.setSignature(signature);
		return post("/api/crypto/verify", request, new TypeReference<ApiResponse<VerifyResponse>>() {
		});
	}

	// ===== HTTP 基础设施 =====

	private <T> T post(String path, Object body, TypeReference<ApiResponse<T>> type) {
		byte[] bodyBytes;
		try {
			bodyBytes = body == null ? new byte[0] : objectMapper.writeValueAsBytes(body);
		} catch (IOException e) {
			throw new OnlineSdkException(500, "SDK请求体序列化失败：" + e.getMessage());
		}
		return execute("POST", path, bodyBytes, type);
	}

	private <T> T get(String path, TypeReference<ApiResponse<T>> type) {
		return execute("GET", path, new byte[0], type);
	}

	private void delete(String path) {
		execute("DELETE", path, new byte[0], new TypeReference<ApiResponse<Void>>() {
		});
	}

	private <T> T execute(String method, String path, byte[] bodyBytes, TypeReference<ApiResponse<T>> type) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(baseUrl + path);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			if (bodyBytes.length > 0 || "POST".equals(method)) {
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			}

			// ===== 接入签名：时间戳 + nonce + HMAC-SM3 =====
			String timestamp = String.valueOf(System.currentTimeMillis());
			String nonce = UUID.randomUUID().toString().replace("-", "");
			String stringToSign = SignatureUtil.buildStringToSign(appKey, timestamp, nonce, method, path, bodyBytes);
			String signature = SignatureUtil.sign(appSecret, stringToSign);
			connection.setRequestProperty(Constants.HEADER_APP_KEY, appKey);
			connection.setRequestProperty(Constants.HEADER_TIMESTAMP, timestamp);
			connection.setRequestProperty(Constants.HEADER_NONCE, nonce);
			connection.setRequestProperty(Constants.HEADER_SIGNATURE, signature);

			if (connection.getDoOutput()) {
				try (OutputStream out = connection.getOutputStream()) {
					out.write(bodyBytes);
					out.flush();
				}
			}

			int httpCode = connection.getResponseCode();
			String responseBody = readAll(httpCode >= 400 ? connection.getErrorStream() : connection.getInputStream());
			if (responseBody == null || responseBody.trim().isEmpty()) {
				throw new OnlineSdkException(httpCode, "平台无响应，HTTP状态码：" + httpCode);
			}

			ApiResponse<T> apiResponse = objectMapper.readValue(responseBody, type);
			if (apiResponse == null || apiResponse.getCode() != ApiResponse.CODE_SUCCESS) {
				int code = apiResponse == null ? httpCode : apiResponse.getCode();
				String message = apiResponse == null ? "未知错误" : apiResponse.getMessage();
				throw new OnlineSdkException(code, message);
			}
			return apiResponse.getData();
		} catch (OnlineSdkException e) {
			throw e;
		} catch (IOException e) {
			throw new OnlineSdkException(500, "连接密码服务平台失败：" + e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String readAll(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return null;
		}
		java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
		byte[] chunk = new byte[4096];
		int n;
		while ((n = inputStream.read(chunk)) != -1) {
			buffer.write(chunk, 0, n);
		}
		return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
}
