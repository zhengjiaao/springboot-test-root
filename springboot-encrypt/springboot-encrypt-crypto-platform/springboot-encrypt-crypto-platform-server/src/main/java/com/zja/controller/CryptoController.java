package com.zja.controller;

import com.zja.common.ApiResponse;
import com.zja.common.Constants;
import com.zja.dto.request.AsymmetricDecryptRequest;
import com.zja.dto.request.AsymmetricEncryptRequest;
import com.zja.dto.request.DigestRequest;
import com.zja.dto.request.MacRequest;
import com.zja.dto.request.SignRequest;
import com.zja.dto.request.SymmetricDecryptRequest;
import com.zja.dto.request.SymmetricEncryptRequest;
import com.zja.dto.request.VerifyRequest;
import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import com.zja.service.CryptoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码运算（需接入认证：X-App-Key/X-Timestamp/X-Nonce/X-Signature）
 *
 * <p>密改核心能力：对称/非对称加解密、摘要、MAC、签名验签；密钥由平台托管，运算在平台侧完成。</p>
 *
 * @author: zhengja
 */
@RestController
@RequestMapping(value = "/api/crypto")
@Api(tags = {"密码运算（在线接入）"})
public class CryptoController {

	private final CryptoService cryptoService;

	public CryptoController(CryptoService cryptoService) {
		this.cryptoService = cryptoService;
	}

	@ApiOperation(value = "对称加密（SM4/AES）", notes = "使用平台托管的对称密钥加密，密文Base64（前16字节为随机IV），适合大批量数据")
	@PostMapping("symmetric/encrypt")
	public ApiResponse<EncryptResponse> symmetricEncrypt(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody SymmetricEncryptRequest request) {
		return ApiResponse.ok(cryptoService.symmetricEncrypt(appKey, request));
	}

	@ApiOperation(value = "对称解密（SM4/AES）", notes = "解密symmetric/encrypt产生的Base64密文")
	@PostMapping("symmetric/decrypt")
	public ApiResponse<DecryptResponse> symmetricDecrypt(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody SymmetricDecryptRequest request) {
		return ApiResponse.ok(cryptoService.symmetricDecrypt(appKey, request));
	}

	@ApiOperation(value = "非对称加密（SM2/RSA）", notes = "公钥加密，仅适合小段数据（如会话密钥）；大批量数据请使用数字信封：SM2加密会话密钥 + SM4加密业务数据")
	@PostMapping("asymmetric/encrypt")
	public ApiResponse<EncryptResponse> asymmetricEncrypt(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody AsymmetricEncryptRequest request) {
		return ApiResponse.ok(cryptoService.asymmetricEncrypt(appKey, request));
	}

	@ApiOperation(value = "非对称解密（SM2/RSA）", notes = "平台侧私钥解密")
	@PostMapping("asymmetric/decrypt")
	public ApiResponse<DecryptResponse> asymmetricDecrypt(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody AsymmetricDecryptRequest request) {
		return ApiResponse.ok(cryptoService.asymmetricDecrypt(appKey, request));
	}

	@ApiOperation(value = "摘要计算（SM3/MD5/SHA_256）", notes = "无需密钥；密改对标：MD5/SHA→SM3")
	@PostMapping("digest")
	public ApiResponse<DigestResponse> digest(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody DigestRequest request) {
		return ApiResponse.ok(cryptoService.digest(appKey, request));
	}

	@ApiOperation(value = "消息认证码（HMAC-SM3/HMAC-SHA256）", notes = "使用平台托管的MAC密钥计算，用于数据完整性+来源真实性校验")
	@PostMapping("mac")
	public ApiResponse<MacResponse> mac(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody MacRequest request) {
		return ApiResponse.ok(cryptoService.mac(appKey, request));
	}

	@ApiOperation(value = "数字签名", notes = "SM2密钥→SM3withSM2签名，RSA密钥→SHA256withRSA签名；私钥不出平台")
	@PostMapping("sign")
	public ApiResponse<SignResponse> sign(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody SignRequest request) {
		return ApiResponse.ok(cryptoService.sign(appKey, request));
	}

	@ApiOperation(value = "验签", notes = "使用密钥对中的公钥验签，返回验签结果")
	@PostMapping("verify")
	public ApiResponse<VerifyResponse> verify(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody VerifyRequest request) {
		return ApiResponse.ok(cryptoService.verify(appKey, request));
	}
}
